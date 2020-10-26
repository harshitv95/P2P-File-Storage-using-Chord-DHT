package com.hvadoda1.dht.chord;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public enum ServerIDAlgorithm {
	SHA256("SHA-256") {
		@Override
		public String generateId(String key) {
			MessageDigest msg;
			try {
				msg = MessageDigest.getInstance(this.getAlgorithmName());
				return new String(msg.digest((key).getBytes(StandardCharsets.UTF_8)));
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
				return null;
			}
		}
	};

	private final String generatorAlgorithm;

	ServerIDAlgorithm(String generatorAlgorithm) {
		this.generatorAlgorithm = generatorAlgorithm;
	}

	String getAlgorithmName() {
		return this.generatorAlgorithm;
	}

	public abstract String generateId(String key);
}
