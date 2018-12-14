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
	protected Button upButton;
	protected Button downButton;
	protected Button okButton;
	protected Button cancelButton;
	protected Button addButton;
	protected Button removeButton;
	
	public ColumnedDialog(Shell parentShell) {
		super(parentShell);
		setShellStyle(SWT.DIALOG_TRIM | SWT.RESIZE | SWT.MAX | SWT.APPLICATION_MODAL);
	}
	
	public void manageMoveButtons(boolean isMoveable){
		upButton.setEnabled(isMoveable);
		downButton.setEnabled(isMoveable);
	}
}
