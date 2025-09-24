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
package com.arcadsoftware.aev.core.ui.rap.tools;

import java.io.File;
import java.io.InputStream;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.osgi.service.component.annotations.Component;

import com.arcadsoftware.aev.core.ui.tools.IFileManagerProvider;

@Component(service = IFileManagerProvider.class)
public class FileManagerProvider implements IFileManagerProvider {
	@Override
	public boolean isSelectorAvailable() {
		return false;
	}

	@Override
	public void openFile(final File file) {
		// We do nothing under RAP Execution
	}

	@Override
	public void openFileFromStream(final InputStream stream, final String tempFileName) {
		// We do nothing under RAP Execution
	}

	@Override
	public String selectDirectory(final Shell shell, final int actionStyle, final String title) {
		return null;
	}

	@Override
	public String selectFile(final Shell shell, final int actionStyle, final String title,
			final String[] fileExtensions) {
		final FileDialog fileDialog = new FileDialog(shell, SWT.OPEN | SWT.APPLICATION_MODAL);
		// fileDialog.setFilterExtensions(fileExtensions);
		fileDialog.setText(title);
		return fileDialog.open();
	}

	@Override
	public String selectFile(final Shell shell, final int actionStyle, final String title,
			final String[] fileExtensions, final String filename) {
		return null;
	}

	@Override
	public List<String> selectFiles(final Shell shell, final int actionStyle, final String title,
			final String[] fileExtensions) {
		return null;
	}

}
