package com.hvadoda1.dht.chord.rpc.thrift.connect;

import org.apache.thrift.TException;

import com.hvadoda1.dht.chord.rpc.connect.AbstractRpcServer;
import com.hvadoda1.dht.chord.rpc.thrift.generated.FileStore;
import com.hvadoda1.dht.chord.rpc.thrift.generated.NodeID;
import com.hvadoda1.dht.chord.rpc.thrift.generated.RFile;
import com.hvadoda1.dht.chord.rpc.thrift.generated.RFileMetadata;
import com.hvadoda1.dht.chord.rpc.thrift.generated.SystemException;
import com.hvadoda1.dht.chord.util.Logger;

public class ThriftServer
		extends AbstractRpcServer<RFile, RFileMetadata, NodeID, FileStore.Client, ThriftConnection, TException>
		implements FileStore.Iface {

	public ThriftServer(int port) throws TException {
		super(port);
		Logger.debugHigh("Initializing Thrift server handler");
	}

	public ThriftServer(NodeID node) throws TException {
		super(node);
		Logger.debugHigh("Initializing Thrift server handler");
	}

	public ThriftServer(String host, int port) throws TException {
		super(host, port);
		Logger.debugHigh("Initializing Thrift server handler");
	}

	public ThriftServer(String id, String host, int port) throws TException {
		super(id, host, port);
		Logger.debugHigh("Initializing Thrift server handler");
	}

	@Override
	protected ThriftConnection getConnection(NodeID node) {
		return new ThriftConnection(node);
	}

	@Override
	protected RFile createFile(RFileMetadata meta) {
		RFile file = new RFile();
		file.setMeta(meta);
		return file;
	}

	@Override
	protected NodeID createNode(String id, String host, int port) {
		return new NodeID(id, host, port);
	}

	@Override
	protected TException generateException(String message) {
		SystemException sysExc = new SystemException();
		sysExc.setMessage(message);
		return sysExc;
	}
}
