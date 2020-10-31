package com.hvadoda1.dht.chord.util;

import com.hvadoda1.dht.chord.Config;

public class StringUtils {
//	public static final String sha256Zero = "0000000000000000000000000000000000000000000000000000000000000000";

	public static String keyToID(String key) {
		return Config.getInstance().getServerIDAlgorithm().generateId(key);
	}

	public static String bytesToHex(byte[] hash) {
		StringBuilder hexString = new StringBuilder(2 * hash.length);
		for (int i = 0; i < hash.length; i++) {
			String hex = Integer.toHexString(0xff & hash[i]);
			if (hex.length() == 1) {
				hexString.append('0');
			}
			hexString.append(hex);
		}
		return hexString.toString();
	}

	public static class Comparing {
		public static boolean lt(String low, String high) {
			return low.compareTo(high) < 0;
		}

		public static boolean lteq(String low, String high) {
			return low.compareTo(high) <= 0;
		}

		public static boolean gt(String high, String low) {
			return high.compareTo(low) > 0;
		}

		public static boolean gteq(String high, String low) {
			return high.compareTo(low) >= 0;
		}

		public static boolean eq(String high, String low) {
			return low.equals(high);
		}
	}
}
