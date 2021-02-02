package com.arcadsoftware.mmk.anttasks.taskdefs.rollback;

import org.apache.tools.ant.Task;

import com.arcadsoftware.mmk.anttasks.taskdefs.rollback.helpers.AbstractRollbackableHelper;

public interface IRollbackableTask {
	String getActionCode();

	AbstractRollbackableHelper getHelper();

	Task getTask();

	String getVersion();

	boolean isInTransaction();

	void setInTransaction(boolean inTransaction);

	void setRollbackDir(String rollbackDir);

	void setRollbackId(String rollbackId);

	void setRollbackIdProperty(String rollbackIdProperty);

}
