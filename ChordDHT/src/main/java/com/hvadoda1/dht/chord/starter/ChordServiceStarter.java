package com.hvadoda1.dht.chord.starter;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import org.apache.thrift.TException;

import com.hvadoda1.dht.chord.Config;
import com.hvadoda1.dht.chord.rpc.connect.IRpcServer;
import com.hvadoda1.dht.chord.rpc.connect.IRpcServerController;
import com.hvadoda1.dht.chord.rpc.thrift.connect.ThriftConfig;
import com.hvadoda1.dht.chord.rpc.thrift.connect.ThriftServer;
import com.hvadoda1.dht.chord.rpc.thrift.connect.ThriftServerController;
import com.hvadoda1.dht.chord.util.CommonUtils;
import com.hvadoda1.dht.chord.util.DateTimeUtils;
import com.hvadoda1.dht.chord.util.Logger;
import com.hvadoda1.dht.chord.util.Logger.Level;

public abstract class ChordServiceStarter<Controller extends IRpcServerController<Server, Exc>, Server extends IRpcServer<?, ?, ?, ?>, Exc extends Exception> {

	private final static List<String> requiredParams = new ArrayList<>() {
		private static final long serialVersionUID = 1L;
		{
			add("port");
		}

	};

	public static void main(String[] args) {
		Map<String, String> argMap = CommonUtils.parseArgsMap(args);
		String missingParam;
		if ((missingParam = requiredParams.stream().filter(param -> !argMap.containsKey(param)).findAny()
				.orElse(null)) != null)
			throw new RuntimeException("Required argument [" + missingParam + "] was not provided");

		Level logLevel = Level.from(Integer.parseInt(argMap.getOrDefault("level", "3")));
		String logFilename = "logs" + File.separator + "log_" + (DateTimeUtils.getLogFileNameDateString()) + ".txt";

		try {
			Logger log = new Logger(logLevel, logFilename, InetAddress.getLocalHost().getHostAddress(), false);
			Logger.debugHigh("Command Line Args", argMap);
			try {
				new ThriftChordServiceStarter(argMap).start(() -> {
					// Equivalent to finally, but for threads
					log.close();
					return log;
				});
			} catch (TException e) {
				e.printStackTrace();
				Logger.error("Exception while executing Chord server", e);
			}

		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	protected final Controller controller;

	public ChordServiceStarter(Map<String, String> argsMap) throws Exc {
		initConfig(argsMap);
		controller = initController(Integer.parseInt(argsMap.get("port")));
	}

	public void start() {
		new Thread(controller).start();
	}

	public <T> T start(Supplier<T> serviceThreadEndCallback) {
		Thread t = new Thread(controller);
		t.start();
		try {
			t.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return serviceThreadEndCallback.get();
	}

	protected abstract Controller initController(int port) throws Exc;

	protected Config initConfig(Map<String, String> argsMap) {
		return new Config(argsMap);
	}

}

class ThriftChordServiceStarter extends ChordServiceStarter<ThriftServerController, ThriftServer, TException> {

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
