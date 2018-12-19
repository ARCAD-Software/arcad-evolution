/*
 * Cr�� le 15 mars 2007
 *
 */
package com.arcadsoftware.ae.core.logger.router;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.concurrent.locks.ReentrantLock;

import com.arcadsoftware.ae.core.logger.formatter.AbstractMessageFormatter;
import com.arcadsoftware.ae.core.logger.formatter.impl.BasicMessageFormatter;
import com.arcadsoftware.ae.core.logger.messages.AbstractMessage;
import com.arcadsoftware.ae.core.logger.messages.Messages;
import com.arcadsoftware.ae.core.logger.messages.impl.ErrorMessage;
import com.arcadsoftware.ae.core.utils.Utils;

/**
 * @author MD
 *
 */
public abstract class AbstractMessageRouter {
	protected Messages messages = new Messages();
	protected AbstractMessageFormatter formatter = null;
	
	protected String data = null;
	
	//<FM number="2016/00133" version="10.06.00" date="1 Mar 2016" user="SJU">
	protected final ReentrantLock interceptionLock = new ReentrantLock();	
	//</FM>
	protected boolean standardOutputCaught;
	
	public AbstractMessageRouter() {
		catchStandardOutput();
	}
	
	public void initializeMessages() {
		messages.clear();
		data=null;		
		doInitialize();
	}
	public void interceptMessage(AbstractMessage message) {
		interceptionLock.lock();
		
		messages.add(message);		
		doIntercept();
		
		interceptionLock.unlock();
	}
	public void finalizeMessages() {
		doFinalize();
	}

	public String getData(){
		return getData(false);
	}
		
	public String getData(boolean refresh){
		if ((data==null) || (refresh)){
			if (formatter==null)
				initMessageFormatter();			
			data = formatter.format(messages);
		}
		return data;
	}
	
	protected void initMessageFormatter(){
		formatter = new BasicMessageFormatter();
	}
	
	protected abstract void doInitialize();
	protected abstract void doIntercept();	
	protected abstract void doFinalize();	
	
	/**
	 * Override to NOT intercept messages directed to the standard/error output.
	 */
	protected boolean canCatchStandardOutput(){
		return true;
	}
	
	/**
	 * @return Renvoie formatter.
	 */
	public AbstractMessageFormatter getFormatter() {
		return formatter;
	}
	/**
	 * @param formatter formatter � d�finir.
	 */
	public void setFormatter(AbstractMessageFormatter formatter) {
		this.formatter = formatter;
	}
	
	protected void catchStandardOutput(){
		if(canCatchStandardOutput() && !this.standardOutputCaught){
	    	System.setOut(new DualStream(System.out, false));
	    	System.setErr(new DualStream(System.err, true));
	    	this.standardOutputCaught = true;
		}
    }
    
    protected class DualStream extends PrintStream{
    	protected boolean canLog;
    	protected boolean isErrorStream;
    	protected File standardLogFile;
    	protected File errorLogFile;
    	
    	public DualStream(OutputStream stream1, boolean isErrorStream) {
    		super(stream1);
    		this.isErrorStream = isErrorStream;
    		try {
				this.standardLogFile = new File(Utils.getHomeDirectory() + "/logs/service_providers_stdout.log")
										.getCanonicalFile();
				this.errorLogFile = new File(Utils.getHomeDirectory() + "/logs/service_providers_stderr.log")
										.getCanonicalFile();
			}
			catch (IOException e) {
				interceptMessage(new ErrorMessage("DualStream", "Could not initialize DualStream output logs:" + e.getLocalizedMessage()));
				e.printStackTrace();
			}
    		canLog = (standardLogFile != null && errorLogFile != null);
		}
    	
    	@Override
    	public void write(byte[] buf, int off, int len) {
    		super.write(buf, off, len);
    		String message = new String(buf, off, len).trim();
    		if(message.length() > 0){
	    		if(isErrorStream){
	    			writeLine(getLogFile(errorLogFile), message);
	    		}
	    		else{
	    			writeLine(getLogFile(standardLogFile), message);
	    		}
    		}
    	}
    	
    	protected void writeLine(File logFile, String message) {
    		FileOutputStream fos = null;
    		message += "\n";
    		try{
    			fos = new FileOutputStream(logFile, true);
    			fos.write(message.getBytes("UTF-8"));
    			fos.flush();
    		}
			catch (Exception e) {}
    		finally{
    			if(fos != null)
					try {fos.close();} catch (IOException e) {}
    		}
		}

		protected File getLogFile(File logFile) {
    		try {
				if(logFile.exists()){
					if(logFile.length() > 2097152){
						renameLocalLogFile(logFile, 1);
						logFile.createNewFile();
					}
				}
				else{
					logFile.createNewFile();
				}
			}
			catch (IOException e) {}
    		
    		return logFile;
    	}
    	
    	protected void renameLocalLogFile(File localLogFile, int number) {
    		String path = localLogFile.getParent();
    		String name = localLogFile.getName();
    		String extension = "";
    		int lastPoint = name.lastIndexOf(".");
    		if(lastPoint > 0) {
    			extension = name.substring(lastPoint);
    			name = name.substring(0, lastPoint) + "_";			 
    		}
    		File backLocalLogFile = new	File(path + File.separator + name + String.format("%1$03d", number) + extension);
    		if(backLocalLogFile.exists()){
    			renameLocalLogFile(localLogFile, number + 1);
    		}
    		localLogFile.renameTo(backLocalLogFile);
    	}
    }
}