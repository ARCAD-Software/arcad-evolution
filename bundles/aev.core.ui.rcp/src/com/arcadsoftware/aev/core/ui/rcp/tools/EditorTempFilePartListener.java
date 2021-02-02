package com.arcadsoftware.aev.core.ui.rcp.tools;

import java.io.File;

import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IPartListener;
import org.eclipse.ui.IWorkbenchPart;

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
			tempFile.delete();
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
