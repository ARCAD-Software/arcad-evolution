package com.arcadsoftware.aev.core.spring.factory;


import java.io.File;

import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.arcadsoftware.ae.core.utils.Utils;


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
		
		String extensionFolder = getExtensionFolder(); 
		if (extensionFolder != null) {
			Utils.setUrls(extensionFolder);
		}
		
		ctx = new FileSystemXmlApplicationContext(getConfigurationFiles());
		
		doAfterInitializing();
	}
	
	protected void doAfterInitializing() {}
	
	protected String[] getConfigurationFiles() {
		return new String[]{getConfigurationFile()};
	}
	protected abstract String getExtensionFolder();
	protected abstract String getConfigurationFile();

	public Object getBean(String beanId) {		
		return ctx.getBean(beanId);
	}


}
