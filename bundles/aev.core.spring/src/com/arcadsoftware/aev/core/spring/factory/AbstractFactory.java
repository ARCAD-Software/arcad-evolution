package com.arcadsoftware.aev.core.spring.factory;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

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
				ctx = new FileSystemXmlApplicationContext(getConfigurationFiles());
				ctx.setClassLoader(new URLClassLoader(getJarURLs().toArray(new URL[0]), ClassLoader.getSystemClassLoader()));
			} catch (final IOException e) {
				throw new FactoryInitializationException(getClass(), e);
			}
		}
		
		doAfterInitializing();
	}

	private List<URL> getJarURLs() throws MalformedURLException {
		final File[] files = Utils.getFiles(new File(getExtensionFolder()), true);
		final List<URL> urls = new ArrayList<>(files.length);
		for(final File file : files) {
			urls.add(file.toURI().toURL());	
		}
		
		return urls;
	}
}
