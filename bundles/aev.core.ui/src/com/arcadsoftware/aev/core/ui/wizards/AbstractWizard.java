/*
 * Created on Jul 26, 2006
 */
package com.arcadsoftware.aev.core.ui.wizards;

import org.eclipse.jface.wizard.IWizardPage;

/**
 * @author MD
 */
public abstract class AbstractWizard extends ArcadWizard {

	protected boolean executionSucceed = false;
	protected AbstractSimpleItemWizardPage[] pages;

	/**
	 * 
	 */
	public AbstractWizard() {
		super();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.arcadsoftware.aev.core.ui.wizards.ArcadWizard#beforeShow()
	 */
	@Override
	public void beforeShow() {
		for (int i = 0; i < pages.length; i++) {
			if (pages[i] instanceof AbstractSelectionPage) {
				((AbstractSelectionPage) pages[i]).makeInput();
			}
		}
	}

	/**
	 * Méthode permettant l'annulation de l'action en cours.<br>
	 * Cette méthode est appelé lorsque l'utilisateur clique sur le bouton
	 * "Annuler" de l'assistant.<br>
	 * Cette méthode a été redéfinie pour mettre à jour l'indicateur de succes
	 * de l'opération stocké dans la variable d'instance "executionSucceed" pour
	 * pouvoir être utilisé par la suite.
	 * 
	 * @see org.eclipse.jface.wizard.Wizard#performCancel()
	 */
	@Override
	public boolean performCancel() {
		executionSucceed = false;
		return true;
	}

	/**
	 * Cette méthode permet de déterminer si tous les paramètres nécessaire à
	 * l'exécution de la commande sont valides
	 * 
	 * @see org.eclipse.jface.wizard.IWizard#canFinish()
	 */
	@Override
	public boolean canFinish() {
		IWizardPage currentPage = getContainer().getCurrentPage();
		if ((currentPage.getNextPage() == null) && currentPage.isPageComplete())
			return true;
		return super.canFinish();
	}

	/**
	 * @return Returns the executionSucceed.
	 */
	public boolean isExecutionSucceed() {
		return executionSucceed;
	}

}
