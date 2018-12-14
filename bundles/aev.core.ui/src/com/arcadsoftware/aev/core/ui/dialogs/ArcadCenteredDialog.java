/*
 * Créé le 29 sept. 04
 *
 * Pour changer le modèle de ce fichier généré, allez à :
 * Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
package com.arcadsoftware.aev.core.ui.dialogs;

import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Shell;

import com.arcadsoftware.aev.core.tools.StringTools;

/**
 * @author MD
 * 
 *         Pour changer le modèle de ce commentaire de type généré, allez à :
 *         Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et
 *         commentaires
 */
public class ArcadCenteredDialog extends ArcadDialog {

	private int width = 350;
	private int height = 270;
	private String title = StringTools.EMPTY;

	/**
	 * @param parentShell
	 */
	public ArcadCenteredDialog(Shell parentShell, int width, int height, String title) {
		this(parentShell, false, width, height, title);
	}

	/**
	 * @param parentShell
	 */
	public ArcadCenteredDialog(Shell parentShell, boolean OkButtonOnly, int width, int height, String title) {
		super(parentShell, OkButtonOnly);
		this.width = width;
		this.height = height;
		this.title = title;
	}

	public ArcadCenteredDialog(Shell parentShell) {
		super(parentShell);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.window.Window#configureShell(org.eclipse.swt.widgets
	 * .Shell)
	 */
	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText(title);
		// Définition de la taille du dialog
		newShell.setSize(width, height);
		// Centrage du dialogue
		Rectangle parentBounds = newShell.getDisplay().getClientArea();
		int x = parentBounds.x + (parentBounds.width - width) / 2;
		int y = parentBounds.y + (parentBounds.height - height) / 2;
		newShell.setLocation(x, y);
	}

	/**
	 * @param i
	 */
	public void setHeight(int i) {
		height = i;
	}

	/**
	 * @param i
	 */
	public void setWidth(int i) {
		width = i;
	}
}