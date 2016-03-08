/*
 * Cr‚‚ le 16 nov. 07
 *
 * TODO Pour changer le modŠle de ce fichier g‚n‚r‚, allez … :
 * Fenˆtre - Pr‚f‚rences - Java - Style de code - ModŠles de code
 */
package com.arcadsoftware.mmk.anttasks.arcad.listener;

import java.util.ArrayList;

import org.apache.tools.ant.BuildEvent;
import org.apache.tools.ant.BuildListener;

import com.arcadsoftware.ae.core.utils.Utils;

public abstract class AbstractArcadBuildListener implements BuildListener {
	
	private static final String INIT_TARGET="::Initialization";
	
	private String currentTarget=null;
	private String currentTask=null;
	
	ArrayList<String> taskMessages;
	
	private int logLevel = 0;
	
	
	public AbstractArcadBuildListener(){
		taskMessages=new ArrayList<String>();
	}
	
	
	public void buildFinished(BuildEvent event) {
	}

	public void buildStarted(BuildEvent event) {
	}

	public void messageLogged(BuildEvent event) {
		if (event.getPriority()<=logLevel) {
			StringBuffer b = new StringBuffer(event.getMessage());
			if (event.getException()!=null) {
				b.append("\n");
				b.append(Utils.stackTrace(event.getException()));
			}
			taskMessages.add(b.toString());			
		}
	}

	public void targetStarted(BuildEvent event) {
		if (event.getTarget()!=null) {
			if (event.getTarget().getName().equals(""))				
				currentTarget=INIT_TARGET;
			else
				currentTarget=event.getTarget().getName();
		}
	}
	
	public void targetFinished(BuildEvent event) {

	}

	public void taskStarted(BuildEvent event) {
		currentTask=event.getTask().getTaskName();
	}

	public void taskFinished(BuildEvent event) {
		sendMessage(currentTarget,currentTask,(String[])taskMessages.toArray());
		taskMessages.clear();
	}

	protected abstract void sendMessage(String targetName,String taskName,String[] messages);
	

}
