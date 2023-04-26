package com.arcadsoftware.mmk.anttasks.taskdefs;


import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

import com.arcadsoftware.ae.core.logger.MessageFactory;
import com.arcadsoftware.ae.core.logger.MessageLogger;
import com.arcadsoftware.mmk.anttasks.taskdefs.misc.AntMessageRouter;

public abstract class AbstractArcadAntTask extends Task {
	private AntMessageRouter router;
	
	public abstract void validateAttributes();
	public abstract void doExecute() throws BuildException;	
	
    public void execute() throws BuildException {  
    	//System.out.println("execute");
    	/* Comme les taches ant peuvent �tre appel�es par l'agent d'ex�cution
    	un MessageLogger a d�j� �t� utilis�. Dans ce cas, on utilise celui
    	qui a d�j� �t� instanci�. Dans le cas contraire (isLoaded = false) on
    	appelle la factory pour la cr�ation.
    	*/
    	if (!MessageLogger.isLoaded()) {
    		//MessageLoader n'apas d�j� �t� instanci�
    		MessageFactory.getInstance();
    	}
    	MessageLogger.initializeMessage();
    	
    	router = new AntMessageRouter(getProject());
    	router.setCurrentTask(this);    	
    	MessageLogger.getInstance().getMessageRouters().add(router);    	
    	try {
	    	validateAttributes();
	    	doBeforeExecuting();
	    	doExecute();
    	} finally {
    		MessageLogger.finalizeMessage();
    		MessageLogger.getInstance().getMessageRouters().remove(router);
    	}
    }	
	
	protected void doBeforeExecuting() {
		
	}
}
