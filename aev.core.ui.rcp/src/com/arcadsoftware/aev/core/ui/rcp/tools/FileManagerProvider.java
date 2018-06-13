package com.arcadsoftware.aev.core.ui.rcp.tools;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import net.sf.jmimemagic.Magic;
import net.sf.jmimemagic.MagicException;
import net.sf.jmimemagic.MagicMatch;
import net.sf.jmimemagic.MagicMatchNotFoundException;
import net.sf.jmimemagic.MagicParseException;

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

import com.arcadsoftware.aev.core.messages.MessageManager;
import com.arcadsoftware.aev.core.ui.EvolutionCoreUIPlugin;
import com.arcadsoftware.aev.core.ui.tools.IFileManagerProvider;

public class FileManagerProvider implements IFileManagerProvider {

	public static final String BIN_EXTENSION = "bin"; //$NON-NLS-1$	
	
	public String selectDirectory(Shell shell, int actionStyle, String title) {
		DirectoryDialog chooser = new DirectoryDialog(EvolutionCoreUIPlugin.getShell(), actionStyle);
		chooser.setText(title);
		return chooser.open();
	}

	public String selectFile(Shell shell, int actionStyle, String title,final String[] fileExtensions) {
		FileDialog chooser = new FileDialog(EvolutionCoreUIPlugin.getShell(), actionStyle);
		chooser.setFilterExtensions(fileExtensions);
		chooser.setText(title);
		return chooser.open();
	}
	
	public List<String> selectFiles(Shell shell, int actionStyle, String title,final String[] fileExtensions) {
		FileDialog chooser = new FileDialog(EvolutionCoreUIPlugin.getShell(), actionStyle);
		chooser.setFilterExtensions(fileExtensions);
		chooser.setText(title);
		String selected = chooser.open();
		if (selected != null){ 
			String[] filenames = chooser.getFileNames();
			String path = chooser.getFilterPath();
			List<String> result = new ArrayList<String>(filenames.length);
			for (String filename : filenames) {
				result.add(path+File.separator+filename);
			}
			return result;
		}
		return null;
	}	
	
	public String selectFile(Shell shell, int actionStyle, String title,final String[] fileExtensions,String filename) {
		FileDialog chooser = new FileDialog(EvolutionCoreUIPlugin.getShell(), actionStyle);
		chooser.setFilterExtensions(fileExtensions);
		chooser.setText(title);
		if ((filename!=null) && (filename.length()>0)) {
			chooser.setFileName(filename);
		}
		return chooser.open();
	}

	public void openFileFromStream(InputStream stream, String tempFileName) {
		if (tempFileName != null && tempFileName.length() > 0) {
			if (stream != null) {
				FileOutputStream out = null;
				File tempFile = null;
				try {					
					tempFile = File.createTempFile("customer_", '_' + tempFileName); //$NON-NLS-1$
					out = new FileOutputStream(tempFile);
					byte buf[] = new byte[1024];
					int len;
					while ((len = stream.read(buf)) > 0)
						out.write(buf, 0, len);
				} catch (IOException e1) {
					MessageManager.addException(e1, MessageManager.LEVEL_PRODUCTION);
				} finally {
					if (out != null)
						try {
							out.close();
						} catch (IOException e1) {
							MessageManager.addException(e1, MessageManager.LEVEL_PRODUCTION);
						}
					try {
						stream.close();
					} catch (IOException e1) {
						MessageManager.addException(e1, MessageManager.LEVEL_PRODUCTION);
					}
				}
				if (tempFile != null){
					String actualFilename = tempFile.getAbsolutePath();
					String extension =   FilenameUtils.getExtension(actualFilename);
					String basename =   FilenameUtils.getBaseName(actualFilename);
					String path =   FilenameUtils.getPath(actualFilename);
					if (extension.equalsIgnoreCase(BIN_EXTENSION)) { 			
						String realExtension = discoverFileExtension(actualFilename);						
						if (!realExtension.equalsIgnoreCase(BIN_EXTENSION)){
							File realFile = new File(path, basename+"."+realExtension); //$NON-NLS-1$
							try {
								FileUtils.copyFile(tempFile, realFile);
								openTempFile(realFile);
							} catch (IOException e) {
								MessageManager.addException(e, MessageManager.LEVEL_PRODUCTION);
							}
						} else {
							openTempFile(tempFile);
						}
					} else{
						openTempFile(tempFile);
					}
				}
			}
		}
	}

	private static String discoverFileExtension(String actualFilename){
		MagicMatch match;
		try {
			match = Magic.getMagicMatch(new File(actualFilename),true);
			String mimeType = match.getMimeType();
			if (mimeType!=null) {
				int pos = mimeType.indexOf('/');
				if (pos>-1) {
					return mimeType.substring(pos+1);
				}
			}
		} catch (MagicParseException e) {
			MessageManager.addException(e, MessageManager.LEVEL_PRODUCTION);
		} catch (MagicMatchNotFoundException e) {
			MessageManager.addException(e, MessageManager.LEVEL_PRODUCTION);
		} catch (MagicException e) {
			MessageManager.addException(e, MessageManager.LEVEL_PRODUCTION);
		}
		return BIN_EXTENSION;  
	}
	
	public void openFile(File file) {
		openFile(file, false);
	}

	private static void openTempFile(File file) {
		openFile(file, true);
	}

	private static void openFile(File file, boolean isTempFile) {
		IFileStore fileStore = EFS.getLocalFileSystem().getStore(file.toURI());
		IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		if (isTempFile) {
			try {
				EditorTempFilePartListener listener = new EditorTempFilePartListener(file);
				IEditorPart editor = IDE.openEditorOnFileStore(page, fileStore);
				listener.setEditor(editor);
				page.addPartListener(listener);
			} catch (PartInitException e) {
				MessageManager.addException(e, MessageManager.LEVEL_PRODUCTION);
			}
		} else {
			try {
				IDE.openEditorOnFileStore(page, fileStore);
			} catch (PartInitException e) {
				MessageManager.addException(e, MessageManager.LEVEL_PRODUCTION);
			}
		}
	}

	@Override
	public boolean isSelectorAvailable() {
		return true;
	}
}
