package com.arcadsoftware.ae.core.logger.messages.impl;

public class SucceedMessage extends SimpleMessage {
	public SucceedMessage(final String serviceName, final String message) {
		super(serviceName, TYPE_MSG_SUCCEED, message);
	}

}
