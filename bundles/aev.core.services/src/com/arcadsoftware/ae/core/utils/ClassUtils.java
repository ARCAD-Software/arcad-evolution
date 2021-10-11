package com.arcadsoftware.ae.core.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;

public class ClassUtils {
	
	private ClassUtils() {
		
	}
	
	public static Object createObject(final String classname)
			throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		if (classname != null && classname.length() != 0) {
			// create the transformer
			final Class<?> transformerClass = Class.forName(classname);
			return transformerClass.newInstance();
		} else {
			throw new ClassNotFoundException(classname);
		}
	}

	public static Object createObject(final String jarFile, final String classname)
			throws ClassNotFoundException,
			InstantiationException, IllegalAccessException, IOException {
		// Get the jar file
		final File f = new File(jarFile);
		if (f.exists()) { // if exists
			// Create an array of URL based on the jarfile
			final URL[] urls = new URL[] { f.toURI().toURL() };
			// Create a new classloader using these URLs
			final URLClassLoader classLoader = new URLClassLoader(urls);
			// If the classname is correctly declared
			if (classname != null && classname.length() != 0) {
				// create the transformer
				final Class<?> transformerClass = Class.forName(classname, true, classLoader);
				return transformerClass.newInstance();
			} else {
				classLoader.close();
				throw new ClassNotFoundException(classname);
			}
		} else {
			throw new FileNotFoundException(jarFile);
		}
	}

}
