package com.arcadsoftware.mmk.anttasks.taskdefs.rollback.impl;

import static com.arcadsoftware.mmk.anttasks.taskdefs.rollback.helpers.ERollbackStringConstants.RB_ACTIONCODE_TRANSACTION;

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

public class ArcadTransactionTask extends AbstractArcadAntTask
		implements TaskContainer, IRollbackableTask {

	private class TransactionHelper extends AbstractRollbackableTaskContainerHelper {
		public TransactionHelper(final IRollbackableTask task) {
			super(task);
		}

		@Override
		public Iterator getTasks() {
			return executedTasks.iterator();
		}
	}

	private static final String VERSION = "1.0.0.0";
	private final Vector<IRollbackableTask> executedTasks = new Vector<>();

	TransactionHelper helper;

	private final Vector<Task> nestedTasks = new Vector<>();

	public ArcadTransactionTask() {
		super();
		helper = new TransactionHelper(this);
	}

	@Override
	public void addTask(final Task task) {
		nestedTasks.addElement(task);
	}

	@Override
	public void doExecute() {
		helper.doBeforeExecuting();
		try {
			try {
				for (final Object o : nestedTasks) {
					((UnknownElement) o).maybeConfigure();

					if (((UnknownElement) o).getTask() instanceof IRollbackableTask) {
						final Task tt = ((UnknownElement) o).getTask();
						final IRollbackableTask t = (IRollbackableTask) tt;
						t.setRollbackDir(helper.getRollbackDir());
						t.setRollbackId(helper.getRollbackId());
						t.setInTransaction(true);
						executedTasks.addElement(t);
					}
					final Task nestedTask = (Task) o;
					nestedTask.perform();

				}
			} catch (final Exception e) {
				MessageLogger.sendErrorMessage(AntFactory.MODULE_NAME, e);
			}
		} finally {
			helper.doAfterExecuting();
		}
	}

	@Override
	public String getActionCode() {
		return RB_ACTIONCODE_TRANSACTION.getValue();
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
		return false;
	}

	@Override
	public void setInTransaction(final boolean inTransaction) {
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

	@Override
	public void validateAttributes() {
		// Contrôle qu'il n'y a pas de tache <transaction> intégrée.
		for (final Object o : nestedTasks) {
			((UnknownElement) o).maybeConfigure();
			if (((UnknownElement) o).getTask() instanceof ArcadTransactionTask) {
				throw new BuildException("Nested Transaction task is not allowed!");
			}
		}
	}

}
