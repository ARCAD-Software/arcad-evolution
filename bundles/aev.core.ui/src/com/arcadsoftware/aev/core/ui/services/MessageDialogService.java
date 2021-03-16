package com.arcadsoftware.aev.core.ui.services;

import java.util.Optional;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

public interface MessageDialogService {
	
	public String getApplicationTitle();

	default Shell getPluginShell() {
		return PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
	}

	default boolean openConfirm(final String message) {
		return MessageDialog.openConfirm(getPluginShell(), getApplicationTitle(), message);
	}

	default boolean openConfirm(final String title, final String message) {
		return MessageDialog.openConfirm(getPluginShell(), title, message);
	}

	default void openError(final String message) {
		openError(message, (Throwable) null);
	}

	default void openError(final String title, final String message) {
		MessageDialog.openError(getPluginShell(), title, message);
	}

	default void openError(final String message, final Throwable throwable) {
		MessageDialog.openError(getPluginShell(), getApplicationTitle(), Optional.ofNullable(message).orElse("")
				+ Optional.ofNullable(throwable).map(t -> "\n\n" + t.getMessage()).orElse(""));
	}

	default void openInformation(final String message) {
		MessageDialog.openInformation(getPluginShell(), getApplicationTitle(), message);
	}

	default void openInformation(final String title, final String message) {
		MessageDialog.openInformation(getPluginShell(), title, message);
	}

	default boolean openQuestion(final String message) {
		return MessageDialog.openQuestion(getPluginShell(), getApplicationTitle(), message);
	}

	default void openWarning(final String message) {
		MessageDialog.openWarning(getPluginShell(), getApplicationTitle(), message);
	}

	default void openWarning(final String title, final String message) {
		MessageDialog.openWarning(getPluginShell(), title, message);
	}
}
