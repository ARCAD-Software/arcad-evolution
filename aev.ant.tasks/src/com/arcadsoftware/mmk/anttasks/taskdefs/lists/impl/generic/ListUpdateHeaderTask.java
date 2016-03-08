package com.arcadsoftware.mmk.anttasks.taskdefs.lists.impl.generic;

import org.apache.tools.ant.BuildException;

import com.arcadsoftware.mmk.anttasks.taskdefs.lists.AbstractXmlFileListTask;

public class ListUpdateHeaderTask extends AbstractXmlFileListTask {
    private String description = null;
    private String comment = null;
    
	@Override
	public void processExecution() throws BuildException {
        updateHeaderInfo(description,comment);
	}

	/**
	 * @param comment the comment to set
	 */
	public void setComment(String comment) {
		this.comment = comment;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}		
	
}
