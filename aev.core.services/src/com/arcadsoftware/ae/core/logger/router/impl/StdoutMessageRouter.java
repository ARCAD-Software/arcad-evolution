/*
 * Cr‚‚ le 12 mars 2007
 *

 */
package com.arcadsoftware.ae.core.logger.router.impl;


/**
 * MessageRouter renvoyant les messages vers la sortie standard.<br>
 * @author MD

 */
public class StdoutMessageRouter extends MessageRouterAdapter {

	
	
	/* (non-Javadoc)
	 * @see com.arcadsoftware.serviceprovider.message.router.AbstractMessageRouter#doFinalize()
	 */
	protected void doFinalize() {
		System.out.println(getData());
	}
	
}
