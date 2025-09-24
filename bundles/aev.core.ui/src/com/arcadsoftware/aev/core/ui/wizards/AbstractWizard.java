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
	 * @see com.arcadsoftware.aev.core.ui.wizards.ArcadWizard#beforeShow()
	 */
	@Override
	public void beforeShow() {
		for (final AbstractSimpleItemWizardPage page : pages) {
			if (page instanceof AbstractSelectionPage) {
				((AbstractSelectionPage) page).makeInput();
			}
		}
	}

	/**
	 * Cette m�thode permet de d�terminer si tous les param�tres n�cessaire � l'ex�cution de la commande sont valides
	 *
	 * @see org.eclipse.jface.wizard.IWizard#canFinish()
	 */
	@Override
	public boolean canFinish() {
		final IWizardPage currentPage = getContainer().getCurrentPage();
		if (currentPage.getNextPage() == null && currentPage.isPageComplete()) {
			return true;
		}
		return super.canFinish();
	}

	/**
	 * @return Returns the executionSucceed.
	 */
	public boolean isExecutionSucceed() {
		return executionSucceed;
	}

	/**
	 * M�thode permettant l'annulation de l'action en cours.<br>
	 * Cette m�thode est appel� lorsque l'utilisateur clique sur le bouton "Annuler" de l'assistant.<br>
	 * Cette m�thode a �t� red�finie pour mettre � jour l'indicateur de succes de l'op�ration stock� dans la variable
	 * d'instance "executionSucceed" pour pouvoir �tre utilis� par la suite.
	 *
	 * @see org.eclipse.jface.wizard.Wizard#performCancel()
	 */
	@Override
	public boolean performCancel() {
		executionSucceed = false;
		return true;
	}

}
