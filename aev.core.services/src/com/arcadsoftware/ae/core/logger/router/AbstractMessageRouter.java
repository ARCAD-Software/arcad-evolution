/*
 * Cr‚‚ le 15 mars 2007
 *
 */
package com.arcadsoftware.ae.core.logger.router;

import java.util.concurrent.locks.ReentrantLock;

import com.arcadsoftware.ae.core.logger.formatter.AbstractMessageFormatter;
import com.arcadsoftware.ae.core.logger.formatter.impl.BasicMessageFormatter;
import com.arcadsoftware.ae.core.logger.messages.AbstractMessage;
import com.arcadsoftware.ae.core.logger.messages.Messages;

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
	 * @return Renvoie formatter.
	 */
	public AbstractMessageFormatter getFormatter() {
		return formatter;
	}
	/**
	 * @param formatter formatter … d‚finir.
	 */
	public void setFormatter(AbstractMessageFormatter formatter) {
		this.formatter = formatter;
	}
}
