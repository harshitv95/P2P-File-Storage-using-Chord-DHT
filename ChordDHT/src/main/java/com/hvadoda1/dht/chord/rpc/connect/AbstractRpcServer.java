package com.hvadoda1.dht.chord.rpc.connect;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.hvadoda1.dht.chord.IFile;
import com.hvadoda1.dht.chord.IFileMeta;
import com.hvadoda1.dht.chord.util.CommonUtils;
import com.hvadoda1.dht.chord.util.FileUtils;
import com.hvadoda1.dht.chord.util.Logger;

public abstract class AbstractRpcServer<File extends IFile, FileMeta extends IFileMeta, Node extends IChordNode, Exc extends Exception>
		implements IRpcServer<File, FileMeta, Node, Exc> {

	protected final Node node;
	protected Node pred;
	protected List<Node> fingerTable;

	protected final Map<String, FileMeta> idKeyMap = new HashMap<>();

	public AbstractRpcServer(Node node) throws Exc {
		this.node = node;
		this.pred = findPred(node.getId());
	}

	public AbstractRpcServer(String id, String host, int port) throws Exc {
		this.node = createNode(Objects.requireNonNullElseGet(id, () -> CommonUtils.serverID(host, port)), host, port);
		this.pred = findPred(node.getId());
	}

	public AbstractRpcServer(String host, int port) throws Exc {
		this(null, host, port);
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
		java.io.File f = new java.io.File(file.getMeta().getFilename());
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
		if (fingerTable != null)
			this.fingerTable = fingerTable;
	}

	@Override
	public Node findSucc(String key) throws Exc {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Node findPred(String key) throws Exc {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Node getNodeSucc() throws Exc {
		if (fingerTable == null)
			throw generateException(
					"Cannot get successor of Node [" + CommonUtils.nodeAddress(node) + "], Finger table is not set");
		return fingerTable.get(0);
	}

	@Override
	public Node getNode() {
		return node;
	}

	@Override
	public boolean isOwnerOfId(String id) {
		return (id.compareTo(pred.getId()) > 0 && id.compareTo(node.getId()) < 1);
	}

	protected abstract File createFile(FileMeta meta);

	protected abstract Node createNode(String id, String host, int port);

	protected abstract Exc generateException(String message);
}
