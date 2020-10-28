package com.arcadsoftware.mmk.lists.impl.xml;


import java.text.SimpleDateFormat;

import com.arcadsoftware.ae.core.exceptions.ArcadException;
import com.arcadsoftware.mmk.lists.IXmlLists;
import com.arcadsoftware.mmk.lists.managers.AbstractStoreManager;

public class XmlStoreManagerOld extends AbstractStoreManager {
	protected final SimpleDateFormat sd = new SimpleDateFormat("yyyyMMdd-HHmmssSSS");
	protected final IXmlLists xmllist;
	
	public XmlStoreManagerOld(IXmlLists list) {
		super(list.getList());
		this.xmllist = list;
	}

	@Override
	public boolean saveItem()  throws ArcadException {	
		return true;
	}

}

