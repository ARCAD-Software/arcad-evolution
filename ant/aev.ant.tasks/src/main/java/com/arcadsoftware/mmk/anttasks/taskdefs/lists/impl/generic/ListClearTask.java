package com.arcadsoftware.mmk.anttasks.taskdefs.lists.impl.generic;

import com.arcadsoftware.mmk.anttasks.taskdefs.lists.AbstractXmlFileListCountedTask;

public class ListClearTask extends AbstractXmlFileListCountedTask {
	@Override
	public int processExecutionWithCount() {
		final int count = list.clearItems();
		if (processedCountProperty != null && !processedCountProperty.equals("")) {
			getProject().setNewProperty(processedCountProperty, String.valueOf(count));
		}
		return count;
	}

}
