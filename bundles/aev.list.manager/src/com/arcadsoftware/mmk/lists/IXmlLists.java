package com.arcadsoftware.mmk.lists;

import com.arcadsoftware.mmk.lists.impl.xml.XmlCashManager;

public interface IXmlLists {

	XmlCashManager getCashManager();

	AbstractArcadList getList();

	/**
	 * Renvoit le nom du fichier xml de la structure sous-jacente.
	 * 
	 * @return the xmlFileName String :
	 */
	String getXmlFileName();

	/**
	 * Fixe le nom du fichier fichier xml de la structure sous-jacente.
	 * 
	 * @param xmlfileName
	 *            String : nom du fichier
	 */
	void setXmlFileName(String xmlfileName);

}
