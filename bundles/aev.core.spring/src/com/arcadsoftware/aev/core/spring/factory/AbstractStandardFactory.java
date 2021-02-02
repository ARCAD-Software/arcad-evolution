package com.arcadsoftware.aev.core.spring.factory;

import java.io.File;

public abstract class AbstractStandardFactory extends AbstractFactory {
	protected abstract String getConfigurationDirectory();

	@Override
	public String getConfigurationFile() {
		if (arcadHomeFolder.exists()) {
			final String res = new File(getExtensionFolder(), getConfigurationFileName()).getAbsolutePath();
			return "file:" + res;
		}
		return null;
	}

	protected abstract String getConfigurationFileName();

	@Override
	public String getExtensionFolder() {
		return new File(arcadHomeFolder, getConfigurationDirectory()).getAbsolutePath();
	}
}
