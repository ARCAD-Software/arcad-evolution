package com.arcadsoftware.mmk.anttasks.taskdefs.rollback.helpers;



import java.text.SimpleDateFormat;
import java.util.Date;

import com.arcadsoftware.ae.core.logger.IMessageLogger;
import com.arcadsoftware.ae.core.logger.MessageLogger;
import com.arcadsoftware.mmk.anttasks.taskdefs.rollback.IRollbackableTask;
import com.arcadsoftware.serviceprovider.transactionlogger.TransactionAction;
import com.arcadsoftware.serviceprovider.transactionlogger.logger.ArcadPCTransactionLogger;
import com.arcadsoftware.serviceprovider.transactionlogger.logger.ArcadTransactionLogger;

public class CopyFileWithLogHelper extends CopyFileHelper implements IMessageLogger {
	
	public CopyFileWithLogHelper(IRollbackableTask task) {
		super(task);
	}
	
	public void logToArcadTfrLog(String fromFile, String toFile, String host, String port, String user, String pwd, ArcadTransactionLogger logger, String localUser, String localPwd, String localPort) {
		Date now = new Date();
    	SimpleDateFormat pattern = new SimpleDateFormat("yyyyMMdd");
    	String date = pattern.format(now);
    	pattern = new SimpleDateFormat("hhmmss");
    	String time = pattern.format(now);
		TransactionAction action = new TransactionAction();
		action.setActionType("RCPY");
		action.setBackupDirectory(getRollbackDir());
		String localHost = System.getenv("COMPUTERNAME");
		action.setHost(localHost);
		action.setPort(localPort);
		action.setSourceFileName(fromFile);
		action.setTargetFilename(toFile);
		action.setTransactionId(getTransactionId());
		action.setUser(localUser);
		action.setPwd(localPwd);
		action.setTransactionDate(date);
		action.setTransactionTime(time);
		action.setLogger(this);
		try {
			logger.writeTransaction(action);
		} catch (Exception e) {
				e.printStackTrace();
		}
	}	
	
	public void logToH2TfrLog(String fromFile, String toFile, String host, String port, String user, String pwd, ArcadPCTransactionLogger h2Logger) {
		Date now = new Date();
    	SimpleDateFormat pattern = new SimpleDateFormat("yyyy-MM-dd");
    	String date = pattern.format(now);
    	pattern = new SimpleDateFormat("hh:mm:ss");
    	String time = pattern.format(now);
		TransactionAction action = new TransactionAction();
		action.setActionType("RCPY");
		action.setBackupDirectory(getRollbackDir());
		action.setHost(System.getenv("COMPUTERNAME"));
		action.setPort(port);
		action.setInfos(System.getenv("COMPUTERNAME"));
		action.setSourceFileName(fromFile);
		action.setTargetFilename(toFile);
		action.setTransactionId(getTransactionId());
		action.setUser(user);
		action.setTransactionDate(date);
		action.setTransactionTime(time);
		action.setLogger(this);
		try {
			h2Logger.writeTransaction(action);
		} catch (Exception e) {
				e.printStackTrace();
		}
	}

	public void logMessage(String message,int logLevel) {		
			switch (logLevel) {
			case IMessageLogger.LOGLVL_FATAL:
				MessageLogger.sendErrorMessage("",message);	
				break;
			case IMessageLogger.LOGLVL_WARNING:
				MessageLogger.sendWarningMessage("",message);	
				break;				
			default:
				MessageLogger.sendInfoMessage("",message);
				break;
			}
			
		}
	}
	

