package com.arcadsoftware.mmk.anttasks.taskdefs.misc;

import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;

import com.arcadsoftware.ae.core.logger.messages.AbstractMessage;
import com.arcadsoftware.ae.core.logger.messages.Messages;
import com.arcadsoftware.ae.core.logger.router.AbstractMessageRouter;

public class AntMessageRouter extends AbstractMessageRouter {
	Project project;
	Task currentTask;	
	
	public AntMessageRouter (Project project){
		this.project = project;
		formatter = new AntMessageFormatter();
	}

	@Override
	protected void doFinalize() {
	}

	@Override
	protected void doInitialize() {
	}

	@Override
	protected void doIntercept() {
		//Traitement du dernier message re‡u
		AbstractMessage m = messages.messageAt(messages.messageCount()-1);
		if (project!=null) {
			if (getFormatter()!=null) {
				Messages currentMessages = new Messages();
				currentMessages.add(m);
				String s = getFormatter().format(currentMessages);
				int level = Project.MSG_ERR;
				if (m.isErrorMessage())
					level = Project.MSG_ERR;
				else if (m.isWarningMessage())
					level = Project.MSG_WARN;
				else 
					level = Project.MSG_INFO;
				if (currentTask!=null)
					project.log(currentTask,s,level);
				else
					project.log(s,level);
			}
		}
	}

	/**
	 * Renvoit 
	 * @return the currentTask Task : 
	 */
	public Task getCurrentTask() {
		return currentTask;
	}

	/**
	 * @param currentTask the currentTask to set
	 */
	public void setCurrentTask(Task currentTask) {
		this.currentTask = currentTask;
	}

}
