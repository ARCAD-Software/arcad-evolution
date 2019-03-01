package com.arcadsoftware.ae.core.utils;


import java.io.File;

import org.springframework.context.support.FileSystemXmlApplicationContext;

public abstract class AbstractFactory {
	protected static File arcadHomeFolder = null;
	private FileSystemXmlApplicationContext ctx;
	
	protected AbstractFactory() {		
		super();
		initialize();
	}	
	
	protected void doBeforeInitializing() {
		String arcadHome  = Utils.getHomeDirectory();
		arcadHomeFolder = new File(arcadHome);	
	}		
	
	protected void initialize(){
		doBeforeInitializing();
		if (getExtensionFolder()!=null) {
			Utils.setUrls(getExtensionFolder());
		}			
		ctx = new FileSystemXmlApplicationContext(getConfigurationFiles());
	}
	
	protected String[] getConfigurationFiles() {
		return new String[]{getConfigurationFile()};
	}
	protected abstract String getExtensionFolder();
	protected abstract String getConfigurationFile();

	public Object getBean(String beanId) {		
		return ctx.getBean(beanId);
	}


}
