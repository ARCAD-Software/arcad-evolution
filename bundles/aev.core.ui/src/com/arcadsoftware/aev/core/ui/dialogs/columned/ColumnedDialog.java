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
package com.arcadsoftware.aev.core.ui.dialogs.columned;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Shell;

import com.arcadsoftware.aev.core.ui.dialogs.ArcadDialog;

/**
 * @author dlelong
 */
public abstract class ColumnedDialog extends ArcadDialog {
	protected static int ADD_ID = 40;
	protected static int REMOVE_ID = 41;
	protected Button addButton;
	protected Button cancelButton;
	protected Button downButton;
	protected Button okButton;
	protected Button removeButton;
	protected Button upButton;

	public ColumnedDialog(final Shell parentShell) {
		super(parentShell);
		setShellStyle(SWT.DIALOG_TRIM | SWT.RESIZE | SWT.MAX | SWT.APPLICATION_MODAL);
	}

	public void manageMoveButtons(final boolean isMoveable) {
		upButton.setEnabled(isMoveable);
		downButton.setEnabled(isMoveable);
	}
}
