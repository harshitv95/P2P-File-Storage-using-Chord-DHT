package com.hvadoda1.dht.chord.rpc.connect;

public interface IRpcClient<Node extends IChordNode, Exc extends Exception> {
	Node findSucc(String key) throws Exc;

	Node findPred(String key) throws Exc;

	Node getNodeSucc() throws Exc;
}
