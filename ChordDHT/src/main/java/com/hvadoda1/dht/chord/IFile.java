package com.hvadoda1.dht.chord;

public interface IFile {
	<M extends IFileMeta> M getMeta();

	String getContent();

	<F extends IFile> F setContent(String content);
}
