package com.arcadsoftware.mmk.anttasks.taskdefs.test;

import com.arcadsoftware.ae.core.logger.MessageLogger;
import com.arcadsoftware.mmk.anttasks.taskdefs.AbstractArcadAntTask;

public class TestTask extends AbstractArcadAntTask {

	@Override
	public void doExecute() {
		MessageLogger.sendInfoMessage("LM", "Voilà un message d'information");
	}

	@Override
	public void validateAttributes() {

	}

}
