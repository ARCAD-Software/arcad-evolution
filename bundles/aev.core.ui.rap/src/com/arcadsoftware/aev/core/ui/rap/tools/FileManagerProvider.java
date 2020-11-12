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
	public String selectDirectory(Shell shell, int actionStyle, String title) {
		return null;
	}
	
	public String selectFile(Shell shell, int actionStyle, String title,final String[] fileExtensions) {
		final FileDialog fileDialog = new FileDialog( shell, SWT.OPEN | SWT.APPLICATION_MODAL );
		//fileDialog.setFilterExtensions(fileExtensions);
		fileDialog.setText(title);
		return fileDialog.open();
	}

	
	public String selectFile(Shell shell, int actionStyle, String title,
			String[] fileExtensions, String filename) {
		return null;
	}
	
	public void openFileFromStream(InputStream stream, String tempFileName) {
		//We do nothing under RAP Execution		
	}
	
	public void openFile(File file) {
		//We do nothing under RAP Execution				
	}

	@Override
	public List<String> selectFiles(Shell shell, int actionStyle, String title,final String[] fileExtensions) {
		return null;
	}

	@Override
	public boolean isSelectorAvailable() {
		return false;
	}	
	
}
