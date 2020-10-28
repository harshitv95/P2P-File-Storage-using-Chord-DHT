package com.hvadoda1.dht.chord.rpc.thrift.connect;

import java.util.Map;

import com.hvadoda1.dht.chord.Config;

public class ThriftConfig extends Config {

	public ThriftConfig(Map<String, String> argsMap) {
		super(argsMap);
	}

	public static String getKeyStorePath() {
		return getProperty("KEYSTORE_PATH");
	}

	public static String getKeyStorePassword() {
		return getProperty("KEYSTORE_PASSWORD");
	}

	public static String getKeyStoreManager() {
		return getProperty("KEYSTORE_MANAGER");
	}

	public static String getKeyStoreType() {
		return getProperty("KEYSTORE_TYPE");
	}

	public static String getTrustStorePath() {
		return getProperty("TRUSTSTORE_PATH");
	}

	public static String getTrustStorePassword() {
		return getProperty("TRUSTSTORE_PASSWORD");
	}

	public static String getTrustStoreManager() {
		return getProperty("TRUSTSTORE_MANAGER");
	}

	public static String getTrustStoreType() {
		return getProperty("TRUSTSTORE_TYPE");
	}

}
