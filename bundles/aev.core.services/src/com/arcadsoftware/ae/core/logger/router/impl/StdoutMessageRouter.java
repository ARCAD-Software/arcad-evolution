package com.arcadsoftware.ae.core.logger.router.impl;

public class StdoutMessageRouter extends MessageRouterAdapter {
	@Override
	protected void doFinalize() {
		System.out.println(getData());
	}
}
