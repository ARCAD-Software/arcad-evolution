package com.arcadsoftware.ae.core.logger.messages.impl;

public class FailedMessage extends SimpleMessage {
    public FailedMessage(String serviceName, String message) {
        super(serviceName, TYPE_MSG_FAILED, message);
    }
}
