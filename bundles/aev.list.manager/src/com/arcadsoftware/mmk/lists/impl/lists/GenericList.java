package com.arcadsoftware.mmk.lists.impl.lists;

import com.arcadsoftware.mmk.lists.AbstractArcadList;
import com.arcadsoftware.mmk.lists.AbstractXmlList;
import com.arcadsoftware.mmk.lists.metadata.AbstractStoreItemManager;

public class GenericList extends AbstractXmlList {

	public GenericList() {
		super();
	}

	@Override
	public AbstractArcadList createCloneList() {
		return new GenericList();
	}

	@Override
	public AbstractStoreItemManager createStoreItemManager(final AbstractArcadList list) {
		return null;
	}

}
