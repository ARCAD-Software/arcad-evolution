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
package com.arcadsoftware.mmk.lists.impl.fillers;

import java.io.File;

import com.arcadsoftware.mmk.lists.impl.lists.FileList;
import com.arcadsoftware.mmk.lists.managers.AbstractFiller;

public class DirectoryScanFiller extends AbstractFiller {

	private final String directoryToScan;

	public DirectoryScanFiller(final FileList list, final String directoryToScan) {
		super(list);
		this.directoryToScan = directoryToScan;
	}

	@Override
	public int fill() {
		final File directoryToScanFile = new File(directoryToScan);
		if (directoryToScanFile.exists() && directoryToScanFile.isDirectory()) {
			return getFiles(directoryToScanFile);
		}
		return -1;
	}

	private int getFiles(final File basedir) {
		if (basedir.exists()) {
			int count = 0;
			final File[] files = basedir.listFiles();
			for (final File file : files) {
				if (file.isDirectory()) {
					getFiles(file);
				} else {
					saveItem(getList().toStoreItem(file));
					count++;
				}
			}
			return count;
		}
		return -1;
	}

}
