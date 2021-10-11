package com.arcadsoftware.ae.core.logger.router;

import java.util.concurrent.locks.ReentrantLock;

import com.arcadsoftware.ae.core.logger.formatter.AbstractMessageFormatter;
import com.arcadsoftware.ae.core.logger.formatter.impl.BasicMessageFormatter;
import com.arcadsoftware.ae.core.logger.messages.AbstractMessage;
import com.arcadsoftware.ae.core.logger.messages.Messages;

public abstract class AbstractMessageRouter {
	protected String data = null;

	protected AbstractMessageFormatter formatter = null;

	// <FM number="2016/00133" version="10.06.00" date="1 Mar 2016" user="SJU">
	protected final ReentrantLock interceptionLock = new ReentrantLock();
	protected Messages messages = new Messages();

	// </FM>
	protected boolean standardOutputCaught;

	protected abstract void doFinalize();

	protected abstract void doInitialize();

	protected abstract void doIntercept();

	public void finalizeMessages() {
		doFinalize();
	}

	public String getData() {
		return getData(false);
	}

	public String getData(final boolean refresh) {
		if (data == null || refresh) {
			if (formatter == null) {
				initMessageFormatter();
			}
			data = formatter.format(messages);
		}
		return data;
	}

	/**
	 * @return Renvoie formatter.
	 */
	public AbstractMessageFormatter getFormatter() {
		return formatter;
	}

	public void initializeMessages() {
		messages.clear();
		data = null;
		doInitialize();
	}

	protected void initMessageFormatter() {
		formatter = new BasicMessageFormatter();
	}

	public void interceptMessage(final AbstractMessage message) {
		interceptionLock.lock();

		messages.add(message);
		doIntercept();

		interceptionLock.unlock();
	}

	public void setFormatter(final AbstractMessageFormatter formatter) {
		this.formatter = formatter;
	}
}
