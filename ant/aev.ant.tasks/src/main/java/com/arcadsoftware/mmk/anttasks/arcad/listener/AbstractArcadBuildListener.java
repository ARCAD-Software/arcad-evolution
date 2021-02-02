package com.arcadsoftware.mmk.anttasks.arcad.listener;

import java.util.ArrayList;

import org.apache.tools.ant.BuildEvent;
import org.apache.tools.ant.BuildListener;

import com.arcadsoftware.ae.core.utils.Utils;

public abstract class AbstractArcadBuildListener implements BuildListener {

	private static final String INIT_TARGET = "::Initialization";

	private static final int LOG_LEVEL = 0;
	private String currentTarget = null;

	private String currentTask = null;

	ArrayList<String> taskMessages;

	public AbstractArcadBuildListener() {
		taskMessages = new ArrayList<>();
	}

	@Override
	public void buildFinished(final BuildEvent event) {
	}

	@Override
	public void buildStarted(final BuildEvent event) {
	}

	@Override
	public void messageLogged(final BuildEvent event) {
		if (event.getPriority() <= LOG_LEVEL) {
			final StringBuilder b = new StringBuilder(event.getMessage());
			if (event.getException() != null) {
				b.append("\n");
				b.append(Utils.stackTrace(event.getException()));
			}
			taskMessages.add(b.toString());
		}
	}

	protected abstract void sendMessage(String targetName, String taskName, String[] messages);

	@Override
	public void targetFinished(final BuildEvent event) {

	}

	@Override
	public void targetStarted(final BuildEvent event) {
		if (event.getTarget() != null) {
			if (event.getTarget().getName().equals("")) {
				currentTarget = INIT_TARGET;
			} else {
				currentTarget = event.getTarget().getName();
			}
		}
	}

	@Override
	public void taskFinished(final BuildEvent event) {
		sendMessage(currentTarget, currentTask, taskMessages.toArray(new String[taskMessages.size()]));
		taskMessages.clear();
	}

	@Override
	public void taskStarted(final BuildEvent event) {
		currentTask = event.getTask().getTaskName();
	}

}
