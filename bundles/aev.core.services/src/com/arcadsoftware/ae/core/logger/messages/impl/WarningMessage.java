package com.arcadsoftware.ae.core.logger.messages.impl;

public class WarningMessage extends SimpleMessage {

    public WarningMessage(String serviceName,  String message) {
        super(serviceName, TYPE_MSG_WARNING, message);
    }

}
