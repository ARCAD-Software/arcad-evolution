package com.arcadsoftware.ae.core.logger;

public interface ISimpleLogger {
	void logError(String message);

	void logInfo(String message);

	void logVerbose(String message);

	void logWarning(String message);
}
