package com.hvadoda1.dht.chord.rpc.connect;

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

	protected final Node node;
	protected Node pred;
	protected List<Node> fingerTable;
	protected final java.io.File uploadsDir;

	protected final Map<String, FileMeta> idKeyMap = new HashMap<>();

	public AbstractRpcServer(Node node) throws Exc {
		this.node = node;
		this.uploadsDir = new java.io.File(Config.hostUploadsDir(node.getPort()));
		initialize();
		Logger.info("Thrift server running on: [" + CommonUtils.nodeAddress(node) + "]");
	}

	public AbstractRpcServer(String id, String host, int port) throws Exc {
		this.node = createNode(Objects.requireNonNullElseGet(id, () -> CommonUtils.serverID(host, port)), host, port);
		this.uploadsDir = new java.io.File(Config.hostUploadsDir(node.getPort()));
		initialize();
		Logger.info("Thrift server running on: [" + CommonUtils.nodeAddress(node) + "]");
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
		initialize();
	}

	public AbstractRpcServer(int port) throws Exc {
		this(null, port);
		initialize();
	}

	@Override
	public void writeFile(File file) throws Exc {
		Objects.requireNonNull(file, "File to write cannot be null");
		Logger.info("Attempting to write file", file.getMeta().getFilename());
		String fileId = CommonUtils.keyToID(file.getMeta().getFilename());
		if (!isOwnerOfId(fileId)) {
			Logger.debugLow("Current server does not own id of file [" + file.getMeta().getFilename()
					+ "].\nCurrent server's ID: [" + node.getId() + "], file's ID: [" + fileId + "]");
			throw generateException("Current server does not own id of file [" + file.getMeta().getFilename() + "]");
		}
		if (!idKeyMap.containsKey(fileId)) {
			Logger.debugLow("Mapping ID [" + fileId + "] to file [" + file.getMeta().getFilename() + "]");
			idKeyMap.put(fileId, file.getMeta());
		}
		java.io.File f = new java.io.File(uploadsDir, file.getMeta().getFilename());
		if (file.getMeta().getFilename().contains(java.io.File.separator))
			f.getParentFile().mkdirs();
		try {
			FileUtils.writeFile(f, file.getContent());
			file.getMeta().setVersion(file.getMeta().getVersion() + 1);
			Logger.info("Updated file " + file.getMeta().getFilename() + " version to " + file.getMeta().getVersion());
		} catch (IOException e) {
			Logger.error("Exception while writing to file:\n" + e.getMessage(), e);
			throw generateException("Failed to create new file: [" + e.getMessage() + "]");
		}

	}

	@Override
	public File readFile(String filename) throws Exc {
		Logger.info("Attempting to read file", filename);
		String fileId = CommonUtils.keyToID(filename);
		if (!isOwnerOfId(fileId)) {
			Logger.debugLow("Current server does not own id of file [" + filename + "].\nCurrent server's ID: ["
					+ node.getId() + "], file's ID: [" + fileId + "]");
			throw generateException("Current server does not own id of file [" + filename + "]");
		}
		if (!idKeyMap.containsKey(fileId))
			throw generateException("File with name [" + filename + "] does not exist");
		File file = createFile(idKeyMap.get(fileId));
		java.io.File f = new java.io.File(file.getMeta().getFilename());
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
		}
	}

	@Override
	public Node findSucc(String key) throws Exc {
		Logger.debugLow("Finding Succesor of key [" + key + "]");
		Node pred = findPred(key);
		Logger.debugHigh("Predecessor of key [" + key + "] = [" + CommonUtils.nodeAddress(pred) + "]");
		try (Conn conn = getConnection(pred);) {
			Client client = conn.connect();
			return client.getNodeSucc();
		} catch (IOException e) {
			Logger.error(e.getMessage(), e);
			throw generateException("Failed to find Successor of key [" + key + "]:\n" + e.getMessage());
		}

//		for (int i = fingerTable.size() - 1; i >= 0; i--)
//			if (fingerTable.get(i).getId().compareTo(this.node.getId()) < 1)
//				return fingerTable.get(i);
//		throw generateException("")
	}

	@Override
	public Node findPred(String key) throws Exc {
		Logger.debugLow("Finding Predecessor of key [" + key + "]");
		if (key.compareTo(node.getId()) > 0 && key.compareTo(getNodeSucc().getId()) < 1) {
			Logger.debugLow("Found Predecessor of key [" + key + "] = [" + CommonUtils.nodeAddress(node) + "]");
			return node;
		}
		if (fingerTable == null || fingerTable.isEmpty())
			throw generateException(
					"Finger table is not set, cannot Find Predecessor of Node [" + CommonUtils.nodeAddress(node) + "]");
		Node target = null;
		for (int i = fingerTable.size() - 1; i >= 0; i--)
			if ((target = fingerTable.get(i)).getId().compareTo(key) == -1)
				break;

		if (target == null)
			target = fingerTable.get(fingerTable.size() - 1);
		try (Conn conn = getConnection(target);) {
			Client client = conn.connect();
			return client.findPred(key);
		} catch (IOException e) {
			Logger.error(e.getMessage(), e);
			throw generateException("Failed to find Predecessor of key [" + key + "]:\n" + e.getMessage());
		}

//		throw generateException("Unable to find predecessor of key [" + key + "] in current Chord ring");
	}

	@Override
	public Node getNodeSucc() throws Exc {
		if (fingerTable == null || fingerTable.isEmpty())
			throw generateException("Cannot get successor of Node [" + CommonUtils.nodeAddress(node)
					+ "], Finger table is not set or no other nodes exist in Chord Network");
		return fingerTable.get(0);
	}

	@Override
	public Node getNode() {
		return node;
	}

	@Override
	public boolean isOwnerOfId(String id) throws Exc {
		if (pred == null)
			pred = findPred(node.getId());
		return (id.compareTo(pred.getId()) > 0 && id.compareTo(node.getId()) < 1);
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
