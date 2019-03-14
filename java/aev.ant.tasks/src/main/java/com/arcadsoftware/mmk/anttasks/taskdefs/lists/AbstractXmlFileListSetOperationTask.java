package com.arcadsoftware.mmk.anttasks.taskdefs.lists;

import java.io.File;

import org.apache.tools.ant.BuildException;

public abstract class AbstractXmlFileListSetOperationTask 
extends AbstractXmlFileListCountedCheckedTask {

	protected String operandFileName = null;
	protected String resultFileName = null;
	
	
	/* (non-Javadoc)
	 * @see com.arcadsoftware.mmk.anttasks.taskdefs.AbstractArcadAntTask#validateAttributes()
	 */
	@Override
	public void validateAttributes() {
		super.validateAttributes();
		if ((operandFileName==null) || (operandFileName.equals(""))) {		
			throw new BuildException("OperandFileName attribute must be set!");
		} else {
			if (operandFileName.equals(getFilename())) {
				//TODO [ANT] Translation
				throw new BuildException("OperandFileName and Filename attributes must not have the same value!");
			}else {			
				File f = new File(operandFileName);
				if (!f.exists()) {
					throw new BuildException("Operand List File Not Found!");
				}
			}
		}
		if ((resultFileName==null) || (resultFileName.equals(""))) {		
			throw new BuildException("resultFileName attribute must be set!");
		}		
	}	
	
	/**
	 * @param operandFileName the operandFileName to set
	 */
	public void setOperandFileName(String operandFileName) {
		this.operandFileName = operandFileName;
	}
	/**
	 * @param resultFileName the resultFileName to set
	 */
	public void setResultFileName(String resultFileName) {
		this.resultFileName = resultFileName;
	}
	
	
	

}
