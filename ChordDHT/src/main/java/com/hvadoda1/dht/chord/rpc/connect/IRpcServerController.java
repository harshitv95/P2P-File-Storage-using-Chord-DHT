package com.hvadoda1.dht.chord.rpc.connect;

public interface IRpcServerController<Server extends IRpcServer<?, ?, ?, ?>, Exc extends Exception> extends Runnable {
	Server createServerHandler() throws Exc;
}
