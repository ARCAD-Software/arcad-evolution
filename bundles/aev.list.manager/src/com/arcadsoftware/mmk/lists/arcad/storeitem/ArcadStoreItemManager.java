package com.arcadsoftware.mmk.lists.arcad.storeitem;

import com.arcadsoftware.mmk.lists.AbstractList;
import com.arcadsoftware.mmk.lists.metadata.AbstractStoreItemManager;
import com.arcadsoftware.mmk.lists.metadata.ListMetaDatas;
import com.arcadsoftware.mmk.lists.metadata.StoreItem;

public class ArcadStoreItemManager extends AbstractStoreItemManager {
	
	public ArcadStoreItemManager(AbstractList list) {
		super(list);
	}
	@Override
	public void createMetadata(ListMetaDatas metadatas) {

	}

	@Override
	public StoreItem toStoreItem(Object object) {
		return null;
	}

}
