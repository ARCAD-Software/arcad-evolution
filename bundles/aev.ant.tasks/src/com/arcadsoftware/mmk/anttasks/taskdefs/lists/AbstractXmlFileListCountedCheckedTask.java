package com.arcadsoftware.mmk.anttasks.taskdefs.lists;


public abstract class AbstractXmlFileListCountedCheckedTask 
extends AbstractXmlFileListCountedTask {
	protected boolean checkIfExists = false; 
	protected boolean replaceIfExists = false; 

	
	/**
	 * @param checkIfExists the checkIfExists to set
	 */
	public void setCheckIfExists(boolean checkIfExists) {
		this.checkIfExists = checkIfExists;
	}

	/**
	 * @param replaceIfExists the replaceIfExists to set
	 */
	public void setReplaceIfExists(boolean replaceIfExists) {
		this.replaceIfExists = replaceIfExists;
	}	

	
	
	
}
