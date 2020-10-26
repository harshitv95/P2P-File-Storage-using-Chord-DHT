package com.hvadoda1.dht.chord;

public class Config {
	public final static Config instance = new Config();
	public final static String USR_DIR = System.getProperty("user.dir");

	private ServerIDAlgorithm serverIDAlgorithm = ServerIDAlgorithm.SHA256;

	protected Config() {
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
