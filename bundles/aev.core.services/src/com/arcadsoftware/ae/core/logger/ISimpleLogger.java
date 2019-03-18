package com.arcadsoftware.ae.core.logger;

public interface ISimpleLogger {
	public void logError(String message);
	public void logInfo(String message);	
	public void logWarning(String message);	
	public void logVerbose(String message);	
}
