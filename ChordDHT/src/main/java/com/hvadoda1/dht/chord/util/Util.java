package com.hvadoda1.dht.chord.util;

import com.hvadoda1.dht.chord.Config;

public class Util {
	public static String serverId(String host, int port) {
		return Config.getInstance().getServerIDAlgorithm().generateId(host, port);
	}
}