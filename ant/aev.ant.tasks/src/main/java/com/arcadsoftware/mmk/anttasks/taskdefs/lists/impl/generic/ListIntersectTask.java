package com.arcadsoftware.mmk.anttasks.taskdefs.lists.impl.generic;

import com.arcadsoftware.mmk.anttasks.taskdefs.lists.AbstractXmlFileListSetOperationTask;
import com.arcadsoftware.mmk.lists.impl.lists.GenericList;

public class ListIntersectTask extends AbstractXmlFileListSetOperationTask {

	@Override
	public int processExecutionWithCount() {
		final GenericList operandList = (GenericList) list.cloneList();
		operandList.setXmlFileName(operandFileName);
		if (resultFileName.equals(operandFileName)) {
			return list.intersect(operandList, operandList, checkIfExists, replaceIfExists);
		} else if (resultFileName.equals(getFilename())) {
			return list.intersect(operandList, list, checkIfExists, replaceIfExists);
		} else {
			final GenericList resultList = (GenericList) list.cloneList();
			resultList.setXmlFileName(resultFileName);
			return list.intersect(operandList, resultList, checkIfExists, replaceIfExists);
		}
	}
}
