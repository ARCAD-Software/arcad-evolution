package com.arcadsoftware.aev.core.spring.factory;

public class FactoryInitializationException extends RuntimeException {
	private static final long serialVersionUID = 5330604769847930848L;

	public FactoryInitializationException(final Class<?> clazz, final Exception cause) {
		super("Could not initialize factory " + clazz, cause);
	}
}
