package com.hvadoda1.dht.chord.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateTimeUtils {
	private static final SimpleDateFormat logFileNameFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");

	public static String getLogFileNameDateString() {
		return logFileNameFormat.format(new Date());
	}

}
