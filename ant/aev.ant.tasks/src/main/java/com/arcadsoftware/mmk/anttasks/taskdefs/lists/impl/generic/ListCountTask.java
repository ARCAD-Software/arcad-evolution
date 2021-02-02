package com.arcadsoftware.mmk.anttasks.taskdefs.lists.impl.generic;

import com.arcadsoftware.mmk.anttasks.taskdefs.lists.AbstractXmlFileListTask;

public class ListCountTask extends AbstractXmlFileListTask {
	protected String countProperty = "";
	protected String countQuery = null;

	@Override
	public void processExecution() {
		final int count = list.count(countQuery);
		if (countProperty != null && !countProperty.equals("")) {
			getProject().setNewProperty(countProperty, String.valueOf(count));
		}
	}

	/**
	 * @param countProperty
	 *            the countProperty to set
	 */
	public void setCountProperty(final String countProperty) {
		this.countProperty = countProperty;
	}

	/**
	 * @param countQuery
	 *            the countQuery to set
	 */
	public void setCountQuery(final String countQuery) {
		this.countQuery = countQuery;
	}
}
