package com.hvadoda1.dht.chord.starter;

import com.hvadoda1.dht.chord.rpc.connect.IRpcServer;
import com.hvadoda1.dht.chord.rpc.connect.IRpcServerController;

public enum ChordImpl {
	Thrift(ThriftChordServiceStarter.class);

	Class<? extends ChordServiceStarter<? extends IRpcServerController<? extends IRpcServer<?, ?, ?, ?>, ? extends Exception>, ? extends IRpcServer<?, ?, ?, ?>, ? extends Exception>> clazz;

	<C extends IRpcServerController<S, E>, S extends IRpcServer<?, ?, ?, E>, E extends Exception> ChordImpl(
			Class<? extends ChordServiceStarter<?, ?, ?>> clazz) {
		this.clazz = clazz;
	}

	<C extends IRpcServerController<S, E>, S extends IRpcServer<?, ?, ?, E>, E extends Exception> Class<? extends ChordServiceStarter<?, ?, ?>> getChordImplClass() {
		return clazz;
	}

}
