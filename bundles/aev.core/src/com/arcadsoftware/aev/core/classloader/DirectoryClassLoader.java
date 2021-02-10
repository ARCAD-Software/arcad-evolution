package com.arcadsoftware.aev.core.classloader;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.arcadsoftware.aev.core.messages.MessageManager;

public class DirectoryClassLoader extends URLClassLoader {		
	private static URL[] getURLs(final String path){
		try (final Stream<Path> stream = Files.walk(Paths.get(path), Integer.MAX_VALUE)){
			return stream.map(Path::toFile) //
						.filter(DirectoryClassLoader::isJarOrClass) //
						.map(DirectoryClassLoader::toURL) //
						.filter(Objects::nonNull) //
						.collect(Collectors.toSet())
						.toArray(new URL[0]);
		} catch (IOException e) {
			MessageManager.addAndPrintException(e);
		}		
		
		return new URL[0];
	}
	
	private static boolean isJarOrClass(final File file){
		final String name = file.getName().toLowerCase();
		return name.endsWith(".jar") || name.endsWith(".class");
	}
	
	private static URL toURL(final File file) {
		try {
			return file.toURI().toURL();
		} catch (MalformedURLException e) {
			return null;
		}
	}
	
	public DirectoryClassLoader(final String path) {
		super(getURLs(path), ClassLoader.getSystemClassLoader());
	}
}
