package com.arcadsoftware.mmk.anttasks.taskdefs.lists;

public abstract class AbstractXmlFileListCountedTask extends AbstractXmlFileListTask {
	protected String countProperty = "";
	protected String processedCountProperty = "";

	/*
	 * (non-Javadoc)
	 * @see com.arcadsoftware.mmk.anttasks.taskdefs.lists.AbstractFileListTask#processExecution()
	 */
	@Override
	public void processExecution() {
		final int count = processExecutionWithCount();
		if (processedCountProperty != null && !processedCountProperty.equals("")) {
			getProject().setNewProperty(processedCountProperty, String.valueOf(count));
		}

		if (countProperty != null && !countProperty.equals("")) {
			final int globalCount = list.count("");
			getProject().setNewProperty(countProperty, String.valueOf(globalCount));
		}
	}

	public abstract int processExecutionWithCount();

	/**
	 * @param countProperty
	 *            the countProperty to set
	 */
	public void setCountProperty(final String countProperty) {
		this.countProperty = countProperty;
	}

	/**
	 * @param processedCountProperty
	 *            the processedCountProperty to set
	 */
	public void setProcessedCountProperty(final String processedCountProperty) {
		this.processedCountProperty = processedCountProperty;
	}

}
