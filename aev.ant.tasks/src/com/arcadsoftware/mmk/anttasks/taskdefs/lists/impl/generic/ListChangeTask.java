package com.arcadsoftware.mmk.anttasks.taskdefs.lists.impl.generic;

import java.util.Iterator;

import org.apache.tools.ant.Task;
import org.apache.tools.ant.UnknownElement;

import com.arcadsoftware.mmk.anttasks.taskdefs.lists.impl.transform.AbstractListItemTransformerTask;
import com.arcadsoftware.mmk.lists.metadata.StoreItem;


public class ListChangeTask extends ListExecuteTask {

	@Override
	public void executeTasks(StoreItem item) {
        for (Iterator i = nestedTasks.iterator(); i.hasNext();) {
        	Object o = i.next();
        	UnknownElement element = (UnknownElement)o;
        	element.maybeConfigure();        	
        	if (element.getTask() instanceof AbstractListItemTransformerTask){
        		executeTransformerTask((AbstractListItemTransformerTask)element.getTask(),item);
        	} else {
        		Task nestedTask = (Task)((UnknownElement)o).getTask();
        		nestedTask.perform();
        	}
        }	
	}

	public void executeTransformerTask(AbstractListItemTransformerTask task,StoreItem item){
		task.setParentList(list);
		task.setItem(item);
		task.perform();		
	}
	
	
}
