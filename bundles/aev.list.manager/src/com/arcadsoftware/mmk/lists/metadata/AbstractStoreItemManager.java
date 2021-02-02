package com.arcadsoftware.mmk.lists.metadata;

import com.arcadsoftware.mmk.lists.AbstractArcadList;
import com.arcadsoftware.mmk.lists.managers.AbstractLoggedObject;

public abstract class AbstractStoreItemManager extends AbstractLoggedObject {

	protected AbstractArcadList list;

	public AbstractStoreItemManager() {
		super();
	}

	public AbstractStoreItemManager(final AbstractArcadList list) {
		this();
		setList(list);
	}

	public abstract void createMetadata(ListMetaDatas metadatas);

	/**
	 * @param list
	 *            the list to set
	 */
	public void setList(final AbstractArcadList list) {
		this.list = list;
		setLogger(list.getLogger());
	}

	public abstract StoreItem toStoreItem(Object object);

}
