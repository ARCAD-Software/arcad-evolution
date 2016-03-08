package com.arcadsoftware.mmk.anttasks.taskdefs.test;

import org.apache.tools.ant.BuildException;

import com.arcadsoftware.ae.core.logger.MessageLogger;
import com.arcadsoftware.mmk.anttasks.taskdefs.AbstractArcadAntTask;

public class TestTask extends AbstractArcadAntTask {

	@Override
	public void doExecute() throws BuildException {		
		MessageLogger.sendInfoMessage("LM","Voil… un message d'information");
	}

	@Override
	public void validateAttributes() {


	}

}
