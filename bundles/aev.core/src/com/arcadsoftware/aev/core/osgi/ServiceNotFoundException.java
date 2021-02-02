package com.arcadsoftware.aev.core.osgi;

public class ServiceNotFoundException extends RuntimeException {

	private static final long serialVersionUID = -7895535899805707572L;

	public ServiceNotFoundException(final Class<?> serviceClass) {
		super("Implementation of service " + serviceClass + " could not be found.");
	}
}
