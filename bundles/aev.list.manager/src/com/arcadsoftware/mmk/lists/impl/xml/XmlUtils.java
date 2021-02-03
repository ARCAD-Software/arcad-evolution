package com.arcadsoftware.mmk.lists.impl.xml;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.arcadsoftware.mmk.lists.AbstractArcadList;
import com.arcadsoftware.mmk.lists.IXmlLists;

public class XmlUtils {
	private XmlUtils() {
		
	}
	
	public static boolean changeXmlFile(final IXmlLists list, final String filename) {
		final File f = new File(list.getXmlFileName());
		try {
			Files.deleteIfExists(f.toPath());
			final File nf = new File(filename);
			return nf.renameTo(f);
		}
		catch(IOException e) {
			return false;
		}
	}

	public static IXmlLists createTempList(final AbstractArcadList listToClone) {
		final IXmlLists clone = (IXmlLists) listToClone.cloneList();
		try {
			final File f = File.createTempFile("tmp", null);
			final String name = f.getAbsolutePath();
			Files.delete(f.toPath());
			clone.setXmlFileName(name);
			return clone;
		} catch (final IOException e) {
			listToClone.logError(AbstractArcadList.MODULE_NAME, e);
			return null;
		}
	}

	public static Date getDate(final String stringDate) {
		try {
			return new SimpleDateFormat("yyyyMMdd-HHmmssSSS").parse(stringDate);
		} catch (final ParseException e) {
			return new Date();
		}
	}
}
