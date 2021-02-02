package com.arcadsoftware.mmk.anttasks.taskdefs.test;

import java.io.File;

import org.apache.tools.ant.Project;
import org.apache.tools.ant.types.FileSet;

import com.arcadsoftware.mmk.anttasks.taskdefs.rollback.impl.ArcadCopyTask;
import com.arcadsoftware.mmk.anttasks.taskdefs.rollback.impl.ArcadRollbackTask;

public class Test {

	/**
	 * @param args
	 */
	public static void main(final String[] args) {

		final ArcadCopyTask t = new ArcadCopyTask();
		t.setProject(new Project());
		final File fromFile = new File("F:\\temp\\ARCAD-Builder-Linker.jar");
		final File toFile = new File("F:\\temp\\copy\\ARCAD-Builder-Linker.jar");
		final File rollbackDir = new File("F:\\temp\\backup");
		final FileSet set = new FileSet();
		set.setFile(fromFile);
		t.setTofile(toFile);
		t.setOverwrite(true);
		t.setRollbackDir(rollbackDir.getAbsolutePath());
		t.setRollbackId("111125152500");
		t.setFile(fromFile);
		t.execute();

		final ArcadRollbackTask rollback = new ArcadRollbackTask();
		rollback.setRollbackDir(rollbackDir.getAbsolutePath());
		rollback.setRollbackId("111125152500");
		rollback.execute();
	}

}
