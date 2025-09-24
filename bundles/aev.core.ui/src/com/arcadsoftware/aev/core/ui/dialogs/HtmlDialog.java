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
package com.arcadsoftware.aev.core.ui.dialogs;

import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

/**
 * @author jbeauquis This dialog affords to display html content in a dialog. The <param>htmlBody</param> must not
 *         contain tags <body> and </body>
 */
public class HtmlDialog extends ArcadResizableDialog {

	private Browser browser;
	private String htmlBody;
	private int xPos = -1;
	private int yPos = -1;

	public HtmlDialog(final Shell parentShell) {
		super(parentShell);
	}

	public HtmlDialog(final Shell parentShell, final String title, final String htmlBody) {
		super(parentShell, title);
		this.htmlBody = htmlBody;
		setShellStyle(SWT.DIALOG_TRIM | SWT.RESIZE | SWT.MAX | SWT.APPLICATION_MODAL);
	}

	public HtmlDialog(final Shell parentShell, final String title, final String htmlBody, final int width,
			final int height) {
		super(parentShell, title, width, height);
		this.htmlBody = htmlBody;
		setShellStyle(SWT.DIALOG_TRIM | SWT.RESIZE | SWT.MAX | SWT.APPLICATION_MODAL);
	}

	public HtmlDialog(final Shell parentShell, final String title, final String htmlBody, final int width,
			final int height, final int xPosition,
			final int yPosition) {
		super(parentShell, title, width, height);
		this.htmlBody = htmlBody;
		setShellStyle(SWT.DIALOG_TRIM | SWT.RESIZE | SWT.MAX | SWT.APPLICATION_MODAL);
		xPos = xPosition;
		yPos = yPosition;
	}

	@Override
	protected void configureShell(final Shell newShell) {
		super.configureShell(newShell);
		if (xPos != -1 && yPos != -1) {
			newShell.setLocation(xPos, yPos);
		}
	}

	@Override
	protected Control createDialogArea(final Composite parent) {
		browser = new Browser(parent, SWT.NONE);
		if (parent.getLayout() instanceof GridLayout) {
			final GridData gridData = new GridData(GridData.FILL, GridData.FILL, true, true);
			browser.setLayoutData(gridData);
		}
		browser.setText(prepareHTML());
		// Rectangle shellRect = this.getShell().getClientArea();
		// Rectangle compRect = parent.getBounds();
		// Rectangle browserRect = browser.getBounds();
		// System.out.println("dimension shell: largeur=" + shellRect.height
		// + " ## hauteur=" + shellRect.height + " ## x=" + shellRect.x +
		// " ## y=" + shellRect.y);
		// System.out.println("dimension composite: largeur=" + compRect.height
		// + " ## hauteur=" + compRect.height + " ## x=" + compRect.x +
		// " ## y=" + compRect.y);
		// System.out.println("dimension browser: largeur=" +
		// browserRect.height + " ## hauteur=" + browserRect.height +
		// " ## x=" + browserRect.x + " ## y=" + browserRect.y);

		return browser;
	}

	private String prepareHTML() {
		final StringBuilder html = new StringBuilder("<html>"); //$NON-NLS-1$
		html.append("<title>") //$NON-NLS-1$
				.append(title).append("</title>"); //$NON-NLS-1$
		if (!htmlBody.trim().startsWith("<body>")) {
			html.append("<body>"); //$NON-NLS-1$
		}
		html.append(htmlBody);
		if (!htmlBody.trim().endsWith("</body>")) {
			html.append("</body>"); //$NON-NLS-1$
		}
		html.append("</html>"); //$NON-NLS-1$
		return html.toString();
	}
}