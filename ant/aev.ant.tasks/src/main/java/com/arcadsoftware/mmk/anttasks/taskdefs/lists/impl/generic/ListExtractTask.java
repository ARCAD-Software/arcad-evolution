package com.arcadsoftware.mmk.anttasks.taskdefs.lists.impl.generic;

import java.io.File;

import org.apache.tools.ant.BuildException;

import com.arcadsoftware.mmk.anttasks.taskdefs.lists.AbstractXmlFileListCountedCheckedTask;
import com.arcadsoftware.mmk.lists.impl.lists.GenericList;

public class ListExtractTask extends AbstractXmlFileListCountedCheckedTask{
	
	private String resultFileName = null;
	private String extractQuery = null;
	protected boolean clearListBeforeAdding = false;
	
	
	/* (non-Javadoc)
	 * @see com.arcadsoftware.mmk.anttasks.taskdefs.AbstractArcadAntTask#validateAttributes()
	 */
	@Override
	public void validateAttributes() {
		super.validateAttributes();
		if ((resultFileName!=null) && (!resultFileName.equals(""))) {
			if (resultFileName.equals(getFilename()))
				throw new BuildException("Main list and Result List must not be the same!");
			else {
				File f = new File(resultFileName);
				if (!f.exists()) {
					throw new BuildException("Result File Not Found!");
				}		
			}
		}		
	}	
	
	@Override
	public int processExecutionWithCount() {		
		if ((resultFileName!=null) && (!resultFileName.equals(""))) {
			GenericList targetList = (GenericList)list.cloneList();
			targetList.setXmlFileName(resultFileName);
			return list.extractItems(extractQuery,targetList,
					clearListBeforeAdding,
					checkIfExists,
					replaceIfExists);
		} else {
			return list.extractItems(extractQuery);
		}
	}


	/**
	 * @param clearListBeforeAdding the clearListBeforeAdding to set
	 */
	public void setClearListBeforeAdding(boolean clearListBeforeAdding) {
		this.clearListBeforeAdding = clearListBeforeAdding;
	}	
	
}
