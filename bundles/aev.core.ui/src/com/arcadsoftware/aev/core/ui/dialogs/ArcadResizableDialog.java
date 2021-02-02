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

	private int height = 220;
	protected String title;
	private int width = 350;

	protected ArcadResizableDialog(final Shell parentShell) {
		super(parentShell);
	}

	public ArcadResizableDialog(final Shell parentShell, final boolean OkButtonOnly, final String title) {
		super(parentShell, OkButtonOnly);
		setShellStyle(SWT.DIALOG_TRIM | SWT.RESIZE | SWT.MAX | SWT.APPLICATION_MODAL);
		this.title = title;
	}

	public ArcadResizableDialog(final Shell parentShell, final boolean OkButtonOnly, final String title,
			final int width, final int height) {
		super(parentShell, OkButtonOnly);
		this.width = width;
		this.height = height;
		setShellStyle(SWT.DIALOG_TRIM | SWT.RESIZE | SWT.MAX | SWT.APPLICATION_MODAL);
		this.title = title;
	}

	public ArcadResizableDialog(final Shell parentShell, final String title) {
		this(parentShell, false, title);
	}

	protected ArcadResizableDialog(final Shell parentShell, final String title, final int width, final int height) {
		this(parentShell, false, title);
		this.width = width;
		this.height = height;
	}

	@Override
	protected void configureShell(final Shell newShell) {
		super.configureShell(newShell);
		newShell.setText(title);
		newShell.setSize(width, height);
		// Centrage du dialogue
		final Rectangle parentBounds = newShell.getDisplay().getClientArea();
		final int x = parentBounds.x + (parentBounds.width - width) / 2;
		final int y = parentBounds.y + (parentBounds.height - height) / 2;
		newShell.setLocation(x, y);
	}

}