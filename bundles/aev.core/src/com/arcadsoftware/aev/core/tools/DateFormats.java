/*******************************************************************************
 * Copyright (c) 2025 ARCAD Software.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     ARCAD Software - initial API and implementation
 *******************************************************************************/
package com.arcadsoftware.aev.core.tools;

import java.text.Format;
import java.text.ParseException;
import java.util.Date;

import org.apache.commons.lang3.time.FastDateFormat;

public class DateFormats {

	public static final FastDateFormat DATETIME = FastDateFormat.getInstance("yyyy-MM-dd HH.mm.ss");
	public static final FastDateFormat HMS = FastDateFormat.getInstance("HHmmss");
	public static final FastDateFormat NATIVE = FastDateFormat.getInstance("yyyyMMdd");
	public static final FastDateFormat TIMESTAMP = FastDateFormat.getInstance("yyyy-MM-dd-HH.mm.ss.SSS");

	public static String hmsFormat(final Date date) {
		return HMS.format(date);
	}

	public static Date hmsParse(final String source) throws ParseException {
		return HMS.parse(source);
	}

	public static Format instance(final String pattern) {
		return FastDateFormat.getInstance(pattern);
	}

	public static String nativeFormat(final Date date) {
		return NATIVE.format(date);
	}

	public static Date nativeParse(final String source) throws ParseException {
		return NATIVE.parse(source);
	}

	public static String timestampFormat(final Date date) {
		return TIMESTAMP.format(date);
	}

	public static Date timestampParse(String source) throws ParseException {
		// <JIRA number="QA-213" version="11.00.06" date="May 9, 2019" type="Bug" user="ACL">
		// The Pre-Java8 ("standard") Date parsing routines evidently can't handle nano-seconds (anything beyond
		// TIMESTAMP(3) precision)
		if (source.length() > 23) {
			source = source.substring(0, 23);
		}
		// </JIRA>
		return TIMESTAMP.parse(source);
	}
}
