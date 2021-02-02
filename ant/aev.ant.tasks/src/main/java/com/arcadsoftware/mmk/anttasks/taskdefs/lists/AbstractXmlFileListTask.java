package com.arcadsoftware.mmk.anttasks.taskdefs.lists;

import java.io.File;

import org.apache.tools.ant.BuildException;

import com.arcadsoftware.mmk.anttasks.AntFactory;
import com.arcadsoftware.mmk.lists.AbstractXmlList;
import com.arcadsoftware.mmk.lists.EListConstants;

public abstract class AbstractXmlFileListTask extends AbstractListTask {
	private String filename;
	protected AbstractXmlList list;
	protected String listType = EListConstants.LST_TYPE_GENERIC.getValue();

	/*
	 * (non-Javadoc)
	 * @see com.arcadsoftware.mmk.anttasks.taskdefs.AbstractArcadAntTask#doExecute()
	 */
	@Override
	public void doExecute() {

		list = (AbstractXmlList) AntFactory.getInstance().getBean(listType);
		list.setXmlFileName(getFilename());
		processExecution();
	}

	/**
	 * Renvoit
	 * 
	 * @return the filename String :
	 */
	public String getFilename() {
		return filename;
	}

	public abstract void processExecution();

	/**
	 * @param filename
	 *            the filename to set
	 */
	public void setFilename(final String filename) {
		this.filename = filename;
	}

	/**
	 * @param listType
	 *            the listType to set
	 */
	public void setListType(final String listType) {
		this.listType = listType;
	}

	public void updateHeaderInfo(final String description, final String comment) {
		if (!list.getCashManager().isActive()) {
			list.load(false, false);
		}

		if (description != null) {
			list.getHeader().setDescription(description);
		}
		if (comment != null) {
			list.getHeader().setComment(comment);
		}
		list.getCashManager().flushRequest();
	}

	/*
	 * (non-Javadoc)
	 * @see com.arcadsoftware.mmk.anttasks.taskdefs.AbstractArcadAntTask#validateAttributes()
	 */
	@Override
	public void validateAttributes() {
		super.validateAttributes();
		if (filename == null || filename.equals("")) {
			throw new BuildException("Filename attribute must be set!");
		} else {
			final File f = new File(filename);
			if (!f.exists()) {
				throw new BuildException("List File Not Found!");
			}
		}
	}

}
