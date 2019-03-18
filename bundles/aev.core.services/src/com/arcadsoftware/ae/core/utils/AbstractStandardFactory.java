package com.arcadsoftware.ae.core.utils;

import java.io.File;



public abstract class AbstractStandardFactory extends AbstractFactory {
	@Override
	public String getConfigurationFile() {				
		if (arcadHomeFolder.exists()){
			String res =  new File(getExtensionFolder(),getConfigurationFileName()).getAbsolutePath();
			return "file:"+res;
	  }
		return null;
	}
	
	@Override
	public String getExtensionFolder() {
		return new File(arcadHomeFolder,getConfigurationDirectory()).getAbsolutePath();
	}

	protected abstract String getConfigurationDirectory();
	protected abstract String getConfigurationFileName();
}
