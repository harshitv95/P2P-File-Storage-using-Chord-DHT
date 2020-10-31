package com.hvadoda1.dht.chord.rpc.thrift.connect;

import static com.hvadoda1.dht.chord.util.NodeUtils.nodeAddress;

import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;

import com.hvadoda1.dht.chord.rpc.connect.IRpcConnection;
import com.hvadoda1.dht.chord.rpc.thrift.generated.FileStore;
import com.hvadoda1.dht.chord.rpc.thrift.generated.FileStore.Client;
import com.hvadoda1.dht.chord.rpc.thrift.generated.NodeID;
import com.hvadoda1.dht.chord.util.Logger;
import com.hvadoda1.dht.chord.util.NodeUtils;

public class ThriftConnection implements IRpcConnection<NodeID, FileStore.Client> {

	private TTransport transport;
	private FileStore.Client client;

	private final NodeID node;

	public ThriftConnection(NodeID node) {
		Logger.debugHigh("Initializing connection to server: [" + nodeAddress(node) + "]");
		this.node = node;
	}

	@Override
	public Client connect() {
		Logger.debugLow("Attempting to connect to server node [" + nodeAddress(node) + "]");
		if (transport != null && !transport.isOpen())
			if (client != null)
				return client;

		try {
			return client = new FileStore.Client(setupTransport());
		} catch (TException x) {
			x.printStackTrace();
			return null;
		}
	}

	@Override
	public void close() {
		if (transport.isOpen())
			transport.close();
	}

	@Override
	public boolean isOpen() {
		return transport.isOpen();
	}

	protected TProtocol setupTransport() throws TTransportException {
		Logger.debugHigh("Initializing transport -> " + NodeUtils.nodeAddress(node));
//		TSSLTransportParameters params = new TSSLTransportParameters();
//		params.setTrustStore(
//				ThriftConfig.getTrustStorePath(),
//				ThriftConfig.getTrustStorePassword(),
//				ThriftConfig.getTrustStoreManager(),
//				ThriftConfig.getTrustStoreType()
//			);
//		transport = TSSLTransportFactory.getClientSocket(node.getIp(), node.getPort(), 0, params);
		transport = new TSocket(node.getIp(), node.getPort());
		if (!transport.isOpen())
			transport.open();
		return new TBinaryProtocol(transport);
	}

}
