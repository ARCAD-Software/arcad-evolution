package com.arcadsoftware.mmk.anttasks.taskdefs.rollback.impl;

import org.apache.tools.ant.Task;

import com.arcadsoftware.mmk.anttasks.taskdefs.rollback.AbstractArcadCopyTask;
import com.arcadsoftware.mmk.anttasks.taskdefs.rollback.IRollbackableTask;
import com.arcadsoftware.mmk.anttasks.taskdefs.rollback.helpers.AbstractRollbackableHelper;
import com.arcadsoftware.mmk.anttasks.taskdefs.rollback.helpers.CopyFileHelper;
import com.arcadsoftware.mmk.anttasks.taskdefs.rollback.helpers.ERollbackStringConstants;

public class ArcadCopyTask extends AbstractArcadCopyTask implements IRollbackableTask{
	protected static final String VERSION= "1.0.0.1";
	protected boolean inTransaction = false;
	
	protected CopyFileHelper helper;
	
	public ArcadCopyTask(){
		super();
		helper = new CopyFileHelper(this);
	}
	
	@Override
	public void doBeforeExecuting() {	
		helper.doBeforeExecuting();
	}

	@Override
	public void doAfterExecuting() {
		helper.doAfterExecuting();
	}

	@Override
	public void doBeforeCopying(String fromFile, String toFile,boolean overwrite) {
		log("Copy "+fromFile+" to "+toFile);
		helper.backupFile(fromFile,toFile,overwrite);		
	}

	public String getActionCode() {
		return ERollbackStringConstants.RB_ACTIONCODE_COPY.getValue();
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

	
	//TODO [ANT] changer le nom de la procedure pour eviter la prise en charge bean
	public void setInTransaction(boolean inTransaction) {
		this.inTransaction = inTransaction;
	}

	public Task getTask() {
		return this;
	}
}
