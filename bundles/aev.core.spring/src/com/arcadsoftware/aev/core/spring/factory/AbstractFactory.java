package com.arcadsoftware.aev.core.spring.factory;

import java.io.File;
import java.io.IOException;

import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.arcadsoftware.ae.core.utils.Utils;

public abstract class AbstractFactory {
	protected File arcadHomeFolder = null;
	private FileSystemXmlApplicationContext ctx;

	protected AbstractFactory() {
		super();
		initialize();
	}

	protected void doAfterInitializing() {
	}

	protected void doBeforeInitializing() {
		final String arcadHome = Utils.getHomeDirectory();
		arcadHomeFolder = new File(arcadHome);
	}

	public Object getBean(final String beanId) {
		return ctx.getBean(beanId);
	}

	protected abstract String getConfigurationFile();

	protected String[] getConfigurationFiles() {
		return new String[] { getConfigurationFile() };
	}

	protected abstract String getExtensionFolder();

	protected void initialize() {
		doBeforeInitializing();

		final String extensionFolder = getExtensionFolder();
		if (extensionFolder != null) {
			try {
				Utils.setUrls(extensionFolder);
			} catch (final IOException e) {
				throw new FactoryInitializationException(getClass(), e);
			}
		}

		ctx = new FileSystemXmlApplicationContext(getConfigurationFiles());

		doAfterInitializing();
	}

}
