package com.arcadsoftware.mmk.anttasks.taskdefs.rollback;

import java.io.File;

import org.apache.tools.ant.Project;

public abstract class AbstractArcadDeleteTask extends DeleteFakeTask {

	@Override
	protected boolean delete(final File f) {
		// <added lines>
		if (doBeforeDeleting(f)) {
			// </added lines>
			if (!f.delete()) {
				try {
					Thread.sleep(getRetryDelay());
				} catch (final InterruptedException ex) {
					Thread.currentThread().interrupt();
				}
				if (!f.delete()) {
					if (isDeleteOnExit()) {
						final int level = isQuiet() ? Project.MSG_VERBOSE : Project.MSG_INFO;
						log("Failed to delete " + f + ", calling deleteOnExit."
								+ " This attempts to delete the file when the Ant jvm"
								+ " has exited and might not succeed.", level);
						f.deleteOnExit();
						return true;
					}
					return false;
				}
			}
			return true;
			// <added lines>
		} else {
			return false;
		}
		// </added lines>
	}

	public abstract void doAfterExecuting();

	public abstract boolean doBeforeDeleting(File f);

	public abstract void doBeforeExecuting();

	@Override
	public void execute() {
		doBeforeExecuting();
		try {
			super.execute();
		} finally {
			doAfterExecuting();
		}
	}

}
