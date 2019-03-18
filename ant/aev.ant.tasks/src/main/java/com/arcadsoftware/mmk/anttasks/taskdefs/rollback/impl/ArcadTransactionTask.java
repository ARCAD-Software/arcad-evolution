package com.arcadsoftware.mmk.anttasks.taskdefs.rollback.impl;

import java.util.Iterator;
import java.util.Vector;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.TaskContainer;
import org.apache.tools.ant.UnknownElement;

import com.arcadsoftware.ae.core.logger.MessageLogger;
import com.arcadsoftware.mmk.anttasks.AntFactory;
import com.arcadsoftware.mmk.anttasks.taskdefs.AbstractArcadAntTask;
import com.arcadsoftware.mmk.anttasks.taskdefs.rollback.IRollbackableTask;
import com.arcadsoftware.mmk.anttasks.taskdefs.rollback.helpers.AbstractRollbackableHelper;
import com.arcadsoftware.mmk.anttasks.taskdefs.rollback.helpers.AbstractRollbackableTaskContainerHelper;

import static com.arcadsoftware.mmk.anttasks.taskdefs.rollback.helpers.ERollbackStringConstants.*;

public class ArcadTransactionTask extends AbstractArcadAntTask 
implements TaskContainer,IRollbackableTask {
	
	private static final String VERSION= "1.0.0.0";	
	
    private Vector<Task> nestedTasks = new Vector<Task>();
    private Vector<IRollbackableTask> executedTasks = new Vector<IRollbackableTask>();
    
    private class TransactionHelper extends AbstractRollbackableTaskContainerHelper {		
		public TransactionHelper(IRollbackableTask task) {
			super(task);
		}

		@Override
		public Iterator getTasks() {		
			return executedTasks.iterator();
		}    	
    }
    
    TransactionHelper helper;
    
    
    public ArcadTransactionTask(){
    	super();
    	helper = new TransactionHelper(this);
    }
        
	@Override
	public void validateAttributes() {
		// Contrôle qu'il n'y a pas de tache <transaction> intégrée.
        for (Iterator i = nestedTasks.iterator(); i.hasNext();) {
        	Object o =  i.next(); 
        	((UnknownElement)o).maybeConfigure();        	
        	if (((UnknownElement)o).getTask() instanceof ArcadTransactionTask){
        		throw new BuildException("Nested Transaction task is not allowed!");
        	}
        }		
	}
	public void addTask(Task task) {
		nestedTasks.addElement(task);
	}
    
	@Override
	public void doExecute() throws BuildException {
		helper.doBeforeExecuting();
		try {
	    	try{
		        for (Iterator i = nestedTasks.iterator(); i.hasNext();) {
		        	Object o =  i.next(); 
	            	((UnknownElement)o).maybeConfigure();
	            	
	            	if (((UnknownElement)o).getTask() instanceof IRollbackableTask){
	            		Task tt = (Task)((UnknownElement)o).getTask();
		            	IRollbackableTask t = (IRollbackableTask)tt;
		            	t.setRollbackDir(helper.getRollbackDir());
		            	t.setRollbackId(helper.getRollbackId());
		            	t.setInTransaction(true);
		            	executedTasks.addElement(t);
	            	}
	            	Task nestedTask = (Task) o;
		            nestedTask.perform();
		            
		        }
	    	} catch (Exception e) {
	    		MessageLogger.sendErrorMessage(AntFactory.MODULE_NAME,e);
			}
		} finally { 
			helper.doAfterExecuting();
		}
	}
	
	public String getActionCode() {
		return RB_ACTIONCODE_TRANSACTION.getValue();
	}
	
	public void setRollbackDir(String rollbackDir) {
		helper.setRollbackDir(rollbackDir);
	}

	public void setRollbackIdProperty(String rollbackIdProperty) {
		helper.setRollbackIdProperty(rollbackIdProperty);		
	}
	
	public void setRollbackId(String rollbackId) {
		helper.setRollbackId(rollbackId);		
	}
	
	public String getVersion() {
		return VERSION;
	}

	public AbstractRollbackableHelper getHelper(){
		return helper;
	}


	public boolean isInTransaction() {
		return false;
	}

	public void setInTransaction(boolean inTransaction) {
	}

	public Task getTask() {
		return this;
	}

}

