package com.arcadsoftware.mmk.lists.managers;

import com.arcadsoftware.ae.core.logger.IMessageLogger;
import com.arcadsoftware.ae.core.utils.Utils;

public abstract class AbstractLoggedObject {
	protected IMessageLogger logger = null;

	public IMessageLogger getLogger() {
		return logger;
	}

	public void logError(final String moduleName, final String message) {
		if (logger != null) {
			logger.logMessage(message, IMessageLogger.LOGLVL_FATAL);
		}
	}

	public void logError(final String moduleName, final Throwable e) {
		logError(moduleName, e.getLocalizedMessage() + "\n" + Utils.stackTrace(e));
	}

	public void logInfo(final String moduleName, final String message) {
		if (logger != null) {
			logger.logMessage(message, IMessageLogger.LOGLVL_INFO);
		}
	}

	public void logVerbose(final String moduleName, final String message) {
		if (logger != null) {
			logger.logMessage(message, IMessageLogger.LOGLVL_VERBOSE);
		}
	}

	public void logWarning(final String moduleName, final String message) {
		if (logger != null) {
			logger.logMessage(message, IMessageLogger.LOGLVL_WARNING);
		}
	}

	public void setLogger(final IMessageLogger logger) {
		this.logger = logger;
	}
}
