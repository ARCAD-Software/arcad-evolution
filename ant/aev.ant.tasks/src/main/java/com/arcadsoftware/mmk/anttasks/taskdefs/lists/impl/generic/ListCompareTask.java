package com.arcadsoftware.mmk.anttasks.taskdefs.lists.impl.generic;

import java.io.File;

import org.apache.tools.ant.BuildException;

import com.arcadsoftware.mmk.anttasks.taskdefs.lists.AbstractXmlFileListTask;
import com.arcadsoftware.mmk.lists.impl.lists.GenericList;

public class ListCompareTask extends AbstractXmlFileListTask {
	protected boolean addedCheckIfExists = false;

	protected String addedFileName = null;
	protected boolean addedReplaceIfExists = false;
	protected boolean commonCheckIfExists = false;

	protected String commonFileName = null;
	protected boolean commongReplaceIfExists = false;

	protected boolean deletedCheckIfExists = false;
	protected String deletedFileName = null;

	protected boolean deletedReplaceIfExists = false;
	protected String operandFileName = null;

	@Override
	public void processExecution() {
		GenericList addedList = null;
		GenericList deletedList = null;
		GenericList commonList = null;

		// Chargement des métadatas
		list.getMetadatas().clear();
		list.load(false, true);

		if (addedFileName != null && !addedFileName.equals("")) {
			addedList = (GenericList) list.cloneList();
			addedList.setXmlFileName(addedFileName);
		}
		if (deletedFileName != null && !deletedFileName.equals("")) {
			deletedList = (GenericList) list.cloneList();
			deletedList.setXmlFileName(deletedFileName);
		}
		if (commonFileName != null && !commonFileName.equals("")) {
			commonList = (GenericList) list.cloneList();
			commonList.setXmlFileName(commonFileName);
		}
		final GenericList opList = (GenericList) list.cloneList();
		opList.setXmlFileName(operandFileName);
		list.compare(opList,
				addedList, addedCheckIfExists, addedReplaceIfExists,
				commonList, commonCheckIfExists, commongReplaceIfExists,
				deletedList, deletedCheckIfExists, deletedReplaceIfExists);
	}

	public void setAddedCheckIfExists(final boolean addedCheckIfExists) {
		this.addedCheckIfExists = addedCheckIfExists;
	}

	public void setAddedFileName(final String addedFileName) {
		this.addedFileName = addedFileName;
	}

	public void setAddedReplaceIfExists(final boolean addedReplaceIfExists) {
		this.addedReplaceIfExists = addedReplaceIfExists;
	}

	public void setCommonCheckIfExists(final boolean commonCheckIfExists) {
		this.commonCheckIfExists = commonCheckIfExists;
	}

	public void setCommonFileName(final String commonFileName) {
		this.commonFileName = commonFileName;
	}

	public void setCommongReplaceIfExists(final boolean commongReplaceIfExists) {
		this.commongReplaceIfExists = commongReplaceIfExists;
	}

	public void setDeletedCheckIfExists(final boolean deletedCheckIfExists) {
		this.deletedCheckIfExists = deletedCheckIfExists;
	}

	public void setDeletedFileName(final String deletedFileName) {
		this.deletedFileName = deletedFileName;
	}

	public void setDeletedReplaceIfExists(final boolean deletedReplaceIfExists) {
		this.deletedReplaceIfExists = deletedReplaceIfExists;
	}

	public void setOperandFileName(final String operandFileName) {
		this.operandFileName = operandFileName;
	}

	/*
	 * (non-Javadoc)
	 * @see com.arcadsoftware.mmk.anttasks.taskdefs.AbstractArcadAntTask#validateAttributes()
	 */
	@Override
	public void validateAttributes() {
		super.validateAttributes();
		if (operandFileName == null || operandFileName.equals("")) {
			throw new BuildException("OperandFileName attribute must be set!");
		} else {
			if (operandFileName.equals(getFilename())) {
				// TODO [ANT] Translation
				throw new BuildException("OperandFileName and Filename attributes must not have the same value!");
			} else {
				final File f = new File(operandFileName);
				if (!f.exists()) {
					throw new BuildException("Operand List File Not Found!");
				}
			}
		}
	}

}
