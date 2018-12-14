package com.arcadsoftware.aev.core.ui.tools;

import java.io.File;
import java.io.InputStream;
import java.util.List;

import org.eclipse.swt.widgets.Shell;

public interface IFileManagerProvider {
	public String selectDirectory(Shell shell, int actionStyle, String title);
	public String selectFile(Shell shell, int actionStyle, String title,final String[] fileExtensions);
	public String selectFile(Shell shell, int actionStyle, String title,final String[] fileExtensions, String filename);
	public List<String> selectFiles(Shell shell, int actionStyle, String title,final String[] fileExtensions);
	public void openFileFromStream(InputStream stream, String tempFileName);
	public void openFile(File file);
	public boolean isSelectorAvailable();
	
	
	
}
