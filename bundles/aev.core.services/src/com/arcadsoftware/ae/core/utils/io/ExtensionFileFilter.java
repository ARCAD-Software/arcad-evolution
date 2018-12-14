/*
 * Créé le Jul 7, 2009
 *
 * TODO Pour changer le modèle de ce fichier généré, allez à :
 * Fenêtre - Préférences - Java - Style de code - Modèles de code
 */
package com.arcadsoftware.ae.core.utils.io;

import java.io.File;
import java.util.Enumeration;
import java.util.Hashtable;

public class ExtensionFileFilter {
	private boolean allowDirectories;
	private Hashtable extensionsTable = new Hashtable();
	private boolean allowAll = false;

	public ExtensionFileFilter(boolean allowDirectories) {
		this.allowDirectories = allowDirectories;
	}

	public ExtensionFileFilter() {
		this(true);
	}

	public void addExtension(String extension,boolean caseInsensitive) {
		if (caseInsensitive) {
			extension = extension.toLowerCase();
	    }
	    if (!extensionsTable.containsKey(extension)) {
	    	extensionsTable.put(extension, new Boolean(caseInsensitive));
	    	if (extension.equals("*") || extension.equals("*.*") || extension.equals(".*")) {
	    		allowAll = true;
	    	}
	    }
	}

	public boolean accept(File file) {
	    if (file.isDirectory()) {
	    	return(allowDirectories);
	    }
	    if (allowAll) {
	    	return(true);
	    }
	    String name = file.getName();
	    int dotIndex = name.lastIndexOf('.');
	    if ((dotIndex == -1) || (dotIndex == name.length() - 1)) {
	    	return false;
	    }
	    //Recherche avaec matchinf exact
	    String extension = name.substring(dotIndex + 1);
	    if (extensionsTable.containsKey(extension)) {
	    	return(true);
	    }
	    //Recherche en ignorant le caseSensitive
	    Enumeration keys = extensionsTable.keys();
	    while(keys.hasMoreElements()) {
	    	String possibleExtension = (String)keys.nextElement();
	    	Boolean caseFlag =
	    		(Boolean)extensionsTable.get(possibleExtension);
	    	if ((caseFlag != null) &&
	            (caseFlag.equals(Boolean.FALSE)) &&
	            (possibleExtension.equalsIgnoreCase(extension))) {
	    		return true;
	    	}
	    }
	    return false;
	  }

}
