package com.arcadsoftware.mmk.anttasks.taskdefs.lists.impl.generic;

import com.arcadsoftware.mmk.anttasks.taskdefs.lists.AbstractXmlFileListSetOperationTask;
import com.arcadsoftware.mmk.lists.impl.lists.GenericList;

public class ListSubstractTask extends AbstractXmlFileListSetOperationTask {

	@Override
	public int processExecutionWithCount() {
		GenericList operandList = (GenericList)list.cloneList();
		operandList.setXmlFileName(operandFileName);
		if (resultFileName.equals(operandFileName))	
			return list.substract(operandList,operandList,checkIfExists,replaceIfExists);
		else if (resultFileName.equals(getFilename()))
			return list.substract(operandList,list,checkIfExists,replaceIfExists);
		else {
			GenericList resultList = (GenericList)list.cloneList();
			resultList.setXmlFileName(resultFileName);			
			return list.substract(operandList,resultList,checkIfExists,replaceIfExists);
		}
	}

}
