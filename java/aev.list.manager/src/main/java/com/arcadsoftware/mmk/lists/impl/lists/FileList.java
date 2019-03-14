package com.arcadsoftware.mmk.lists.impl.lists;



import com.arcadsoftware.mmk.lists.AbstractList;
import com.arcadsoftware.mmk.lists.AbstractXmlList;
import com.arcadsoftware.mmk.lists.impl.storeitem.FileStoreItemManager;
import com.arcadsoftware.mmk.lists.metadata.AbstractStoreItemManager;

public class FileList extends AbstractXmlList {
	
	public FileList() {
		super();
	}

	/* (non-Javadoc)
	 * @see com.arcadsoftware.mmk.lists.AbstractList#getStoreItemTransformer()
	 */
	@Override
	public AbstractStoreItemManager createStoreItemManager(AbstractList list) {
		return  new FileStoreItemManager(list);
	}

	public AbstractList createCloneList() {
		return new FileList();
	}
}
