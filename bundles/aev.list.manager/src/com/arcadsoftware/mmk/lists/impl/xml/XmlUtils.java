package com.arcadsoftware.mmk.lists.impl.xml;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.arcadsoftware.mmk.lists.AbstractArcadList;
import com.arcadsoftware.mmk.lists.IXmlLists;

public class XmlUtils {
	private static SimpleDateFormat sd = new SimpleDateFormat("yyyyMMdd-HHmmssSSS");

	public static boolean changeXmlFile(final IXmlLists list, final String filename) {
		final File f = new File(list.getXmlFileName());
		if (f.delete()) {
			final File nf = new File(filename);
			return nf.renameTo(f);
		}
		return false;
	}

	public static IXmlLists createTempList(final AbstractArcadList listToClone) {
		final IXmlLists clone = (IXmlLists) listToClone.cloneList();
		try {
			final File f = File.createTempFile("tmp", null);
			final String name = f.getAbsolutePath();
			f.delete();
			clone.setXmlFileName(name);
			return clone;
		} catch (final IOException e) {
			listToClone.logError(AbstractArcadList.MODULE_NAME, e);
			return null;
		}
	}

	public static Date getDate(final String stringDate) {
		try {
			final Date d = sd.parse(stringDate);
			return d;
		} catch (final ParseException e) {
			return new Date();
		}
	}
}
