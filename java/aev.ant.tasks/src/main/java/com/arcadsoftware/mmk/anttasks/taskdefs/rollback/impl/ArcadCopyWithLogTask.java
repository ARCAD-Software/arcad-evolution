package com.arcadsoftware.mmk.anttasks.taskdefs.rollback.impl;

import org.apache.tools.ant.Task;

import com.arcadsoftware.ae.core.logger.IMessageLogger;
import com.arcadsoftware.ae.core.logger.MessageLogger;


import com.arcadsoftware.mmk.anttasks.taskdefs.rollback.AbstractArcadCopyWithLogTask;
import com.arcadsoftware.mmk.anttasks.taskdefs.rollback.IRollbackableTask;
import com.arcadsoftware.mmk.anttasks.taskdefs.rollback.helpers.AbstractRollbackableHelper;
import com.arcadsoftware.mmk.anttasks.taskdefs.rollback.helpers.CopyFileWithLogHelper;
import com.arcadsoftware.mmk.anttasks.taskdefs.rollback.helpers.ERollbackStringConstants;

public class ArcadCopyWithLogTask extends AbstractArcadCopyWithLogTask  
implements IRollbackableTask, IMessageLogger {
	
	
	private static final String VERSION= "1.0.0.0";
	private boolean inTransaction = false;
	
	CopyFileWithLogHelper helper;
	
	public ArcadCopyWithLogTask(){
		super();
		helper = new CopyFileWithLogHelper(this);
	}
	
	@Override
	public void doBeforeExecuting() {	
		helper.doBeforeExecuting();
	}

	@Override
	public void doAfterExecuting() {
		helper.doAfterExecuting();
	}

	@Override
	public void doBeforeCopying(String fromFile, String toFile,boolean overwrite) {
		MessageLogger.sendInfoMessage("COPY TASK","Copy "+fromFile+" to "+toFile);
		helper.backupFile(fromFile,toFile,overwrite);		
	}

	@Override
	public void doAfterCopying(String fromFile, String toFile) {
		helper.logToArcadTfrLog(fromFile, toFile, host, port, user, pwd, logger, localUser, localPwd, localPort);
		helper.logToH2TfrLog(fromFile, toFile, host, port, user, pwd, h2Logger);
	}

	public String getActionCode() {
		return ERollbackStringConstants.RB_ACTIONCODE_COPY.getValue();
	}


	public void setRollbackDir(String rollbackDir) {
		helper.setRollbackDir(rollbackDir);
	}

	public void setRollbackIdProperty(String rollbackIdProperty) {
		helper.setRollbackIdProperty(rollbackIdProperty);		
	}

	public void setRollbackId(String rollbackId) {
		helper.setRollbackId(rollbackId);		
	}
	
	public String getVersion() {
		return VERSION;
	}

	public AbstractRollbackableHelper getHelper(){
		return helper;
	}

	public boolean isInTransaction() {
		return inTransaction;
	}

	
	//TODO [ANT] changer le nom de la procédure pour éviter la prise en charge bean
	public void setInTransaction(boolean inTransaction) {
		this.inTransaction = inTransaction;
	}

	public Task getTask() {
		return this;
	}
	
	public void setHost(String host) {
		this.host = host;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public void setLocalUser(String localUser) {
		this.localUser = localUser;
	}
	
	public void setLocalPwd(String localPwd) {
		this.localPwd = localPwd;
	}
	
	public void setLocalPort(String localPort) {
		this.localPort = localPort;
	}
	
	public void setCopyWithLog(boolean copyWithLog) {
		this.copyWithLog = copyWithLog;
	}
	
	public void setInstance(String instance) {
		this.instance =instance;
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

