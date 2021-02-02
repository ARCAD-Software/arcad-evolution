package com.arcadsoftware.ae.core.logger.messages.impl;

public class FailedMessage extends SimpleMessage {
	public FailedMessage(final String serviceName, final String message) {
		super(serviceName, TYPE_MSG_FAILED, message);
	}
}
