package com.arcadsoftware.mmk.lists.managers;

import com.arcadsoftware.mmk.lists.AbstractArcadList;

public abstract class AbstractBrowseManager extends AbstractLoggedObject {
	protected AbstractArcadList list;

	public AbstractBrowseManager(final AbstractArcadList list) {
		super();
		this.list = list;
		setLogger(list.getLogger());
	}

	public abstract void browse();

}
