package com.arcadsoftware.mmk.anttasks.taskdefs.rollback.helpers;

import java.util.Iterator;

import org.w3c.dom.Element;

import com.arcadsoftware.mmk.anttasks.taskdefs.rollback.IRollbackableTask;
import com.arcadsoftware.mmk.anttasks.taskdefs.rollback.impl.ArcadRollbackTask;

public abstract class AbstractRollbackableTaskContainerHelper
		extends AbstractRollbackableHelper {

	public AbstractRollbackableTaskContainerHelper() {
		super();
	}

	public AbstractRollbackableTaskContainerHelper(final IRollbackableTask task) {
		super(task);
	}

	@Override
	public Element createRollbackData(final Element e) {
		for (final Iterator i = getTasks(); i.hasNext();) {
			final Object o = i.next();
			if (o instanceof IRollbackableTask) {
				final IRollbackableTask t = (IRollbackableTask) o;
				if (t.getHelper() instanceof AbstractRollbackableTaskHelper) {
					final AbstractRollbackableTaskHelper thelper = (AbstractRollbackableTaskHelper) t.getHelper();
					thelper.createRollbackData(e);
				}
			}
		}
		return e;
	}

	@Override
	public void doBeforeExecuting() {
	}

	public abstract Iterator getTasks();

	@Override
	public boolean rollback(final ArcadRollbackTask rollbackTask, final Element e) {
		return false;
	}

}
