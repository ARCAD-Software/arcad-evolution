package com.arcadsoftware.mmk.lists.managers;

import com.arcadsoftware.mmk.lists.AbstractList;
import com.arcadsoftware.mmk.lists.IContentAction;

public abstract class AbstractContentManager  extends AbstractLoggedObject
implements IContentAction {
	protected AbstractList list;
	protected AbstractCashManager cashManager;
	
	public AbstractContentManager(AbstractList list) {
		super();
		this.list = list;
		setLogger(list.getLogger());
	}

	/**
	 * Renvoit 
	 * @return the cashManager AbstractCashManager : 
	 */
	public AbstractCashManager getCashManager() {
		return cashManager;
	}

		
}
