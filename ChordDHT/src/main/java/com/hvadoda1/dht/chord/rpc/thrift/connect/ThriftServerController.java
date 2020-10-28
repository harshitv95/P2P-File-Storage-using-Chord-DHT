package com.hvadoda1.dht.chord.rpc.thrift.connect;

import org.apache.thrift.TException;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TSimpleServer;
import org.apache.thrift.transport.TSSLTransportFactory;
import org.apache.thrift.transport.TSSLTransportFactory.TSSLTransportParameters;
import org.apache.thrift.transport.TServerTransport;
import org.apache.thrift.transport.TTransportException;

import com.hvadoda1.dht.chord.Config;
import com.hvadoda1.dht.chord.rpc.connect.IRpcServerController;
import com.hvadoda1.dht.chord.rpc.thrift.generated.FileStore;
import com.hvadoda1.dht.chord.util.Logger;

public class ThriftServerController implements IRpcServerController<ThriftServer, TException> {

	protected final int port;
	protected final FileStore.Processor<ThriftServer> processor;

	public ThriftServerController(int port) throws TException {
		Logger.debugLow("Initializing Thrift Chord Server Controller");
		this.port = port;
		this.processor = new FileStore.Processor<>(createServerHandler());
	}

	@Override
	public void run() {
		Logger.debugLow("Running Thrift Chord Server Controller");
		try {
			TServer server = setupServer();
			server.serve();
		} catch (TTransportException e) {
			Logger.error("Could not start RPC Server", e);
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
		TSSLTransportParameters params = new TSSLTransportParameters();
		params.setKeyStore(
					ThriftConfig.getKeyStorePath(),
					ThriftConfig.getKeyStorePassword(),
					ThriftConfig.getKeyStoreManager(),
					ThriftConfig.getKeyStoreType()
				);
		return TSSLTransportFactory.getServerSocket(port, 0, null, params);
	}

}
