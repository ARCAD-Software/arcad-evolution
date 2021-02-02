package com.arcadsoftware.mmk.lists.managers;

import com.arcadsoftware.mmk.lists.AbstractArcadList;
import com.arcadsoftware.mmk.lists.IContentAction;

public abstract class AbstractContentManager extends AbstractLoggedObject
		implements IContentAction {
	protected AbstractCashManager cashManager;
	protected AbstractArcadList list;

	public AbstractContentManager(final AbstractArcadList list) {
		super();
		this.list = list;
		setLogger(list.getLogger());
	}

	/**
	 * Renvoit
	 * 
	 * @return the cashManager AbstractCashManager :
	 */
	public AbstractCashManager getCashManager() {
		return cashManager;
	}

}
