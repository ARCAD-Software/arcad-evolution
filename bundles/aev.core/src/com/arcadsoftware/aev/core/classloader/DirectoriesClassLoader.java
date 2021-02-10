package com.arcadsoftware.aev.core.classloader;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;

import com.arcadsoftware.aev.core.messages.MessageManager;

public class DirectoriesClassLoader extends URLClassLoader {
	private static URL[] getURLs(final String... paths) {
		final Set<URL> urls = new HashSet<>();
		Stream.of(paths).filter(StringUtils::isNotBlank).map(Paths::get).forEach(path -> loadURLs(urls, path));
		return urls.toArray(new URL[urls.size()]);
	}

	private static void loadURLs(final Set<URL> urls, final Path path) {
		try (final Stream<Path> stream = Files.walk(path, Integer.MAX_VALUE)) {
			stream.map(Path::toFile) //
					.filter(DirectoriesClassLoader::isJarOrClass) //
					.map(DirectoriesClassLoader::toURL) //
					.filter(Objects::nonNull) //
					.collect(Collectors.toSet()).forEach(urls::add);
		} catch (IOException e) {
			MessageManager.addAndPrintException(e);
		}
	}

	private static boolean isJarOrClass(final File file) {
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

	public DirectoriesClassLoader(final String... paths) {
		super(getURLs(paths), ClassLoader.getSystemClassLoader());
	}
	
	public DirectoriesClassLoader(final Collection<String> paths) {
		super(getURLs(paths.toArray(new String[paths.size()])), ClassLoader.getSystemClassLoader());
	}
}
