package com.arcadsoftware.mmk.lists.managers;

import com.arcadsoftware.ae.core.logger.IMessageLogger;
import com.arcadsoftware.ae.core.utils.Utils;

public abstract class AbstractLoggedObject {
	protected IMessageLogger logger = null;

	public void setLogger(IMessageLogger logger) {
		this.logger =logger;
	}

	
	public void logError(String moduleName,String message) {
		if (logger!=null) {
			logger.logMessage(message, IMessageLogger.LOGLVL_FATAL);
		}
	}

	
	public void logInfo(String moduleName,String message) {
		if (logger!=null) {
			logger.logMessage(message, IMessageLogger.LOGLVL_INFO);
		}
	}

	
	public void logWarning(String moduleName,String message) {
		if (logger!=null) {
			logger.logMessage(message, IMessageLogger.LOGLVL_WARNING);
		}
	}

	
	public void logVerbose(String moduleName,String message) {
		if (logger!=null) {
			logger.logMessage(message, IMessageLogger.LOGLVL_VERBOSE);
		}
	}

	public void logError(String moduleName,Throwable e) {
		logError(moduleName,e.getLocalizedMessage()+"\n"+Utils.stackTrace(e));		
	}	
	
	public IMessageLogger getLogger() {
		return logger;
	}
}
