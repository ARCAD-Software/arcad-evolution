package com.arcadsoftware.aev.core.ui.services;

import java.util.Optional;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

public abstract class MessageDialogService {

	protected abstract String getApplicationTitle();

	private Shell getPluginShell() {
		return PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
	}

	public boolean openConfirm(final String message) {
		return MessageDialog.openConfirm(getPluginShell(), getApplicationTitle(), message);
	}

	public boolean openConfirm(final String title, final String message) {
		return MessageDialog.openConfirm(getPluginShell(), title, message);
	}

	public void openError(final String message) {
		openError(message, (Throwable) null);
	}

	public void openError(final String title, final String message) {
		MessageDialog.openError(getPluginShell(), title, message);
	}

	public void openError(final String message, final Throwable throwable) {
		MessageDialog.openError(getPluginShell(), getApplicationTitle(), Optional.ofNullable(message).orElse("")
				+ Optional.ofNullable(throwable).map(t -> "\n\n" + t.getMessage()).orElse(""));
	}

	public void openInformation(final String message) {
		MessageDialog.openInformation(getPluginShell(), getApplicationTitle(), message);
	}

	public void openInformation(final String title, final String message) {
		MessageDialog.openInformation(getPluginShell(), title, message);
	}

	public boolean openQuestion(final String message) {
		return MessageDialog.openQuestion(getPluginShell(), getApplicationTitle(), message);
	}

	public void openWarning(final String message) {
		MessageDialog.openWarning(getPluginShell(), getApplicationTitle(), message);
	}

	public void openWarning(final String title, final String message) {
		MessageDialog.openWarning(getPluginShell(), title, message);
	}
}
