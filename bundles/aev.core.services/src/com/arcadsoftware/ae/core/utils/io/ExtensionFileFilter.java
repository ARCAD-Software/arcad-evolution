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
package com.arcadsoftware.ae.core.utils.io;

import java.io.File;
import java.util.Enumeration;
import java.util.Hashtable;

public class ExtensionFileFilter {
	private boolean allowAll = false;
	private final boolean allowDirectories;
	private final Hashtable<String, Boolean> extensionsTable = new Hashtable<>();

	public ExtensionFileFilter() {
		this(true);
	}

	public ExtensionFileFilter(final boolean allowDirectories) {
		this.allowDirectories = allowDirectories;
	}

	public boolean accept(final File file) {
		if (file.isDirectory()) {
			return allowDirectories;
		}
		if (allowAll) {
			return true;
		}
		final String name = file.getName();
		final int dotIndex = name.lastIndexOf('.');
		if (dotIndex == -1 || dotIndex == name.length() - 1) {
			return false;
		}
		// Recherche avaec matchinf exact
		final String extension = name.substring(dotIndex + 1);
		if (extensionsTable.containsKey(extension)) {
			return true;
		}
		// Recherche en ignorant le caseSensitive
		final Enumeration<String> keys = extensionsTable.keys();
		while (keys.hasMoreElements()) {
			final String possibleExtension = keys.nextElement();
			final Boolean caseFlag = extensionsTable.get(possibleExtension);
			if (caseFlag != null &&
					caseFlag.equals(Boolean.FALSE) &&
					possibleExtension.equalsIgnoreCase(extension)) {
				return true;
			}
		}
		return false;
	}

	public void addExtension(String extension, final boolean caseInsensitive) {
		if (caseInsensitive) {
			extension = extension.toLowerCase();
		}
		if (!extensionsTable.containsKey(extension)) {
			extensionsTable.put(extension, new Boolean(caseInsensitive));
			if (extension.equals("*") || extension.equals("*.*") || extension.equals(".*")) {
				allowAll = true;
			}
		}
	}

}
