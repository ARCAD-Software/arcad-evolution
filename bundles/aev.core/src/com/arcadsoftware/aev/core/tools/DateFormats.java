package com.arcadsoftware.aev.core.tools;

import java.text.Format;
import java.text.ParseException;
import java.util.Date;

import org.apache.commons.lang3.time.FastDateFormat;

public class DateFormats {

	public static final FastDateFormat NATIVE = FastDateFormat.getInstance("yyyyMMdd");
	public static final FastDateFormat HMS = FastDateFormat.getInstance("HHmmss");
	public static final FastDateFormat TIMESTAMP = FastDateFormat.getInstance("yyyy-MM-dd-HH.mm.ss.SSS");
	public static final FastDateFormat DATETIME = FastDateFormat.getInstance("yyyy-MM-dd HH.mm.ss");
	
	public static Date nativeParse(String source) throws ParseException {
		return NATIVE.parse(source);
	}
	public static String nativeFormat(Date date) {
		return NATIVE.format(date);
	}

	public static Date timestampParse(String source) throws ParseException {
		return TIMESTAMP.parse(source);
	}
	public static String timestampFormat(Date date) {
		return TIMESTAMP.format(date);
	}

	
	public static Date hmsParse(String source) throws ParseException {
		return HMS.parse(source);
	}
	public static String hmsFormat(Date date) {
		return HMS.format(date);
	}

	public static Format instance(String pattern) {
		return FastDateFormat.getInstance(pattern);
	}
}
