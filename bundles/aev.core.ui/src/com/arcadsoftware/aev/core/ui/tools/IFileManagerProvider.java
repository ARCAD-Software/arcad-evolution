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
package com.arcadsoftware.aev.core.ui.tools;

import java.io.File;
import java.io.InputStream;
import java.util.List;

import org.eclipse.swt.widgets.Shell;

public interface IFileManagerProvider {
	boolean isSelectorAvailable();

	void openFile(File file);

	void openFileFromStream(InputStream stream, String tempFileName);

	String selectDirectory(Shell shell, int actionStyle, String title);

	String selectFile(Shell shell, int actionStyle, String title, final String[] fileExtensions);

	String selectFile(Shell shell, int actionStyle, String title, final String[] fileExtensions, String filename);

	List<String> selectFiles(Shell shell, int actionStyle, String title, final String[] fileExtensions);

}
