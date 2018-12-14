package com.arcadsoftware.mmk.anttasks.taskdefs;


import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

import com.arcadsoftware.mmk.anttasks.taskdefs.misc.AntMessageRouter;

public abstract class AbstractArcadAntTask extends Task {
	private AntMessageRouter router;
	
	public abstract void validateAttributes();
	public abstract void doExecute() throws BuildException;	
	
    public void execute() throws BuildException {  
    	validateAttributes();
    	doBeforeExecuting();
    	doExecute();
    }	
	
	protected void doBeforeExecuting() {
		
	}
}
