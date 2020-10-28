package com.hvadoda1.dht.chord.util;

import java.util.HashMap;
import java.util.Map;

import com.hvadoda1.dht.chord.Config;
import com.hvadoda1.dht.chord.rpc.connect.IChordNode;

public class CommonUtils {
	public static Map<String, String> parseArgsMap(String[] args) {
		Map<String, String> map = new HashMap<String, String>();
		String[] argSplit;
		String key, val;
		StringBuilder sb;
		int i;
		for (String arg : args) {
			argSplit = arg.split("=");
			if (argSplit.length > 1) {
				sb = new StringBuilder();
				for (i = 1; i < argSplit.length; i++)
					sb.append(argSplit[i]).append('=');
				if (sb.length() > 0)
					sb.deleteCharAt(sb.length() - 1);
				val = sb.toString();
			} else
				continue;
//				val = argSplit[0];
			key = argSplit[0];

			map.put(key, val);
		}

		return map;
	}

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