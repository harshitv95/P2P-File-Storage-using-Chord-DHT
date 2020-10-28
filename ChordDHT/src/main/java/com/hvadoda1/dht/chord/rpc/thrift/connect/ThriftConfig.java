package com.hvadoda1.dht.chord.rpc.thrift.connect;

import java.util.Map;

import com.hvadoda1.dht.chord.Config;
import com.hvadoda1.dht.chord.util.Logger;

public class ThriftConfig extends Config {

	public ThriftConfig(Map<String, String> argsMap) {
		super(argsMap);
	}

	public static String getKeyStorePath() {
		String s = getProperty("KEYSTORE_PATH");
		Logger.debugLow("Using KeyStore [" + s + "]");
		return s;
	}

	public static String getKeyStorePassword() {
		String s = getProperty("KEYSTORE_PASSWORD");
		Logger.debugLow("Using KeyStore Password [" + s + "]");
		return s;
	}

	public static String getKeyStoreManager() {
		String s = getProperty("KEYSTORE_MANAGER");
		Logger.debugLow("Using KeyStore Manager [" + s + "]");
		return s == null || s.trim().isEmpty() ? null : s;
	}

	public static String getKeyStoreType() {
		String s = getProperty("KEYSTORE_TYPE");
		Logger.debugLow("Using KeyStore Type [" + s + "]");
		return s == null || s.trim().isEmpty() ? null : s;
	}

	public static String getTrustStorePath() {
		String s = getProperty("TRUSTSTORE_PATH");
		Logger.debugLow("Using TrustStore [" + s + "]");
		return s;
	}

	public static String getTrustStorePassword() {
		String s = getProperty("TRUSTSTORE_PASSWORD");
		Logger.debugLow("Using TrustStore Type [" + s + "]");
		return s;
	}

	public static String getTrustStoreManager() {
		String s = getProperty("TRUSTSTORE_MANAGER");
		Logger.debugLow("Using TrustStore Manager [" + s + "]");
		return s == null || s.trim().isEmpty() ? null : s;
	}

	public static String getTrustStoreType() {
		String s = getProperty("TRUSTSTORE_TYPE");
		Logger.debugLow("Using TrustStore Type [" + s + "]");
		return s == null || s.trim().isEmpty() ? null : s;
	}

}
