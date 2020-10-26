package com.hvadoda1.dht.chord;

public interface IFileMeta {
	String getFilename();

	int getVersion();

	<M extends IFileMeta> M setVersion(int version);

//	long length();
//
//	Date lastModified();
}
