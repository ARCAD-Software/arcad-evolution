/*
 * Créé le 25 mai 04
 *
 * Pour changer le modèle de ce fichier généré, allez à :
 * Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
package com.arcadsoftware.aev.core.ui.wizards;

import org.eclipse.jface.wizard.Wizard;

/**
 * @author MD
 * 
 *         Pour changer le modèle de ce commentaire de type généré, allez à :
 *         Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et
 *         commentaires
 */
public abstract class ArcadWizard extends Wizard {

	private boolean browseMode = false;

	public ArcadWizard() {
		super();
		setNeedsProgressMonitor(true);
	}

	public abstract void beforeShow();

	protected void doAfterPerformFinish() {
		// Do nothing
	}

	/**
	 * @return Returns the browseMode.
	 */
	public boolean isBrowseMode() {
		return browseMode;
	}

	/**
	 * @param browseMode
	 *            The browseMode to set.
	 */
	public void setBrowseMode(boolean browseMode) {
		this.browseMode = browseMode;
	}
}
