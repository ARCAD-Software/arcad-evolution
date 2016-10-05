package com.arcadsoftware.mmk.lists.impl.xml;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.arcadsoftware.mmk.lists.AbstractList;
import com.arcadsoftware.mmk.lists.IXmlLists;

public class XmlUtils {
	private static SimpleDateFormat sd = new SimpleDateFormat("yyyyMMdd-HHmmssSSS");
	
	public static Date getDate(String stringDate) {
		try {
			Date d = sd.parse(stringDate);
			return d;
		} catch (ParseException e) {
			return new Date();
		}
	}		
	
	public static IXmlLists createTempList(AbstractList listToClone) {
		IXmlLists clone = (IXmlLists)listToClone.cloneList();
		try {
			File f = File.createTempFile("tmp",null);
			String name = f.getAbsolutePath();
			f.delete();
			clone.setXmlFileName(name);
			return clone;
		} catch (IOException e) {
			listToClone.logError(AbstractList.MODULE_NAME, e);
			return null;
		}
	}
	
	public static boolean changeXmlFile(IXmlLists list, String filename) {
		File f = new File(list.getXmlFileName());
		if (f.delete()){
			File nf = new File(filename);
			return nf.renameTo(f);			
		}		
		return false;
	}
}
