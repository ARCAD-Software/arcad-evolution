package com.arcadsoftware.ae.core.logger.messages.impl;

public class SucceedMessage extends SimpleMessage {
    public SucceedMessage(String serviceName, String message) {
        super(serviceName, TYPE_MSG_SUCCEED, message);
    }

}
