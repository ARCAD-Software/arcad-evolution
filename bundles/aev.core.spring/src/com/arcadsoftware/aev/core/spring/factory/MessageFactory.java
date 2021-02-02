package com.arcadsoftware.aev.core.spring.factory;

import java.io.File;

import com.arcadsoftware.ae.core.logger.MessageLogger;

public class MessageFactory extends AbstractFactory {
	private static MessageFactory instance = new MessageFactory();

	/**
	 * Renvoit
	 * 
	 * @return the instance ListFactory :
	 */
	public static MessageFactory getInstance() {
		return instance;
	}

	@Override
	protected String getConfigurationFile() {
		if (arcadHomeFolder.exists()) {
			final String res = new File(arcadHomeFolder, "messages-settings.xml").getAbsolutePath();
			return "file:" + res;
		}
		return null;
	}

	@Override
	protected String getExtensionFolder() {
		return new File(arcadHomeFolder, "messages").getAbsolutePath();
	}

	/*
	 * (non-Javadoc)
	 * @see com.arcadsoftware.ae.core.utils.AbstractFactory#initialize()
	 */
	@Override
	protected void initialize() {
		super.initialize();
		// Chargement du MessageSender
		MessageLogger.setInstance((MessageLogger) getBean("MessageLogger"));
	}

}
