package com.hvadoda1.dht.chord.rpc.thrift.connect;

import com.hvadoda1.dht.chord.Config;

public class ThriftConfig extends Config {
	private String keyStorePath = "/home/cs557-inst/thrift-0.13.0/lib/java/test/.truststore";

	public String getKeyStorePath() {
		return keyStorePath;
	}

	public void setKeyStorePath(String keyStorePath) {
		this.keyStorePath = keyStorePath;
	}
	
}
