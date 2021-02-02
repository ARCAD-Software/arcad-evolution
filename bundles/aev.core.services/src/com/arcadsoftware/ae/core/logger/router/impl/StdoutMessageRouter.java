package com.arcadsoftware.ae.core.logger.router.impl;

public class StdoutMessageRouter extends MessageRouterAdapter {

	@Override
	protected boolean canCatchStandardOutput() {
		return false;
	}

	@Override
	protected void doFinalize() {
		System.out.println(getData());
	}
}
