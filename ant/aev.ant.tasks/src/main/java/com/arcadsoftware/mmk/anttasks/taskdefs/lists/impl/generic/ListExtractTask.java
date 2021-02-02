package com.arcadsoftware.mmk.anttasks.taskdefs.lists.impl.generic;

import java.io.File;

import org.apache.tools.ant.BuildException;

import com.arcadsoftware.mmk.anttasks.taskdefs.lists.AbstractXmlFileListCountedCheckedTask;
import com.arcadsoftware.mmk.lists.impl.lists.GenericList;

public class ListExtractTask extends AbstractXmlFileListCountedCheckedTask {

	protected boolean clearListBeforeAdding = false;
	private String extractQuery = null;
	private String resultFileName = null;

	public void setExtractQuery(String extractQuery) {
		this.extractQuery = extractQuery;
	}
	
	public void setResultFileName(String resultFileName) {
		this.resultFileName = resultFileName;
	}
	
	@Override
	public int processExecutionWithCount() {
		if (resultFileName != null && !resultFileName.equals("")) {
			final GenericList targetList = (GenericList) list.cloneList();
			targetList.setXmlFileName(resultFileName);
			return list.extractItems(extractQuery, targetList,
					clearListBeforeAdding,
					checkIfExists,
					replaceIfExists);
		} else {
			return list.extractItems(extractQuery);
		}
	}

	/**
	 * @param clearListBeforeAdding
	 *            the clearListBeforeAdding to set
	 */
	public void setClearListBeforeAdding(final boolean clearListBeforeAdding) {
		this.clearListBeforeAdding = clearListBeforeAdding;
	}


	@Override
	public void validateAttributes() {
		super.validateAttributes();
		if (resultFileName != null && !resultFileName.equals("")) {
			if (resultFileName.equals(getFilename())) {
				throw new BuildException("Main list and Result List must not be the same!");
			} else {
				final File f = new File(resultFileName);
				if (!f.exists()) {
					throw new BuildException("Result File Not Found!");
				}
			}
		}
	}

}
