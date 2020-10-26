package com.hvadoda1.dht.chord.rpc.connect;

import java.io.Closeable;

public interface IRpcConnection<Node extends IChordNode, Client extends IRpcClient<Node, ? extends Exception>>
		extends Closeable {
	Client connect();

	boolean isOpen();
}
