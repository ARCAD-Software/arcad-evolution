package com.arcadsoftware.mmk.anttasks;

import java.io.File;

import com.arcadsoftware.aev.core.spring.factory.AbstractFactory;
import com.arcadsoftware.mmk.anttasks.taskdefs.rollback.settings.RollbackSettings;

public class AntFactory extends AbstractFactory {
	public static final String MODULE_NAME= "ARCAD-ANT-TASKS";
	private static AntFactory instance = new AntFactory();
	
	@Override
	protected String getConfigurationFile() {	
		System.out.println("getConfigurationFile");
		if (arcadHomeFolder.exists()) {
			String res =new File(arcadHomeFolder,"ant-settings.xml").getAbsolutePath(); 
			return "file:"+res; 
		}
		return null;
	}

	@Override
	protected String getExtensionFolder() {
		return new File(arcadHomeFolder,"ant").getAbsolutePath();
	}

	/* (non-Javadoc)
	 * @see com.arcadsoftware.ae.core.utils.AbstractFactory#initialize()
	 */
	@Override
	protected void initialize() {
		System.out.println("initialize");
		super.initialize();
		RollbackSettings.setInstance((RollbackSettings)getBean("rollback-settings"));
	}

	/**
	 * Renvoit 
	 * @return the instance ListFactory : 
	 */
	public static AntFactory getInstance() {
		return instance;
	}

	/**
	 * Renvoit 
	 * @return the instance ListFactory : 
	 */
	public static RollbackSettings getRollbackSettings() {
		return RollbackSettings.getInstance();
	}	
	

}
