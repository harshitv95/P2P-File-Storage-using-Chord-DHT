package com.hvadoda1.dht.chord.util;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Objects;

public class FileUtils {
	public static String readFile(File file) throws IOException {
		StringBuilder sb = new StringBuilder();
		String line;
		try (BufferedReader br = new BufferedReader(new FileReader(file));) {
			while ((line = br.readLine()) != null)
				sb.append(line).append(System.lineSeparator());
		}
		return sb.toString();
	}

	public static void writeFile(File file, String content) throws IOException {
		Objects.requireNonNull(file, "Cannot write to File as File was null");
		if (!file.exists())
			file.createNewFile();
		byte buf[] = new byte[4096];
		try (OutputStream fw = new FileOutputStream(file);
				ByteArrayInputStream bis = new ByteArrayInputStream(content.getBytes());) {
			while (bis.read(buf) > 0) {
				fw.write(buf);
				fw.flush();
			}
		}
	}
}
