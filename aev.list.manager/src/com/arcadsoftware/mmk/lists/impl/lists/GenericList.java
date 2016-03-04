package com.arcadsoftware.mmk.lists.impl.lists;

import com.arcadsoftware.mmk.lists.AbstractList;
import com.arcadsoftware.mmk.lists.AbstractXmlList;
import com.arcadsoftware.mmk.lists.metadata.AbstractStoreItemManager;

public class GenericList extends AbstractXmlList {
	
	
	public GenericList(){
		super();
	}

	@Override
	public AbstractList createCloneList() {
		return new GenericList();
	}

	@Override
	public AbstractStoreItemManager createStoreItemManager(AbstractList list) {
		return null;
	}

}
