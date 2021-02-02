package com.arcadsoftware.aev.core.ui.rcp.tools;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
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
import net.sf.jmimemagic.MagicException;
import net.sf.jmimemagic.MagicMatch;
import net.sf.jmimemagic.MagicMatchNotFoundException;
import net.sf.jmimemagic.MagicParseException;

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
		} catch (final MagicParseException e) {
			MessageManager.addException(e, MessageManager.LEVEL_PRODUCTION);
		} catch (final MagicMatchNotFoundException e) {
			MessageManager.addException(e, MessageManager.LEVEL_PRODUCTION);
		} catch (final MagicException e) {
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
		if (tempFileName != null && tempFileName.length() > 0) {
			if (stream != null) {
				FileOutputStream out = null;
				File tempFile = null;
				try {
					tempFile = File.createTempFile("customer_", '_' + tempFileName); //$NON-NLS-1$
					tempFile.deleteOnExit();
					out = new FileOutputStream(tempFile);
					final byte buf[] = new byte[1024];
					int len;
					while ((len = stream.read(buf)) > 0) {
						out.write(buf, 0, len);
					}
				} catch (final IOException e1) {
					MessageManager.addException(e1, MessageManager.LEVEL_PRODUCTION);
				} finally {
					if (out != null) {
						try {
							out.close();
						} catch (final IOException e1) {
							MessageManager.addException(e1, MessageManager.LEVEL_PRODUCTION);
						}
					}
					try {
						stream.close();
					} catch (final IOException e1) {
						MessageManager.addException(e1, MessageManager.LEVEL_PRODUCTION);
					}
				}
				if (tempFile != null) {
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
