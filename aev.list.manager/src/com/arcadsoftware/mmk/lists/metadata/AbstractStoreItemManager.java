package com.arcadsoftware.mmk.lists.metadata;

import com.arcadsoftware.mmk.lists.AbstractList;
import com.arcadsoftware.mmk.lists.managers.AbstractLoggedObject;

public abstract class AbstractStoreItemManager extends AbstractLoggedObject{
	
	protected AbstractList list;
	
	public AbstractStoreItemManager() {
		super();
	}	
	public AbstractStoreItemManager(AbstractList list) {
		this();
		setList(list);
	}
	
	public abstract StoreItem toStoreItem(Object object);
	public abstract void createMetadata(ListMetaDatas metadatas);
	/**
	 * @param list the list to set
	 */
	public void setList(AbstractList list) {
		this.list = list;
		setLogger(list.getLogger());
	}
	
}
