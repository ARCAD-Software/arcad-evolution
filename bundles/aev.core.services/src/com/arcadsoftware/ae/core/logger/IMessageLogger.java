package com.arcadsoftware.ae.core.logger;

public interface IMessageLogger {
	int LOGLVL_FATAL = 3;
	int LOGLVL_INFO = 1;
	int LOGLVL_VERBOSE = 0;
	int LOGLVL_WARNING = 2;

	void logMessage(String message, int logLevel);
}
