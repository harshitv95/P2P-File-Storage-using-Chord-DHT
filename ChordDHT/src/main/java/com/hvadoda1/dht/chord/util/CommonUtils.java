package com.hvadoda1.dht.chord.util;

import com.hvadoda1.dht.chord.Config;
import com.hvadoda1.dht.chord.rpc.connect.IChordNode;

public class CommonUtils {
	public static String serverID(String host, int port) {
		return keyToID(host + ":" + port);
	}

	public static String keyToID(String key) {
		return Config.getInstance().getServerIDAlgorithm().generateId(key);
	}

	public static String nodeAddress(IChordNode node) {
		return node.getIp() + ":" + node.getPort();
	}
}