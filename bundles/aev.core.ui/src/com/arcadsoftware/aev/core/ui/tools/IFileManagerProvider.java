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
