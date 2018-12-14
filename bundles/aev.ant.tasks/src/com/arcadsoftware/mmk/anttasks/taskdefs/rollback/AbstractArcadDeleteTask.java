package com.arcadsoftware.mmk.anttasks.taskdefs.rollback;

import java.io.File;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.condition.Os;


public abstract class AbstractArcadDeleteTask extends DeleteFakeTask {

	
	@Override
	public void execute() throws BuildException {
		doBeforeExecuting();
		try {
			super.execute();
		} finally {
			doAfterExecuting();
		}
	}	
	
    protected boolean delete(File f) {
    	//<added lines>
		if (doBeforeDeleting(f)) {
	    //</added lines>			
	        if (!f.delete()) {
	            if (Os.isFamily("windows")) {
	                System.gc();
	            }
	            try {
	                Thread.sleep(getDELETE_RETRY_SLEEP_MILLIS());
	            } catch (InterruptedException ex) {
	                // Ignore Exception
	            }
	            if (!f.delete()) {
	                if (isDeleteOnExit()) {
	                    int level = isQuiet() ? Project.MSG_VERBOSE : Project.MSG_INFO;
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
	    //<added lines>	        
		} else 
			return false;
    	//</added lines>
    }

	public abstract boolean doBeforeDeleting(File f);
	public abstract void doBeforeExecuting();	
	public abstract void doAfterExecuting();    
    
}
