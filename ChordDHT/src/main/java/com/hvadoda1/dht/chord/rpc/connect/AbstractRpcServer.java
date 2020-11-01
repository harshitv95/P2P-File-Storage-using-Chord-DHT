package com.hvadoda1.dht.chord.rpc.connect;

import static com.hvadoda1.dht.chord.util.NodeUtils.isPredOf;
import static com.hvadoda1.dht.chord.util.NodeUtils.isSuccOf;
import static com.hvadoda1.dht.chord.util.NodeUtils.nodeAddress;
import static com.hvadoda1.dht.chord.util.StringUtils.keyToID;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.hvadoda1.dht.chord.Config;
import com.hvadoda1.dht.chord.IFile;
import com.hvadoda1.dht.chord.IFileMeta;
import com.hvadoda1.dht.chord.util.CommonUtils;
import com.hvadoda1.dht.chord.util.FileUtils;
import com.hvadoda1.dht.chord.util.Logger;

public abstract class AbstractRpcServer<File extends IFile, FileMeta extends IFileMeta, Node extends IChordNode, Client extends IRpcClient<Node, Exc>, Conn extends IRpcConnection<Node, Client>, Exc extends Exception>
		implements IRpcServer<File, FileMeta, Node, Exc> {

	protected final java.io.File uploadsDir;
	protected final Node node;
	protected final Map<String, FileMeta> idMetaMap = new HashMap<>();

	protected Node pred;
	protected List<Node> fingerTable;
	protected int intialFingerTableLen;

	public AbstractRpcServer(Node node) throws Exc {
		this.node = node;
		this.uploadsDir = new java.io.File(Config.hostUploadsDir(node.getPort()));
		initialize();
		Logger.setHost(nodeAddress(node));
		Logger.info("Chord server running on: [" + nodeAddress(node) + "]");
		Logger.debugLow("Server ID: [" + node.getId() + "]");
	}

	public AbstractRpcServer(String id, String host, int port) throws Exc {
		this.node = createNode(Objects.requireNonNullElseGet(id, () -> CommonUtils.serverID(host, port)), host, port);
		this.uploadsDir = new java.io.File(Config.hostUploadsDir(node.getPort()));
		initialize();
		Logger.setHost(nodeAddress(node));
		Logger.info("Chord server running on: [" + nodeAddress(node) + "]");
		Logger.debugLow("Server ID: [" + node.getId() + "]");
	}

	public AbstractRpcServer(String host, int port) throws Exc {
		this(null, Objects.requireNonNullElseGet(host, () -> {
			try {
				return InetAddress.getLocalHost().getHostAddress();
			} catch (UnknownHostException e) {
				Logger.error("Failed to get current host's IP Address:\n" + e.getMessage(), e);
				e.printStackTrace();
				throw new RuntimeException("Failed to get current host's IP Address", e);
			}
		}), port);
	}

	public AbstractRpcServer(int port) throws Exc {
		this(null, port);
	}

	@Override
	public void writeFile(File file) throws Exc {
		Objects.requireNonNull(file, "File to write cannot be null");
		Logger.info("Attempting to write file", file.getMeta().getFilename());
		String fileId = keyToID(file.getMeta().getFilename());
		if (!isSuccOfId(fileId)) {
			Logger.debugLow("Current server does not own id of file [" + file.getMeta().getFilename()
					+ "].\nCurrent server's ID: [" + node.getId() + "], file's ID: [" + fileId + "]");
			throw generateException("Current server does not own id of file [" + file.getMeta().getFilename() + "]");
		}

		java.io.File f = new java.io.File(uploadsDir, file.getMeta().getFilename());
		if (file.getMeta().getFilename().contains(java.io.File.separator))
			f.getParentFile().mkdirs();
		try {
			FileUtils.writeFile(f, file.getContent());

			if (!idMetaMap.containsKey(fileId)) {
				Logger.debugLow("Mapping ID [" + fileId + "] to file [" + file.getMeta().getFilename() + "]");
				idMetaMap.put(fileId, file.getMeta());
				file.getMeta().setVersion(-1);
			}

			idMetaMap.get(fileId).setVersion(idMetaMap.get(fileId).getVersion() + 1);
			Logger.info("Updated file [" + file.getMeta().getFilename() + "], version to ["
					+ idMetaMap.get(fileId).getVersion() + "]");
		} catch (IOException e) {
			Logger.error("Exception while writing to file:\n" + e.getMessage(), e);
			throw generateException("Failed to create new file: [" + e.getMessage() + "]");
		}

	}

	@Override
	public File readFile(String filename) throws Exc {
		Logger.info("Attempting to read file", filename);
		String fileId = keyToID(filename);
		if (!isSuccOfId(fileId)) {
			Logger.debugLow("Current server does not own id of file [" + filename + "]\nCurrent server's ID: ["
					+ node.getId() + "], file's ID: [" + fileId + "]");
			throw generateException("Current server does not own id of file [" + filename + "]");
		}
		if (!idMetaMap.containsKey(fileId)) {
			Logger.error("File with name [" + filename + "] does not exist", null);
			throw generateException("File with name [" + filename + "] does not exist");
		}
		File file = createFile(idMetaMap.get(fileId));
		java.io.File f = new java.io.File(uploadsDir, file.getMeta().getFilename());
		try {
			file.setContent(FileUtils.readFile(f));
		} catch (IOException e) {
			Logger.error("Exception while reading from file:\n" + e.getMessage(), e);
			throw generateException("Failed to read file: [" + e.getMessage() + "]");
		}
		return file;
	}

	@Override
	public void setFingertable(List<Node> fingerTable) throws Exc {
		if (fingerTable != null) {
			Logger.debugLow("New Finger Table", fingerTable);
			this.fingerTable = fingerTable;
			intialFingerTableLen = this.fingerTable.size();
		}
	}

	@Override
	public Node findSucc(String key) throws Exc {
		Logger.debugLow("Finding Succesor of key [" + key + "]");
		if (isPredOfId(key)) {
			Logger.debugHigh("Predecessor of key [" + key + "] = [" + nodeAddress(node) + "]");
			return getNodeSucc();
		} else {
			Node pred = findPred(key);
			Logger.debugHigh("Predecessor of key [" + key + "] = [" + nodeAddress(pred) + "]");
			return getNodeSucc(pred);
		}
	}

	@Override
	public Node findPred(String key) throws Exc {
		Logger.debugLow("Finding Predecessor of key [" + key + "]");
		if (fingerTable == null || fingerTable.isEmpty())
			throw generateException(
					"Finger table is not set, cannot Find Predecessor of Node [" + nodeAddress(node) + "]");
		if (isPredOfId(key)) {
			Logger.debugLow("Found Predecessor of key [" + key + "] = [" + nodeAddress(node) + "]");
			return node;
		}
		if (fingerTable.size() == intialFingerTableLen) {
			if (fingerTable.get(fingerTable.size() - 1).getId().equals(node.getId()))
				fingerTable.add(getNodeSucc());
			else
				fingerTable.add(getNodeSucc(fingerTable.get(fingerTable.size() - 1)));
		}

		Node target = null, targetSucc = fingerTable.get(fingerTable.size() - 1);

		for (int i = fingerTable.size() - 2; i >= 0; i--, targetSucc = target, target = null)
			if (isPredOf(target = fingerTable.get(i), key, targetSucc)) {
				Logger.debugLow(
						"isPredOf(" + nodeAddress(target) + ", " + key + ", " + nodeAddress(targetSucc) + ") -> true");
				break;
			} else {
				Logger.debugLow(
						"isPredOf(" + nodeAddress(target) + ", " + key + ", " + nodeAddress(targetSucc) + ") -> false");
			}

		if (target == null) {
			target = fingerTable.get(fingerTable.size() - 2);
			Logger.debugLow("No Target found, setting to default: [" + nodeAddress(target) + "]");
		}
		try (Conn conn = getConnection(target);) {
			Logger.debugLow("Target found : [" + nodeAddress(target) + "]");
			Client client = conn.connect();
			return client.findPred(key);
		} catch (IOException e) {
			Logger.error(e.getMessage(), e);
			throw generateException("Failed to find Predecessor of key [" + key + "]:\n" + e.getMessage());
		}
	}

	@Override
	public Node getNodeSucc() throws Exc {
		if (fingerTable == null || fingerTable.isEmpty())
			throw generateException("Cannot get successor of Node [" + nodeAddress(node)
					+ "], Finger table is not set or no other nodes exist in Chord Network");
		return fingerTable.get(0);
	}

	protected Node getNodeSucc(Node node) throws Exc {
		try (Conn conn = getConnection(node);) {
			Client client = conn.connect();
			return client.getNodeSucc();
		} catch (IOException e) {
			Logger.error(e.getMessage(), e);
			throw new RuntimeException(
					"Failed to find Successor of Node [" + nodeAddress(node) + "]:\n" + e.getMessage());
		}
	}

	@Override
	public Node getNode() {
		return node;
	}

	@Override
	public boolean isSuccOfId(String id) throws Exc {
		if (pred == null)
			pred = findPred(node.getId());
		return isSuccOf(node, id, pred);
//		return (id.compareTo(pred.getId()) > 0 && id.compareTo(node.getId()) < 1);
	}

	@Override
	public boolean isPredOfId(String id) throws Exc {
		return isPredOf(node, id, getNodeSucc());
	}

	protected void initialize() {
		if (!uploadsDir.exists() || !uploadsDir.isDirectory())
			uploadsDir.mkdirs();
		FileUtils.deleteDirectory(uploadsDir, false);
	}

	protected abstract Conn getConnection(Node node);

	protected abstract File createFile(FileMeta meta);

	protected abstract Node createNode(String id, String host, int port);

	protected abstract Exc generateException(String message);
}
