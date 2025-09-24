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
package com.arcadsoftware.aev.core.ui.rcp.tools;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IPartListener;
import org.eclipse.ui.IWorkbenchPart;

import com.arcadsoftware.aev.core.osgi.GlobalLogService;
import com.arcadsoftware.aev.core.osgi.ServiceRegistry;

public class EditorTempFilePartListener implements IPartListener {

	private IEditorPart editor;
	private final File tempFile;

	public EditorTempFilePartListener(final File tempFile) {
		super();
		this.tempFile = tempFile;
	}

	@Override
	public void partActivated(final IWorkbenchPart part) {
		// Do nothing
	}

	@Override
	public void partBroughtToTop(final IWorkbenchPart part) {
		// Do nothing
	}

	@Override
	public void partClosed(final IWorkbenchPart part) {
		if (part.equals(editor)) {
			try {
				Files.delete(tempFile.toPath());
			}
			catch (IOException e) {
				ServiceRegistry.lookupOrDie(GlobalLogService.class).debug(e);
			}
		}
	}

	@Override
	public void partDeactivated(final IWorkbenchPart part) {
		// Do nothing
	}

	@Override
	public void partOpened(final IWorkbenchPart part) {
		// Do nothing
	}

	public void setEditor(final IEditorPart editor) {
		this.editor = editor;
	}

}
