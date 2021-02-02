package com.arcadsoftware.mmk.anttasks.taskdefs.rollback.impl;

import org.apache.tools.ant.Task;

import com.arcadsoftware.mmk.anttasks.taskdefs.rollback.AbstractArcadCopyTask;
import com.arcadsoftware.mmk.anttasks.taskdefs.rollback.IRollbackableTask;
import com.arcadsoftware.mmk.anttasks.taskdefs.rollback.helpers.AbstractRollbackableHelper;
import com.arcadsoftware.mmk.anttasks.taskdefs.rollback.helpers.CopyFileHelper;
import com.arcadsoftware.mmk.anttasks.taskdefs.rollback.helpers.ERollbackStringConstants;

public class ArcadCopyTask extends AbstractArcadCopyTask implements IRollbackableTask {
	protected static final String VERSION = "1.0.0.1";
	protected CopyFileHelper helper;

	protected boolean inTransaction = false;

	public ArcadCopyTask() {
		super();
		helper = new CopyFileHelper(this);
	}

	@Override
	public void doAfterExecuting() {
		helper.doAfterExecuting();
	}

	@Override
	public void doBeforeCopying(final String fromFile, final String toFile, final boolean overwrite) {
		log("Copy " + fromFile + " to " + toFile);
		helper.backupFile(fromFile, toFile, overwrite);
	}

	@Override
	public void doBeforeExecuting() {
		helper.doBeforeExecuting();
	}

	@Override
	public String getActionCode() {
		return ERollbackStringConstants.RB_ACTIONCODE_COPY.getValue();
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

	// TODO [ANT] changer le nom de la procedure pour eviter la prise en charge bean
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
