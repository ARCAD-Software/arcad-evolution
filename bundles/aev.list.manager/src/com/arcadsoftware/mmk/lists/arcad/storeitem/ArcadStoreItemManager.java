package com.arcadsoftware.mmk.lists.arcad.storeitem;

import com.arcadsoftware.mmk.lists.AbstractArcadList;
import com.arcadsoftware.mmk.lists.metadata.AbstractStoreItemManager;
import com.arcadsoftware.mmk.lists.metadata.ListMetaDatas;
import com.arcadsoftware.mmk.lists.metadata.StoreItem;

public class ArcadStoreItemManager extends AbstractStoreItemManager {

	public ArcadStoreItemManager(final AbstractArcadList list) {
		super(list);
	}

	@Override
	public void createMetadata(final ListMetaDatas metadatas) {

	}

	@Override
	public StoreItem toStoreItem(final Object object) {
		return null;
	}

}
