package com.hvadoda1.dht.chord.starter;

import java.util.Map;

import org.apache.thrift.TException;

import com.hvadoda1.dht.chord.Config;
import com.hvadoda1.dht.chord.rpc.thrift.connect.ThriftConfig;
import com.hvadoda1.dht.chord.rpc.thrift.connect.ThriftServer;
import com.hvadoda1.dht.chord.rpc.thrift.connect.ThriftServerController;

public class ThriftChordServiceStarter extends ChordServiceStarter<ThriftServerController, ThriftServer, TException> {

	public ThriftChordServiceStarter(Map<String, String> argsMap) throws TException {
		super(argsMap);
	}

	@Override
	protected ThriftServerController initController(int port) throws TException {
		return new ThriftServerController(port);
	}

	@Override
	protected Config initConfig(Map<String, String> argsMap) {
		return new ThriftConfig(argsMap);
	}

}