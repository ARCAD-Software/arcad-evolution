package com.arcadsoftware.mmk.anttasks.taskdefs.lists.impl.generic;

import com.arcadsoftware.mmk.anttasks.taskdefs.lists.AbstractXmlFileListCountedTask;

public class ListRemoveDuplicateTask 
extends AbstractXmlFileListCountedTask {
	
	protected String orderClause = null;
	
	@Override
	public int processExecutionWithCount() {
		return list.removeDuplicate(orderClause);
	}

	/**
	 * @param orderQuery the orderQuery to set
	 */
	public void setOrderClause(String orderClause) {
		this.orderClause = orderClause;
	}

}
