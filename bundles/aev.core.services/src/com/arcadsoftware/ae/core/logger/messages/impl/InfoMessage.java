
package com.arcadsoftware.ae.core.logger.messages.impl;

public class InfoMessage extends SimpleMessage {

	public InfoMessage(final String serviceName, final String message) {
		super(serviceName, TYPE_MSG_INFO, message);
	}

}
