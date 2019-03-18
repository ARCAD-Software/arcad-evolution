package com.arcadsoftware.mmk.anttasks.taskdefs.lists.impl.generic;

import java.io.File;

import org.apache.tools.ant.BuildException;

import com.arcadsoftware.mmk.anttasks.taskdefs.lists.AbstractXmlFileListTask;
import com.arcadsoftware.mmk.lists.impl.lists.GenericList;

public class ListCompareTask extends AbstractXmlFileListTask {
	protected String operandFileName = null;
	
	protected String addedFileName = null;
	protected String deletedFileName = null;
	protected String commonFileName = null;	
	
	protected boolean addedCheckIfExists = false; 
	protected boolean addedReplaceIfExists = false; 
	
	protected boolean deletedCheckIfExists = false; 
	protected boolean deletedReplaceIfExists = false; 
	
	protected boolean commonCheckIfExists = false; 
	protected boolean commongReplaceIfExists = false; 	
	
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
	}		
	
	@Override
	public void processExecution() throws BuildException {
		GenericList addedList=null;
		GenericList deletedList=null;		
		GenericList commonList=null;		
		
		//Chargement des métadatas
		list.getMetadatas().clear();		
		list.load(false,true);
		
		
		if ((addedFileName!=null) && (!addedFileName.equals(""))) {
			addedList = (GenericList)list.cloneList();
			addedList.setXmlFileName(addedFileName);
		}
		if ((deletedFileName!=null) && (!deletedFileName.equals(""))) {
			deletedList = (GenericList)list.cloneList();
			deletedList.setXmlFileName(deletedFileName);
		}
		if ((commonFileName!=null) && (!commonFileName.equals(""))) {
			commonList = (GenericList)list.cloneList();
			commonList.setXmlFileName(commonFileName);
		}	
		GenericList opList=(GenericList)list.cloneList();
		opList.setXmlFileName(operandFileName);
		list.compare(opList,
				     addedList,addedCheckIfExists,addedReplaceIfExists,
				     commonList,commonCheckIfExists,commongReplaceIfExists,
				     deletedList,deletedCheckIfExists,deletedReplaceIfExists);
	}


	public void setOperandFileName(String operandFileName) {
		this.operandFileName = operandFileName;
	}

	public void setAddedCheckIfExists(boolean addedCheckIfExists) {
		this.addedCheckIfExists = addedCheckIfExists;
	}

	public void setAddedFileName(String addedFileName) {
		this.addedFileName = addedFileName;
	}

	public void setAddedReplaceIfExists(boolean addedReplaceIfExists) {
		this.addedReplaceIfExists = addedReplaceIfExists;
	}

	public void setCommonCheckIfExists(boolean commonCheckIfExists) {
		this.commonCheckIfExists = commonCheckIfExists;
	}

	public void setCommonFileName(String commonFileName) {
		this.commonFileName = commonFileName;
	}

	public void setCommongReplaceIfExists(boolean commongReplaceIfExists) {
		this.commongReplaceIfExists = commongReplaceIfExists;
	}

	public void setDeletedCheckIfExists(boolean deletedCheckIfExists) {
		this.deletedCheckIfExists = deletedCheckIfExists;
	}

	public void setDeletedFileName(String deletedFileName) {
		this.deletedFileName = deletedFileName;
	}

	public void setDeletedReplaceIfExists(boolean deletedReplaceIfExists) {
		this.deletedReplaceIfExists = deletedReplaceIfExists;
	}

}
