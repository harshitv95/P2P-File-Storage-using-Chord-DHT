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
		String s = getProperty("KEYSTORE_MANAGER");
		return s == null || s.trim().isEmpty() ? null : s;
	}

	public static String getKeyStoreType() {
		String s = getProperty("KEYSTORE_TYPE");
		return s == null || s.trim().isEmpty() ? null : s;
	}

	public static String getTrustStorePath() {
		return getProperty("TRUSTSTORE_PATH");
	}

	public static String getTrustStorePassword() {
		return getProperty("TRUSTSTORE_PASSWORD");
	}

	public static String getTrustStoreManager() {
		String s = getProperty("TRUSTSTORE_MANAGER");
		return s == null || s.trim().isEmpty() ? null : s;
	}

	public static String getTrustStoreType() {
		String s = getProperty("TRUSTSTORE_TYPE");
		return s == null || s.trim().isEmpty() ? null : s;
	}

}
