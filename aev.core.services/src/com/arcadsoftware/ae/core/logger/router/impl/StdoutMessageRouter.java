/*
 * Cr‚‚ le 12 mars 2007
 *

 */
package com.arcadsoftware.ae.core.logger.router.impl;

public class StdoutMessageRouter extends MessageRouterAdapter {
	
	@Override
	protected boolean canCatchStandardOutput() {
		return false;
	}
	
	protected void doFinalize() {
		System.out.println(getData());
	}	
}
