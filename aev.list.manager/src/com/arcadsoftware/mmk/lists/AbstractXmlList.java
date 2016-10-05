package com.arcadsoftware.mmk.lists;


import com.arcadsoftware.mmk.lists.impl.xml.XmlCashManager;
import com.arcadsoftware.mmk.lists.impl.xml.XmlContentManager;
import com.arcadsoftware.mmk.lists.impl.xml.XmlStoreManager;
import com.arcadsoftware.mmk.lists.managers.AbstractContentManager;
import com.arcadsoftware.mmk.lists.managers.AbstractStoreManager;

public abstract class AbstractXmlList extends AbstractList 
implements IXmlLists {

	private String xmlfileName;
	private XmlCashManager cashManager=null;
	

	public AbstractXmlList() {
		super();
		
	}	

	@Override
	public AbstractStoreManager createStoreManager() {
		if (xmlfileName!=null) 
			return new XmlStoreManager(this);
		return null;		
	}

	@Override
	public AbstractContentManager createContentManager() {
		return new XmlContentManager(this);
	}	
	
	public void setXmlFileName(String xmlfileName) {
		this.xmlfileName = xmlfileName;
	}

	public String getXmlFileName() {
		return xmlfileName;
	}

	public AbstractList getList() {
		return this;
	}

	public XmlCashManager getCashManager() {
		if (cashManager==null) {
			cashManager = new XmlCashManager(this);
		}
		return cashManager;
	}

	public IXmlLists duplicate() {
		return null;
	}

	
	
}
