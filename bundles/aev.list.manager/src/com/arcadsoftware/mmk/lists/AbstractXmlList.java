package com.arcadsoftware.mmk.lists;

import com.arcadsoftware.mmk.lists.impl.xml.XmlCashManager;
import com.arcadsoftware.mmk.lists.impl.xml.XmlContentManager;
import com.arcadsoftware.mmk.lists.impl.xml.XmlStoreManager;
import com.arcadsoftware.mmk.lists.managers.AbstractContentManager;
import com.arcadsoftware.mmk.lists.managers.AbstractStoreManager;

public abstract class AbstractXmlList extends AbstractArcadList
		implements IXmlLists {

	private XmlCashManager cashManager = null;
	private String xmlfileName;

	public AbstractXmlList() {
		super();

	}

	@Override
	public AbstractContentManager createContentManager() {
		return new XmlContentManager(this);
	}

	@Override
	public AbstractStoreManager createStoreManager() {
		if (xmlfileName != null) {
			return new XmlStoreManager(this);
		}
		return null;
	}

	public IXmlLists duplicate() {
		return null;
	}

	@Override
	public XmlCashManager getCashManager() {
		if (cashManager == null) {
			cashManager = new XmlCashManager(this);
		}
		return cashManager;
	}

	@Override
	public AbstractArcadList getList() {
		return this;
	}

	@Override
	public String getXmlFileName() {
		return xmlfileName;
	}

	@Override
	public void setXmlFileName(final String xmlfileName) {
		this.xmlfileName = xmlfileName;
	}

}
