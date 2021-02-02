package com.arcadsoftware.mmk.lists.impl.lists;

import com.arcadsoftware.mmk.lists.AbstractArcadList;
import com.arcadsoftware.mmk.lists.AbstractXmlList;
import com.arcadsoftware.mmk.lists.impl.storeitem.FileStoreItemManager;
import com.arcadsoftware.mmk.lists.metadata.AbstractStoreItemManager;

public class FileList extends AbstractXmlList {

	public FileList() {
		super();
	}

	@Override
	public AbstractArcadList createCloneList() {
		return new FileList();
	}

	/*
	 * (non-Javadoc)
	 * @see com.arcadsoftware.mmk.lists.AbstractList#getStoreItemTransformer()
	 */
	@Override
	public AbstractStoreItemManager createStoreItemManager(final AbstractArcadList list) {
		return new FileStoreItemManager(list);
	}
}
