package com.arcadsoftware.mmk.lists.managers;

import com.arcadsoftware.mmk.lists.AbstractList;



public abstract class AbstractBrowseManager {
	protected AbstractList list;
	
	public AbstractBrowseManager(AbstractList list) {
		super();
		this.list = list;
	}

	public abstract void browse();	

	
}
