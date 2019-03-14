package com.arcadsoftware.ae.core.logger;

import java.io.File;

import com.arcadsoftware.ae.core.utils.AbstractFactory;

public class MessageFactory extends AbstractFactory {
	private static MessageFactory instance = new MessageFactory();
	
	@Override
	protected String getConfigurationFile() {				
		if (arcadHomeFolder.exists()) {
			String res =  new File(arcadHomeFolder,"messages-settings.xml").getAbsolutePath();
			return "file:"+res;
		}
		return null;
	}

	@Override
	protected String getExtensionFolder() {
		return new File(arcadHomeFolder,"messages").getAbsolutePath();
	}

	/* (non-Javadoc)
	 * @see com.arcadsoftware.ae.core.utils.AbstractFactory#initialize()
	 */
	@Override
	protected void initialize() {
		super.initialize();
		//Chargement du MessageSender
		MessageLogger.setInstance( (MessageLogger)this.getBean("MessageLogger"));
	}

	/**
	 * Renvoit 
	 * @return the instance ListFactory : 
	 */
	public static MessageFactory getInstance() {
		return instance;
	}

}
