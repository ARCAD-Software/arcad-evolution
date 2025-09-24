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
 * Created on 12 avr. 2006
 *
 */
package com.arcadsoftware.aev.core.ui.wizards;

import org.eclipse.jface.wizard.IWizardPage;

import com.arcadsoftware.aev.core.ui.actions.AbstractSimpleItemWithWizardAction;

/**
 * Classe de base de d�claration d'assistant permettant l'ex�cution d'une commande ARCAD.<br>
 * Cet assistant doit �tre utilis� en conjonction avec une action de type
 * {@link com.arcadsoftware.core.ui.actions.AbstractSimpleItemWithWizardAction AbstractSimpleItemWithWizardAction} qui
 * doit �tre pass�e en param�tre du constructeur.<br>
 *
 * @author MD
 */
public class AbstractSimpleItemWizard extends AbstractWizard {

	protected AbstractSimpleItemWithWizardAction action;

	/**
	 * Constructeur de la classe.<br>
	 *
	 * @param action
	 *            {@link com.arcadsoftware.core.ui.actions.AbstractSimpleItemWithWizardAction
	 *            AbstractSimpleItemWithWizardAction} Action utilisant cet assistant
	 * @param title
	 *            String : Titre de l'assistant
	 */
	public AbstractSimpleItemWizard(final AbstractSimpleItemWithWizardAction action, final String title) {
		this(title);
		this.action = action;
	}

	public AbstractSimpleItemWizard(final String title) {
		super();
		setNeedsProgressMonitor(true);
		setWindowTitle(title);
	}

	@Override
	public void addPage(final IWizardPage page) {
		super.addPage(page);
		((AbstractSimpleItemWizardPage) page).makePageData();
	}

	/**
	 * M�thode d'ajout des pages de l'assistant.<br>
	 * Cette m�thode fait appel � la m�thode
	 * {@link com.arcadsoftware.core.ui.actions.AbstractSimpleItemWithWizardAction#getPages() getPages()} de la classe
	 * action pass� en param�tre au constructeur pour r�cup�rer les pages sp�cifiques n�cessaires � l'ex�cution de
	 * l'action.<br>
	 * Cette m�thode permet aussi d'int�grer automatiquement la page de type {@link SimpleItemCommandWizardPage
	 * SimpleItemCommandWizardPage} permettant l'affichage du r�sum� et du prompt de commande si la valeur de la
	 * variable "addSummaryPage" est vrai;
	 *
	 * @see org.eclipse.jface.wizard.IWizard#addPages()
	 * @see com.arcadsoftware.core.ui.actions.AbstractSimpleItemWithWizardAction#getPages()
	 */
	@Override
	public void addPages() {
		pages = action.getPages();
		for (final AbstractSimpleItemWizardPage page : pages) {
			addPage(page);
			page.setWizard(this);
		}
	}

	@Override
	public boolean canFinish() {
		boolean result = true;
		for (int i = 0; i < getPages().length; i++) {
			if (getPages()[i] instanceof AbstractSimpleItemWizardPage) {
				result = result && ((AbstractSimpleItemWizardPage) getPages()[i]).isPageComplete();
			}
		}
		return result;
	}

	/**
	 * @return Returns the action.
	 */
	public AbstractSimpleItemWithWizardAction getAction() {
		return action;
	}

	@Override
	public IWizardPage getNextPage(final IWizardPage page) {
		IWizardPage nextPage = null;
		if (((AbstractSimpleItemWizardPage) page).isPageComplete()) {
			nextPage = super.getNextPage(page);
		}
		return nextPage;
	}

	/**
	 * M�thode permettant le d�clenchement de l'action.<br>
	 * Cette m�thode est appel� lorsque l'utilisateur clique sur le bouton "Terminer" de l'assistant.<br>
	 * Cette m�thode a �t� red�finie pour :
	 * <ul>
	 * <li>Tenir compte des modifications �ventuelles de la commande r�alis�e par l'utilisation du prompter de
	 * commande.</li>
	 * <li>D�l�guer l'ex�cution r�elle � l'action appelante via l'appel de la m�thode
	 * {@link com.arcadsoftware.core.ui.actions.AbstractSimpleItemWithWizardAction#doExecuteCommand(String)
	 * doExecuteCommand(String command)}</li>
	 * <li>Pour op�rations plus complexes, d�l�guer l'ex�cution r�elle � l'action appelante via l'appel de la m�thode
	 * {@link com.arcadsoftware.core.ui.actions.AbstractSimpleItemWithWizardAction#runActions()}</li>
	 * </ul>
	 * <p>
	 * <b>NOTE :</b><br>
	 * Si la commande est nulle ou �gale � chaine vide, on cosid�re que l'ex�cution s'est d�roul�e correctement.
	 * </p>
	 *
	 * @return boolean : <b>True</b> si l'ex�cution s'est d�roul�e correctement, <b>false</b> sinon.<br>
	 *         Ce code retour d'ex�cution est stock� dans la variable d'instance "executionSucceed" pour pouvoir �tre
	 *         utilis� par la suite.
	 * @see org.eclipse.jface.wizard.Wizard#performFinish()
	 */
	@Override
	public boolean performFinish() {
		for (int i = 0; i < getPages().length; i++) {
			if (getPages()[i] instanceof AbstractSimpleItemWizardPage) {
				((AbstractSimpleItemWizardPage) getPages()[i]).makePageData();
			}
		}
		return executionSucceed = action.isActionsToRunAfterWizard() ? true : action.runActions();
	}

}