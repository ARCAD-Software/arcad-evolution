/*
 * Créé le 23 mars 05
 *
 * Pour changer le modèle de ce fichier généré, allez à :
 * Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
package com.arcadsoftware.aev.core.ui.wizards;

import org.eclipse.jface.wizard.IWizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;

/**
 * @author MD Pour changer le modèle de ce commentaire de type généré, allez à :
 *         Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
public class ArcadWizardDialog extends WizardDialog {

	/**
	 * @param parentShell
	 * @param newWizard
	 */
	public ArcadWizardDialog(final Shell parentShell, final IWizard newWizard) {
		super(parentShell, newWizard);
		this.setPageSize(565, 350);
		updateDialog();
	}

	public void perforFinish() {
		finishPressed();
	}

	public void updateDialog() {
		updateSize(getCurrentPage());
	}

}
