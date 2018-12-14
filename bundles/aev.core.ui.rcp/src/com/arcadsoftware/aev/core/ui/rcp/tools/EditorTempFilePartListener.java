package com.arcadsoftware.aev.core.ui.rcp.tools;

import java.io.File;

import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IPartListener;
import org.eclipse.ui.IWorkbenchPart;

public class EditorTempFilePartListener implements IPartListener {

	private File tempFile;
	private IEditorPart editor;

	public EditorTempFilePartListener(final File tempFile) {
		super();
		this.tempFile = tempFile;
	}

	public void setEditor(IEditorPart editor) {
		this.editor = editor;
	}

	public void partActivated(IWorkbenchPart part) {
		// Do nothing
	}

	public void partBroughtToTop(IWorkbenchPart part) {
		// Do nothing
	}

	public void partClosed(IWorkbenchPart part) {
		if (part.equals(editor))
			tempFile.delete();
	}

	public void partDeactivated(IWorkbenchPart part) {
		// Do nothing
	}

	public void partOpened(IWorkbenchPart part) {
		// Do nothing
	}

}
