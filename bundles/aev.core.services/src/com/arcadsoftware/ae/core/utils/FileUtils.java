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
package com.arcadsoftware.ae.core.utils;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class FileUtils {
	private FileUtils() {	
		
	}
	
	public static boolean copyFile(final File sourceFile, final File destFile, final boolean keepModificationDate) {
		boolean result;
		try {
			if (destFile.exists() && destFile.isFile()) {
				Files.delete(destFile.toPath());
			}
		}
		catch (final IOException ioe) {
			result = false;
		}
		final File parent = destFile.getParentFile();
		if (!parent.exists()) {
			parent.mkdirs();
		}

		try (
			FileInputStream	in = new FileInputStream(sourceFile);
			FileOutputStream out = new FileOutputStream(destFile);
		){
			final byte[] buffer = new byte[64 * 1024];
			int count = 0;
			do {
				out.write(buffer, 0, count);
				count = in.read(buffer, 0, buffer.length);
			} while (count != -1);
			
			if (keepModificationDate) {
				keepModificationDate(sourceFile, destFile);
			}
			result = true;
		} catch (final IOException ioe) {
			result = false;
		}

		return result;
	}

	public static boolean copyFile(final String source, final String destination, final boolean keepModificationDate) {
		final File destFile = new File(destination);
		final File sourceFile = new File(source);
		return copyFile(sourceFile, destFile, keepModificationDate);
	}

	public static boolean duplicate(final String sourceFolder, final String targetFolder, final boolean structureOnly) {
		final File targetDirectory = new File(targetFolder);
		final File sourceDirectory = new File(sourceFolder);
		if (!targetDirectory.exists() && !targetDirectory.mkdirs()) {
			return false;
		}

		FileFilter filter = null;
		if (structureOnly) {
			filter = File::isDirectory;
		}

		try {
			final ArrayList<String> list = new ArrayList<>();
			getStructure(sourceDirectory, list, filter);
			for (final String sourceFileName : list) {
				final String relativePart = FilePathTools.substractFilePath(sourceFileName, sourceFolder,
						File.separatorChar);
				final String newFilename = targetFolder + File.separator + relativePart;
				final File destFile = new File(newFilename);
				final File sourceFile = new File(sourceFileName);
				if (structureOnly) {
					destFile.mkdirs();
				} else {
					if (sourceFile.isFile()) {
						copyFile(sourceFileName, newFilename, false);
					}
				}
			}
			return true;
		} catch (final IOException e) {
			return false;
		}

	}

	public static void getStructure(final File root, final List<String> structure, final FileFilter filter)
			throws IOException {
		if (root.isDirectory()) {
			File[] kids;
			if (filter != null) {
				kids = root.listFiles(filter);
			} else {
				kids = root.listFiles();
			}
			for (final File kid : kids) {
				structure.add(kid.getPath());
			}
			for (final File kid : kids) {
				getStructure(kid, structure, filter);
			}
		}
	}

	private static boolean keepModificationDate(final File sourceFile, final File destFile) {
		final long lm = (long) (Math.floor(sourceFile.lastModified() / 1000.0) * 1000.0);
		return sourceFile.setLastModified(lm) && destFile.setLastModified(lm);
	}

}
