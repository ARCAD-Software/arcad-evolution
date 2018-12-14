package com.arcadsoftware.mmk.anttasks.taskdefs.rollback.impl;

import java.io.File;

import org.apache.tools.ant.Task;


import com.arcadsoftware.mmk.anttasks.taskdefs.rollback.AbstractArcadDeleteTask;
import com.arcadsoftware.mmk.anttasks.taskdefs.rollback.IRollbackableTask;
import com.arcadsoftware.mmk.anttasks.taskdefs.rollback.helpers.AbstractRollbackableHelper;
import com.arcadsoftware.mmk.anttasks.taskdefs.rollback.helpers.DeleteFileHelper;
import com.arcadsoftware.mmk.anttasks.taskdefs.rollback.helpers.ERollbackStringConstants;



public class ArcadDeleteTask extends AbstractArcadDeleteTask 
implements IRollbackableTask {
	
	private static final String VERSION= "1.0.0.0";
	private boolean inTransaction = false;
	
	
	DeleteFileHelper helper;
	
	public ArcadDeleteTask(){
		super();
		helper = new DeleteFileHelper(this);
	}
	
	public boolean doBeforeDeleting(File f){
		helper.backup(f);
		return true;
	}	
	
	
	@Override
	public void doBeforeExecuting() {		
		helper.doBeforeExecuting();
	}

	@Override
	public void doAfterExecuting() {
		helper.doAfterExecuting();
	}


	public String getActionCode() {
		return ERollbackStringConstants.RB_ACTIONCODE_DELETE.getValue();
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
		return inTransaction;
	}

	public void setInTransaction(boolean inTransaction) {
		this.inTransaction = inTransaction;
	}

	public Task getTask() {
		return this;
	}	
	


	
	
}
