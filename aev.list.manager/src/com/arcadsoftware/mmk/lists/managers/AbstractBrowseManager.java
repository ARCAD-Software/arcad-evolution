package com.arcadsoftware.mmk.lists.managers;

import com.arcadsoftware.mmk.lists.AbstractList;



public abstract class AbstractBrowseManager  extends AbstractLoggedObject{
	protected AbstractList list;
	
	public AbstractBrowseManager(AbstractList list) {
		super();
		this.list = list;
		setLogger(list.getLogger());
	}

	public abstract void browse();	

	
}
