package com.hvadoda1.dht.chord.rpc.thrift.connect;

import org.apache.thrift.TException;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TSimpleServer;
import org.apache.thrift.transport.TSSLTransportFactory;
import org.apache.thrift.transport.TServerTransport;
import org.apache.thrift.transport.TTransportException;

import com.hvadoda1.dht.chord.rpc.connect.IRpcServerController;
import com.hvadoda1.dht.chord.rpc.thrift.generated.FileStore;
import com.hvadoda1.dht.chord.util.Logger;

public class ThriftServerController implements IRpcServerController<ThriftServer, TException> {

	protected final int port;
	protected final FileStore.Processor<ThriftServer> processor;

	public ThriftServerController(int port) throws TException {
		this.port = port;
		this.processor = new FileStore.Processor<>(createServerHandler());
	}

	@Override
	public void run() {
		try {
			TServer server = setupServer();
			server.serve();
		} catch (TTransportException e) {
			Logger.error("Could noyt start RPC Server", e);
			e.printStackTrace();
		}
	}

	@Override
	public ThriftServer createServerHandler() throws TException {
		return new ThriftServer(port);
	}

	protected TServer setupServer() throws TTransportException {
		return new TSimpleServer(new TServer.Args(getTransport()).processor(processor));
	}

	protected TServerTransport getTransport() throws TTransportException {
		return TSSLTransportFactory.getServerSocket(port, 0);
	}

}
