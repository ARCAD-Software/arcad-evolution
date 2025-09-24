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
/*
 * Cr�� le 29 sept. 04
 *
 * Pour changer le mod�le de ce fichier g�n�r�, allez � :
 * Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
 */
package com.arcadsoftware.aev.core.ui.dialogs;

import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Shell;

import com.arcadsoftware.aev.core.tools.StringTools;

/**
 * @author MD Pour changer le mod�le de ce commentaire de type g�n�r�, allez � :
 *         Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
 */
public class ArcadCenteredDialog extends ArcadDialog {

	private int height = 270;
	private String title = StringTools.EMPTY;
	private int width = 350;

	public ArcadCenteredDialog(final Shell parentShell) {
		super(parentShell);
	}

	/**
	 * @param parentShell
	 */
	public ArcadCenteredDialog(final Shell parentShell, final boolean OkButtonOnly, final int width, final int height,
			final String title) {
		super(parentShell, OkButtonOnly);
		this.width = width;
		this.height = height;
		this.title = title;
	}

	/**
	 * @param parentShell
	 */
	public ArcadCenteredDialog(final Shell parentShell, final int width, final int height, final String title) {
		this(parentShell, false, width, height, title);
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.jface.window.Window#configureShell(org.eclipse.swt.widgets .Shell)
	 */
	@Override
	protected void configureShell(final Shell newShell) {
		super.configureShell(newShell);
		newShell.setText(title);
		// D�finition de la taille du dialog
		newShell.setSize(width, height);
		// Centrage du dialogue
		final Rectangle parentBounds = newShell.getDisplay().getClientArea();
		final int x = parentBounds.x + (parentBounds.width - width) / 2;
		final int y = parentBounds.y + (parentBounds.height - height) / 2;
		newShell.setLocation(x, y);
	}

	/**
	 * @param i
	 */
	public void setHeight(final int i) {
		height = i;
	}

	/**
	 * @param i
	 */
	public void setWidth(final int i) {
		width = i;
	}
}