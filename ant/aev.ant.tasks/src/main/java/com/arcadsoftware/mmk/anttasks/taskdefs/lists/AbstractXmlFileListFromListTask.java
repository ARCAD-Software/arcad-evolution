package com.arcadsoftware.mmk.anttasks.taskdefs.lists;

import java.io.File;

import org.apache.tools.ant.BuildException;

public abstract class AbstractXmlFileListFromListTask
		extends AbstractXmlFileListCountedCheckedTask {

	protected String fromListFileName = null;

	/**
	 * @param fromList
	 *            the fromList to set
	 */
	public void setFromListFileName(final String fromListFileName) {
		this.fromListFileName = fromListFileName;
	}

	/*
	 * (non-Javadoc)
	 * @see com.arcadsoftware.mmk.anttasks.taskdefs.AbstractArcadAntTask#validateAttributes()
	 */
	@Override
	public void validateAttributes() {
		if (getFilename() == null || getFilename().equals("")) {
			throw new BuildException("Filename attribute must be set!");
		}
		if (fromListFileName != null && !fromListFileName.equals("")) {
			final File f2 = new File(fromListFileName);
			if (!f2.exists()) {
				throw new BuildException("FromList File not found!");
			}
		}
	}

}
