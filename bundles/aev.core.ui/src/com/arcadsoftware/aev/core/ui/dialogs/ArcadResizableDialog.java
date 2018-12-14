/**
 * 
 */
package com.arcadsoftware.aev.core.ui.dialogs;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Shell;

/**
 * @author jbeauquis created on 7 déc. 07 12:40:24
 */
public class ArcadResizableDialog extends ArcadDialog {

	protected String title;
	private int width = 350;
	private int height = 220;

	protected ArcadResizableDialog(Shell parentShell) {
		super(parentShell);
	}

	protected ArcadResizableDialog(Shell parentShell, String title, int width, int height) {
		this(parentShell, false, title);
		this.width = width;
		this.height = height;
	}

	public ArcadResizableDialog(Shell parentShell, boolean OkButtonOnly, String title) {
		super(parentShell, OkButtonOnly);
		setShellStyle(SWT.DIALOG_TRIM | SWT.RESIZE | SWT.MAX | SWT.APPLICATION_MODAL);
		this.title = title;
	}

	public ArcadResizableDialog(Shell parentShell, boolean OkButtonOnly, String title, int width, int height) {
		super(parentShell, OkButtonOnly);
		this.width = width;
		this.height = height;
		setShellStyle(SWT.DIALOG_TRIM | SWT.RESIZE | SWT.MAX | SWT.APPLICATION_MODAL);
		this.title = title;
	}

	public ArcadResizableDialog(Shell parentShell, String title) {
		this(parentShell, false, title);
	}

	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText(title);
		newShell.setSize(width, height);
		// Centrage du dialogue
		Rectangle parentBounds = newShell.getDisplay().getClientArea();
		int x = parentBounds.x + (parentBounds.width - width) / 2;
		int y = parentBounds.y + (parentBounds.height - height) / 2;
		newShell.setLocation(x, y);
	}

}