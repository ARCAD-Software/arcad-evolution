package com.arcadsoftware.mmk.anttasks.taskdefs.rollback;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import org.apache.tools.ant.Project;

public abstract class AbstractArcadDeleteTask extends DeleteFakeTask {

	@Override
	protected boolean delete(final File f) {
		// <added lines>
		if (doBeforeDeleting(f)) {
			// </added lines>
			try {
				Files.delete(f.toPath());
				return true;
			}
			catch(IOException e) {
				log(e, Project.MSG_WARN);
				try {
					Thread.sleep(getRetryDelay());
					Files.delete(f.toPath());
					return true;
				}
				catch(IOException ex) {
					if (isDeleteOnExit()) {
						final int level = isQuiet() ? Project.MSG_VERBOSE : Project.MSG_INFO;
						log("Failed to delete " + f + ", calling deleteOnExit."
								+ " This attempts to delete the file when the Ant jvm"
								+ " has exited and might not succeed.", level);
						f.deleteOnExit();
						return true;
					}
					else {
						log(ex, Project.MSG_WARN);
						return false;
					}
				}
				catch (InterruptedException e1) {
					log(e1, Project.MSG_WARN);
					Thread.currentThread().interrupt();
				}
			}
		}
		return false;
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
