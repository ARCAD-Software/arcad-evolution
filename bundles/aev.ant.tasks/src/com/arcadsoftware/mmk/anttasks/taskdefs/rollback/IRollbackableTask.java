package com.arcadsoftware.mmk.anttasks.taskdefs.rollback;

import org.apache.tools.ant.Task;

import com.arcadsoftware.mmk.anttasks.taskdefs.rollback.helpers.AbstractRollbackableHelper;



public interface IRollbackableTask {
	public String getActionCode();
	public String getVersion();	
	public void setRollbackDir(String rollbackDir);	
	public void setRollbackId(String rollbackId);	
	public void setRollbackIdProperty(String rollbackIdProperty);	
	public AbstractRollbackableHelper getHelper();
	public boolean isInTransaction();	
	public void setInTransaction(boolean inTransaction);	
	public Task getTask();
	
}
