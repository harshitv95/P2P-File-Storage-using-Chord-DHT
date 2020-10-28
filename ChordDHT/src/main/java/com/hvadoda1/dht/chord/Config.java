package com.hvadoda1.dht.chord;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

import com.hvadoda1.dht.chord.util.Logger;

public class Config {
	protected static Config instance;
	public final static String USR_DIR = System.getProperty("user.dir");

	protected ServerIDAlgorithm serverIDAlgorithm = ServerIDAlgorithm.SHA256;

	protected Map<String, String> args;

	protected Properties props = new Properties();

	public Config() {
		if (instance != null)
			throw new RuntimeException("Config can only be initialized once");
		instance = this;
	}

	public Config(Map<String, String> args) {
		this();
		setArgs(args);
	}

	public void setArgs(Map<String, String> args) {
		this.args = args;
		if (args.containsKey("properties"))
			loadProperties(args.get("properties"));
		else {
			InputStream stream = getClass().getResourceAsStream("/chord.properties");
			try {
				loadProperties(stream);
			} catch (IOException e) {
				Logger.error("Failed to find default properties file", e);
			}
		}
	}

	public void loadProperties(String propertiesFilename) {
		File propertiesFile = new File(propertiesFilename);
		if (!propertiesFile.exists() || !propertiesFile.isFile() || !propertiesFile.canRead())
			throw new RuntimeException("Invalid Properties file or cannot read [" + args.get("properties") + "]");
		try {
			loadProperties(new FileInputStream(propertiesFile));
		} catch (IOException e) {
			Logger.error("Failed to read properties from file [" + propertiesFilename + "]", e);
			throw new RuntimeException("Failed to read properties from file [" + propertiesFilename + "]", e);
		}
	}

	public void loadProperties(InputStream stream) throws IOException {
		props.load(stream);
	}

	public Map<String, String> getArgs() {
		return args;
	}

	public Properties getProperties() {
		return new Properties(this.props);
	}

	public static String getProperty(String key) {
		return instance.props.getProperty(key);
	}

	public static String getPropertyOrDefault(String key, String defaultVal) {
		return instance.props.getProperty(key, defaultVal);
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
