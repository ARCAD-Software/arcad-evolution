package com.arcadsoftware.mmk.anttasks.taskdefs.lists;

import org.apache.tools.ant.BuildException;




public abstract class AbstractXmlFileListCountedTask extends AbstractXmlFileListTask {
	protected String processedCountProperty = "";
	protected String countProperty = "";
	
		
	
	
	/* (non-Javadoc)
	 * @see com.arcadsoftware.mmk.anttasks.taskdefs.lists.AbstractFileListTask#processExecution()
	 */
	@Override
	public void processExecution() throws BuildException {
		int count = processExecutionWithCount();
    	if ((processedCountProperty!=null) && (!processedCountProperty.equals(""))) {        	
    		getProject().setNewProperty(processedCountProperty, String.valueOf(count));
    	}   

    	if ((countProperty!=null) && (!countProperty.equals(""))) {
    		int globalCount = list.count("");
    		getProject().setNewProperty(countProperty, String.valueOf(globalCount));
    	}  			
	}

	public abstract int processExecutionWithCount();
	

	/**
	 * @param processedCountProperty the processedCountProperty to set
	 */
	public void setProcessedCountProperty(String processedCountProperty) {
		this.processedCountProperty = processedCountProperty;
	}

    
	/**
	 * @param countProperty the countProperty to set
	 */
	public void setCountProperty(String countProperty) {
		this.countProperty = countProperty;
	}   
	
	
	
}
