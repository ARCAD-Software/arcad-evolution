/*******************************************************************************
 * Copyright (c) 2025 ARCAD Software.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     ARCAD Software - initial API and implementation
 *******************************************************************************/
package com.arcadsoftware.aev.core.classloader;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
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
		final Set<URI> urls = new HashSet<>();
		Stream.of(paths).filter(StringUtils::isNotBlank).map(Paths::get).forEach(path -> loadURLs(urls, path));
		return urls.stream().map(uri -> {
			try {
				return uri.toURL();
			}
			catch (MalformedURLException e) {
				MessageManager.addAndPrintException(e);
				return null;
			}
		})
		.filter(Objects::nonNull)
		.collect(Collectors.toSet())
		.toArray(new URL[0]);
	}

	private static void loadURLs(final Set<URI> urls, final Path path) {
		try (final Stream<Path> stream = Files.walk(path, Integer.MAX_VALUE)) {
			stream.map(Path::toFile) //
					.filter(DirectoriesClassLoader::isJarOrClass) //
					.map(File::toURI) //
					.filter(Objects::nonNull) //
					.collect(Collectors.toSet()) //
					.forEach(urls::add);
		} catch (IOException e) {
			MessageManager.addAndPrintException(e);
		}
	}

	private static boolean isJarOrClass(final File file) {
		final String name = file.getName().toLowerCase();
		return name.endsWith(".jar") || name.endsWith(".class");
	}

	public DirectoriesClassLoader(final String... paths) {
		super(getURLs(paths), ClassLoader.getSystemClassLoader());
	}
	
	public DirectoriesClassLoader(final Collection<String> paths) {
		super(getURLs(paths.toArray(new String[paths.size()])), ClassLoader.getSystemClassLoader());
	}
}
