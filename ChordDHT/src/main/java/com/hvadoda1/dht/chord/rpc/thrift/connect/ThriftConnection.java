package com.hvadoda1.dht.chord.rpc.thrift.connect;

import java.io.IOException;

import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSSLTransportFactory;
import org.apache.thrift.transport.TTransport;

import com.hvadoda1.dht.chord.rpc.connect.IRpcConnection;
import com.hvadoda1.dht.chord.rpc.thrift.generated.FileStore;
import com.hvadoda1.dht.chord.rpc.thrift.generated.FileStore.Client;
import com.hvadoda1.dht.chord.rpc.thrift.generated.NodeID;

public class ThriftConnection implements IRpcConnection<NodeID, FileStore.Client> {
	
	private TTransport transport;
	private FileStore.Client client;

	private final NodeID node;

	public ThriftConnection(NodeID node) {
		this.node = node;
	}

	@Override
	public Client connect() {
		if (transport != null && !transport.isOpen())
			if (client != null)
				return client;

		try {
//			TSSLTransportParameters params = new TSSLTransportParameters();
//			params.setTrustStore("/home/cs557-inst/thrift-0.13.0/lib/java/test/.truststore", "thrift", "SunX509",
//					"JKS");

			TTransport transport = TSSLTransportFactory.getClientSocket(node.getIp(), node.getPort(), 0);
			if (!transport.isOpen())
				transport.open();
			TProtocol protocol = new TBinaryProtocol(transport);
			return new FileStore.Client(protocol);
		} catch (TException x) {
			x.printStackTrace();
			return null;
		}
	}

	@Override
	public void close() throws IOException {
		if (transport.isOpen())
			transport.close();
	}

	@Override
	public boolean isOpen() {
		return transport.isOpen();
	}

}
