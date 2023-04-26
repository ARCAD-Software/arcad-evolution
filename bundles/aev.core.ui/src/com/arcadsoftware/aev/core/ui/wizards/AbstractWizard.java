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
	 * M�thode permettant l'annulation de l'action en cours.<br>
	 * Cette m�thode est appel� lorsque l'utilisateur clique sur le bouton
	 * "Annuler" de l'assistant.<br>
	 * Cette m�thode a �t� red�finie pour mettre � jour l'indicateur de succes
	 * de l'op�ration stock� dans la variable d'instance "executionSucceed" pour
	 * pouvoir �tre utilis� par la suite.
	 * 
	 * @see org.eclipse.jface.wizard.Wizard#performCancel()
	 */
	@Override
	public boolean performCancel() {
		executionSucceed = false;
		return true;
	}

	/**
	 * Cette m�thode permet de d�terminer si tous les param�tres n�cessaire �
	 * l'ex�cution de la commande sont valides
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
