package com.hvadoda1.dht.chord;

public class Config {
	private final static Config instance = new Config();

	private ServerIDAlgorithm serverIDAlgorithm = ServerIDAlgorithm.SHA256;

	private Config() {
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
