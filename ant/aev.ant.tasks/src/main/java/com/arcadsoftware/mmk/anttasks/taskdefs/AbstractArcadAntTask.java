package com.arcadsoftware.mmk.anttasks.taskdefs;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

public abstract class AbstractArcadAntTask extends Task {
	protected void doBeforeExecuting() {

	}

	public abstract void doExecute() throws BuildException;

	@Override
	public void execute() {
		validateAttributes();
		doBeforeExecuting();
		doExecute();
	}

	public abstract void validateAttributes();
}
