package com.arcadsoftware.mmk.lists;

import com.arcadsoftware.mmk.lists.impl.xml.XmlCashManager;

public interface IXmlLists {

	/**
	 * Renvoit le nom du fichier xml de la structure 
	 * sous-jacente.
	 * @return the xmlFileName String : 
	 */
	public String getXmlFileName() ;

	/**
	 * Fixe le nom du fichier fichier xml de la structure 
	 * sous-jacente.
	 * @param xmlfileName String : nom du fichier
	 */
	public void setXmlFileName(String xmlfileName) ;	
	public AbstractList getList();	
	public XmlCashManager getCashManager();
	
	
}
