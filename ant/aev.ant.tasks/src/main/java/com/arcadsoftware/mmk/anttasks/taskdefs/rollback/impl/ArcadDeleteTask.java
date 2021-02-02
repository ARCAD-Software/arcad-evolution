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

	private static final String VERSION = "1.0.0.0";
	DeleteFileHelper helper;

	private boolean inTransaction = false;

	public ArcadDeleteTask() {
		super();
		helper = new DeleteFileHelper(this);
	}

	@Override
	public void doAfterExecuting() {
		helper.doAfterExecuting();
	}

	@Override
	public boolean doBeforeDeleting(final File f) {
		helper.backup(f);
		return true;
	}

	@Override
	public void doBeforeExecuting() {
		helper.doBeforeExecuting();
	}

	@Override
	public String getActionCode() {
		return ERollbackStringConstants.RB_ACTIONCODE_DELETE.getValue();
	}

	@Override
	public AbstractRollbackableHelper getHelper() {
		return helper;
	}

	@Override
	public Task getTask() {
		return this;
	}

	@Override
	public String getVersion() {
		return VERSION;
	}

	@Override
	public boolean isInTransaction() {
		return inTransaction;
	}

	@Override
	public void setInTransaction(final boolean inTransaction) {
		this.inTransaction = inTransaction;
	}

	@Override
	public void setRollbackDir(final String rollbackDir) {
		helper.setRollbackDir(rollbackDir);
	}

	@Override
	public void setRollbackId(final String rollbackId) {
		helper.setRollbackId(rollbackId);
	}

	@Override
	public void setRollbackIdProperty(final String rollbackIdProperty) {
		helper.setRollbackIdProperty(rollbackIdProperty);
	}

}
