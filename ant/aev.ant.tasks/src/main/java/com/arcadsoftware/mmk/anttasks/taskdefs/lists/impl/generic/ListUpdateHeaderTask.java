package com.arcadsoftware.mmk.anttasks.taskdefs.lists.impl.generic;

import com.arcadsoftware.mmk.anttasks.taskdefs.lists.AbstractXmlFileListTask;

public class ListUpdateHeaderTask extends AbstractXmlFileListTask {
	private String comment = null;
	private String description = null;

	@Override
	public void processExecution() {
		updateHeaderInfo(description, comment);
	}

	/**
	 * @param comment
	 *            the comment to set
	 */
	public void setComment(final String comment) {
		this.comment = comment;
	}

	/**
	 * @param description
	 *            the description to set
	 */
	@Override
	public void setDescription(final String description) {
		this.description = description;
	}

}
