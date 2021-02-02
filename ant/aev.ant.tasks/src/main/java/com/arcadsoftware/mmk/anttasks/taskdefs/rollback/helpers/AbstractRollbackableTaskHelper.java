package com.arcadsoftware.mmk.anttasks.taskdefs.rollback.helpers;

import static com.arcadsoftware.mmk.anttasks.taskdefs.rollback.helpers.ERollbackStringConstants.RB_TAG_ACTION;
import static com.arcadsoftware.mmk.anttasks.taskdefs.rollback.helpers.ERollbackStringConstants.RB_TAG_CODE;
import static com.arcadsoftware.mmk.anttasks.taskdefs.rollback.helpers.ERollbackStringConstants.RB_TAG_VERSION;

import java.io.File;

import org.apache.tools.ant.BuildException;
import org.w3c.dom.Element;

import com.arcadsoftware.ae.core.utils.XMLUtils;
import com.arcadsoftware.mmk.anttasks.taskdefs.rollback.IRollbackableTask;

public abstract class AbstractRollbackableTaskHelper
		extends AbstractRollbackableHelper {

	public AbstractRollbackableTaskHelper() {
		super();
	}

	public AbstractRollbackableTaskHelper(final IRollbackableTask task) {
		super(task);
	}

	public String createNewActionDirectory() {
		final String basedir = getBackupRoot();
		final String baseFile = basedir + File.separator + task.getActionCode();
		int i = 0;
		File f;
		do {
			i++;
			f = new File(baseFile + i);
		} while (f.exists());

		task.getTask().getProject().log("--> " + f.getAbsolutePath());
		final File parent = f.getParentFile();
		task.getTask().getProject().log("--> parent " + parent.getAbsolutePath());
		if (!parent.exists()) {
			task.getTask().getProject().log("--> parent : create directory");
			if (f.mkdirs()) {
				return f.getAbsolutePath();
			} else {
				task.getTask().getProject().log("--> error creating directory");
				return null;
			}
		} else {
			return f.getAbsolutePath();
		}
	}

	@Override
	public Element createRollbackData(final Element e) {
		final Element action = XMLUtils.addElement(document, e, RB_TAG_ACTION.getValue());
		action.setAttribute(RB_TAG_CODE.getValue(), task.getActionCode());
		action.setAttribute(RB_TAG_VERSION.getValue(), task.getVersion());
		return action;
	}

	@Override
	public void doBeforeExecuting() {
		dataDirectory = createNewActionDirectory();
		if (dataDirectory == null) {
			throw new BuildException("Unable to create rollback directory!", task.getTask().getLocation());
		}
	}

}
