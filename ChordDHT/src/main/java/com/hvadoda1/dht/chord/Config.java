package com.hvadoda1.dht.chord;

public class Config {
	public static Config instance;
	public final static String USR_DIR = System.getProperty("user.dir");

	private ServerIDAlgorithm serverIDAlgorithm = ServerIDAlgorithm.SHA256;

	public Config() {
		if (instance != null)
			throw new RuntimeException("Config can only be initialized once");
		instance = this;
	}

	public static Config getInstance() {
		return instance;
	}

	public ServerIDAlgorithm getServerIDAlgorithm() {
		return serverIDAlgorithm;
	}

	public void setServerIDAlgorithm(ServerIDAlgorithm serverIDAlgorithm) {
		this.serverIDAlgorithm = serverIDAlgorithm;
	}

}
