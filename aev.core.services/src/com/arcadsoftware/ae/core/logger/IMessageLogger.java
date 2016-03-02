package com.arcadsoftware.ae.core.logger;

public interface IMessageLogger {
  public static final int LOGLVL_VERBOSE = 0;
  public static final int LOGLVL_INFO = 1;	  
  public static final int LOGLVL_WARNING = 2;	  
  public static final int LOGLVL_FATAL = 3;
  public void logMessage(String message,int logLevel);
}
