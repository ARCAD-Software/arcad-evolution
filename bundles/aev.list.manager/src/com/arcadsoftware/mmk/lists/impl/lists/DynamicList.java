package com.arcadsoftware.mmk.lists.impl.lists;

import com.arcadsoftware.mmk.lists.AbstractArcadList;
import com.arcadsoftware.mmk.lists.AbstractXmlList;
import com.arcadsoftware.mmk.lists.ListFactory;
import com.arcadsoftware.mmk.lists.metadata.AbstractStoreItemManager;

public class DynamicList extends AbstractXmlList {

	String storeItemBeanId = null;
	AbstractStoreItemManager storeItemManager = null;

	public DynamicList(final String storeItemBeanId) {
		super();
	}

	@Override
	public AbstractArcadList createCloneList() {
		return new DynamicList(storeItemBeanId);
	}

	@Override
	public AbstractStoreItemManager createStoreItemManager(final AbstractArcadList list) {
		if (storeItemManager == null) {
			storeItemManager = (AbstractStoreItemManager) ListFactory.getInstance().getBean(storeItemBeanId);
			storeItemManager.setList(list);
		}
		return storeItemManager;
	}
}
