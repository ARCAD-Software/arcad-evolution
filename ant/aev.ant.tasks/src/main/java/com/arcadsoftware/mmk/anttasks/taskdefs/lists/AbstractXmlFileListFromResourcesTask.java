package com.arcadsoftware.mmk.anttasks.taskdefs.lists;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.types.FileSet;
import org.apache.tools.ant.types.ResourceCollection;

import com.arcadsoftware.mmk.lists.EListConstants;

public abstract class AbstractXmlFileListFromResourcesTask
		extends AbstractXmlFileListCountedTask {
	protected List<String> files;
	protected String fromListFileName = null;

	protected ResourceResolverHelper helper;
	protected List<ResourceCollection> rcs = new ArrayList<>();

	/**
	 * Add a collection of files to copy.
	 *
	 * @param res
	 *            a resource collection to copy.
	 * @since Ant 1.7
	 */
	public void add(final ResourceCollection res) {
		rcs.add(res);
	}

	/**
	 * Add a set of files to copy.
	 *
	 * @param set
	 *            a set of files to copy.
	 */
	public void addFileset(final FileSet set) {
		add(set);
	}

	/*
	 * (non-Javadoc)
	 * @see com.arcadsoftware.mmk.anttasks.taskdefs.AbstractArcadAntTask#doBeforeExecuting()
	 */
	@Override
	protected void doBeforeExecuting() {
		super.doBeforeExecuting();
		listType = EListConstants.LST_TYPE_FILE.getValue();
	}

	public abstract int process();

	/*
	 * (non-Javadoc)
	 * @see com.arcadsoftware.mmk.anttasks.taskdefs.lists.AbstractFileListWithProcessTask#processExecutionWithCount()
	 */
	@Override
	public int processExecutionWithCount() {
		helper = new ResourceResolverHelper();
		helper.getElements(getProject(), rcs);
		files = helper.getFileList();

		return process();
	}

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
