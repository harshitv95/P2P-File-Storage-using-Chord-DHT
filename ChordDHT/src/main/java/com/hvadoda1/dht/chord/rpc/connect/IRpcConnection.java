package com.hvadoda1.dht.chord.rpc.connect;

public interface IRpcConnection<Node extends IChordNode, Client extends IRpcClient<Node, ? extends Exception>> {
	Client getConnection(Node node);
}
