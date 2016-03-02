/*
 * Created on 13 f‚vr. 2007
 *
 */
package com.arcadsoftware.ae.core.logger.messages.impl;

/**
 * @author MD
 *
 */
public class FailedMessage extends SimpleMessage {

    /**
     * @param serviceName
     * @param message
     */
    public FailedMessage(String serviceName, String message) {
        super(serviceName, TYPE_MSG_FAILED, message);
    }

}
