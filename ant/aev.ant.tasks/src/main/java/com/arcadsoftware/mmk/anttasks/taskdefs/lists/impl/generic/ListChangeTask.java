package com.arcadsoftware.mmk.anttasks.taskdefs.lists.impl.generic;

import org.apache.tools.ant.Task;
import org.apache.tools.ant.UnknownElement;

import com.arcadsoftware.mmk.anttasks.taskdefs.lists.impl.transform.AbstractListItemTransformerTask;
import com.arcadsoftware.mmk.lists.metadata.StoreItem;

public class ListChangeTask extends ListExecuteTask {

	@Override
	public void executeTasks(final StoreItem item) {
		for (final Object o : nestedTasks) {
			final UnknownElement element = (UnknownElement) o;
			element.maybeConfigure();
			if (element.getTask() instanceof AbstractListItemTransformerTask) {
				executeTransformerTask((AbstractListItemTransformerTask) element.getTask(), item);
			} else {
				final Task nestedTask = ((UnknownElement) o).getTask();
				nestedTask.perform();
			}
		}
	}

	public void executeTransformerTask(final AbstractListItemTransformerTask task, final StoreItem item) {
		task.setParentList(list);
		task.setItem(item);
		task.perform();
	}

}
