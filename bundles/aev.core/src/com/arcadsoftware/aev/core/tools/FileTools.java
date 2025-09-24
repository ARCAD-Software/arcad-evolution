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
package com.arcadsoftware.aev.core.tools;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

import com.arcadsoftware.aev.core.messages.MessageManager;

/**
 * Collection of file tools to centralize handling/logging of File related
 * methods.
 * 
 * @author ACL
 *
 */
public class FileTools {

	private FileTools() {
	}

	public static boolean deleteFile(File file) {
		try {
			file.setWritable(true);
			Files.delete(file.toPath());
			return true;
		} catch (IOException e) {
			MessageManager.addAndPrintException(e);
		}
		return false;
	}

	public static boolean createNewFile(File file) throws IOException {
		boolean created = file.createNewFile();
		// This is only false if the file is not created, otherwise it should
		// throw an error...
		if (!created)
			MessageManager.logDiagnostic("File " + file.getName() + " already exists");
		return created;
	}

	public static boolean renameTo(File file, File dest) {
		boolean renamed = file.renameTo(dest);
		if (!renamed)
			MessageManager.logDiagnostic("Could not rename file " + file.getName() + " to " + dest.getName());
		return renamed;
	}

	public static boolean setReadOnly(File file) {
		boolean readOnly = file.setReadOnly();
		if (!readOnly)
			MessageManager.logDiagnostic("Could not set " + file.getName() + " to Read Only");
		return readOnly;
	}

	public static boolean deleteDirectory(final File file) throws IOException {
		try {
			Files.walkFileTree(file.toPath(), new SimpleFileVisitor<Path>() {
				@Override
				public FileVisitResult postVisitDirectory(final Path dir, final IOException exc) throws IOException {
					Files.delete(dir);
					return FileVisitResult.CONTINUE;
				}

				@Override
				public FileVisitResult visitFile(final Path file, final BasicFileAttributes attrs) throws IOException {
					Files.delete(file);
					return FileVisitResult.CONTINUE;
				}
			});

			return true;
		} catch (IOException e) {
			MessageManager.addAndPrintException(e);
			return false;
		}
	}

}
