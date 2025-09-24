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

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.core.filesystem.EFS;
import org.eclipse.core.filesystem.IFileStore;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;
import org.osgi.service.component.annotations.Component;

import com.arcadsoftware.aev.core.messages.MessageManager;
import com.arcadsoftware.aev.core.ui.EvolutionCoreUIPlugin;
import com.arcadsoftware.aev.core.ui.tools.IFileManagerProvider;

import net.sf.jmimemagic.Magic;
import net.sf.jmimemagic.MagicMatch;

@Component(service = IFileManagerProvider.class)
public class FileManagerProvider implements IFileManagerProvider {

	public static final String BIN_EXTENSION = "bin"; //$NON-NLS-1$

	private static String discoverFileExtension(final String actualFilename) {
		MagicMatch match;
		try {
			match = Magic.getMagicMatch(new File(actualFilename), true);
			final String mimeType = match.getMimeType();
			if (mimeType != null) {
				final int pos = mimeType.indexOf('/');
				if (pos > -1) {
					return mimeType.substring(pos + 1);
				}
			}
		} catch (final Exception e) {
			MessageManager.addException(e, MessageManager.LEVEL_PRODUCTION);
		}
		return BIN_EXTENSION;
	}

	private static void openFile(final File file, final boolean isTempFile) {
		final IFileStore fileStore = EFS.getLocalFileSystem().getStore(file.toURI());
		final IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		if (isTempFile) {
			try {
				final EditorTempFilePartListener listener = new EditorTempFilePartListener(file);
				final IEditorPart editor = IDE.openEditorOnFileStore(page, fileStore);
				listener.setEditor(editor);
				page.addPartListener(listener);
			} catch (final PartInitException e) {
				MessageManager.addException(e, MessageManager.LEVEL_PRODUCTION);
			}
		} else {
			try {
				IDE.openEditorOnFileStore(page, fileStore);
			} catch (final PartInitException e) {
				MessageManager.addException(e, MessageManager.LEVEL_PRODUCTION);
			}
		}
	}

	private static void openTempFile(final File file) {
		openFile(file, true);
	}

	@Override
	public boolean isSelectorAvailable() {
		return true;
	}

	@Override
	public void openFile(final File file) {
		openFile(file, false);
	}

	@Override
	public void openFileFromStream(final InputStream stream, final String tempFileName) {
		if (StringUtils.isNotBlank(tempFileName) && stream != null) {
			final File tempFile;
			try {
				tempFile = 
						new File(System.getProperty("java.io.tmpdir"), UUID.randomUUID() + tempFileName)
							.getCanonicalFile();
				if(!(tempFile.setReadable(true, true) &&
						tempFile.setWritable(true, true) &&
						tempFile.setExecutable(true, true))){
					throw new IOException("Could not set RWX for owner only on " + tempFile);
				}
				tempFile.deleteOnExit();
			} catch (IOException e1) {
				MessageManager.addException(e1, MessageManager.LEVEL_PRODUCTION);
				return;
			} 
			
			try (
				BufferedInputStream input = new BufferedInputStream(stream);	
				FileOutputStream output = FileUtils.openOutputStream(tempFile)
			){				
				IOUtils.copyLarge(input, output);
			} catch (final IOException e1) {
				MessageManager.addException(e1, MessageManager.LEVEL_PRODUCTION);
			}

			final String actualFilename = tempFile.getAbsolutePath();
			final String extension = FilenameUtils.getExtension(actualFilename);
			final String basename = FilenameUtils.getBaseName(actualFilename);
			final String path = FilenameUtils.getPath(actualFilename);
			if (extension.equalsIgnoreCase(BIN_EXTENSION)) {
				final String realExtension = discoverFileExtension(actualFilename);
				if (!realExtension.equalsIgnoreCase(BIN_EXTENSION)) {
					final File realFile = new File(path, basename + "." + realExtension); //$NON-NLS-1$
					try {
						FileUtils.copyFile(tempFile, realFile);
						openTempFile(realFile);
					} catch (final IOException e) {
						MessageManager.addException(e, MessageManager.LEVEL_PRODUCTION);
					}
				} else {
					openTempFile(tempFile);
				}
			} else {
				openTempFile(tempFile);
			}			
		}	
	}

	@Override
	public String selectDirectory(final Shell shell, final int actionStyle, final String title) {
		final DirectoryDialog chooser = new DirectoryDialog(EvolutionCoreUIPlugin.getShell(), actionStyle);
		chooser.setText(title);
		return chooser.open();
	}

	@Override
	public String selectFile(final Shell shell, final int actionStyle, final String title,
			final String[] fileExtensions) {
		final FileDialog chooser = new FileDialog(EvolutionCoreUIPlugin.getShell(), actionStyle);
		chooser.setFilterExtensions(fileExtensions);
		chooser.setText(title);
		return chooser.open();
	}

	@Override
	public String selectFile(final Shell shell, final int actionStyle, final String title,
			final String[] fileExtensions, final String filename) {
		final FileDialog chooser = new FileDialog(EvolutionCoreUIPlugin.getShell(), actionStyle);
		chooser.setFilterExtensions(fileExtensions);
		chooser.setText(title);
		if (filename != null && filename.length() > 0) {
			chooser.setFileName(filename);
		}
		return chooser.open();
	}

	@Override
	public List<String> selectFiles(final Shell shell, final int actionStyle, final String title,
			final String[] fileExtensions) {
		final FileDialog chooser = new FileDialog(EvolutionCoreUIPlugin.getShell(), actionStyle);
		chooser.setFilterExtensions(fileExtensions);
		chooser.setText(title);
		final String selected = chooser.open();
		if (selected != null) {
			final String[] filenames = chooser.getFileNames();
			final String path = chooser.getFilterPath();
			final List<String> result = new ArrayList<>(filenames.length);
			for (final String filename : filenames) {
				result.add(path + File.separator + filename);
			}
			return result;
		}
		return null;
	}
}
