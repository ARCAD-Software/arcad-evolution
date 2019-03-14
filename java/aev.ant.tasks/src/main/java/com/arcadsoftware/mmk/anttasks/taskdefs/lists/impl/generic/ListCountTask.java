package com.arcadsoftware.mmk.anttasks.taskdefs.lists.impl.generic;

import org.apache.tools.ant.BuildException;

import com.arcadsoftware.mmk.anttasks.taskdefs.lists.AbstractXmlFileListTask;

public class ListCountTask extends AbstractXmlFileListTask {
	protected String countProperty = "";
	protected String countQuery = null;
	
	
	@Override
	public void processExecution() throws BuildException {
		int count = list.count(countQuery);
    	if ((countProperty!=null) && (!countProperty.equals(""))) {        	
    		getProject().setNewProperty(countProperty, String.valueOf(count));
    	}   		
	}
	
	/**
	 * @param countProperty the countProperty to set
	 */
	public void setCountProperty(String countProperty) {
		this.countProperty = countProperty;
	}

	/**
	 * @param countQuery the countQuery to set
	 */
	public void setCountQuery(String countQuery) {
		this.countQuery = countQuery;
	}   
}
