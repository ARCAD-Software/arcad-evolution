package com.arcadsoftware.mmk.lists.impl.lists;

import com.arcadsoftware.mmk.lists.AbstractList;
import com.arcadsoftware.mmk.lists.AbstractXmlList;
import com.arcadsoftware.mmk.lists.ListFactory;
import com.arcadsoftware.mmk.lists.metadata.AbstractStoreItemManager;

public class DynamicList extends AbstractXmlList {

	AbstractStoreItemManager storeItemManager = null;
	String storeItemBeanId = null;
	
	public DynamicList(String storeItemBeanId) {
		super();
	}
		
	@Override
	public AbstractList createCloneList() {
		return new DynamicList(storeItemBeanId);
	}

	@Override
	public AbstractStoreItemManager createStoreItemManager(AbstractList list) {
		if (storeItemManager==null) {
			storeItemManager = (AbstractStoreItemManager)ListFactory.getInstance().getBean(storeItemBeanId);
			storeItemManager.setList(list);		
		}		
		return storeItemManager;
	}
}
