package com.hvadoda1.dht.chord.rpc.connect;

import java.util.List;

import com.hvadoda1.dht.chord.IFile;
import com.hvadoda1.dht.chord.IFileMeta;

public interface IRpcServer<File extends IFile, FileMeta extends IFileMeta, Node extends IChordNode, Exc extends Exception> {
	/**
	 * Writes a file to the appropriate node (having the ID as the immediate
	 * successor of file's generated ID)
	 * 
	 * @param filename Name of the file
	 * @param contents Contents to be written to the file
	 * @throws Exc 
	 */
	void writeFile(File file) throws Exc;

	/**
	 * Reads a file from the appropriate node (having the ID as the immediate
	 * successor of file's generated ID)
	 * 
	 * @param filename Name of the file to read
	 * @return Instance of {@link IFile} containing metadata and contents of file
	 * @throws Exception If the file was not found on the corresponding server
	 */
	File readFile(String filename) throws Exc;

	void setFingertable(List<Node> node_list) throws Exc;

	Node findSucc(String key) throws Exc;

	Node findPred(String key) throws Exc;

	Node getNodeSucc() throws Exc;
	
	Node getNode();
	
	boolean isOwnerOfId(String id) throws Exc;

}
