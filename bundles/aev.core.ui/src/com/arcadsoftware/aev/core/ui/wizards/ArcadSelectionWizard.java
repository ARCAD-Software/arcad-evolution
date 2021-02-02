/*
 * Créé le 10 juin 04
 *
 * Pour changer le modèle de ce fichier généré, allez à :
 * Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
package com.arcadsoftware.aev.core.ui.wizards;

/**
 * @author MD Pour changer le modèle de ce commentaire de type généré, allez à :
 *         Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
public class ArcadSelectionWizard extends ArcadWizard {
	protected WizardSelectionPage listPage;

	/**
	 *
	 */
	public ArcadSelectionWizard() {
		super();
	}

	/*
	 * (non-Javadoc)
	 * @see com.arcadsoftware.aev.core.ui.wizards.ArcadWizard#beforeShow()
	 */
	@Override
	public void beforeShow() {
		if (listPage != null) {
			listPage.makeInput();
		}
	}

	// --------------------------------------------------------------------------
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.jface.wizard.IWizard#canFinish()
	 */
	@Override
	public boolean canFinish() {
		if (listPage != null) {
			return listPage.isPageComplete();
		}
		return super.canFinish();
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.jface.wizard.Wizard#performFinish()
	 */
	@Override
	public boolean performFinish() {
		return false;
	}
}
