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
 * Created on Jul 25, 2006
 */
package com.arcadsoftware.aev.core.ui.wizards;

import java.lang.reflect.InvocationTargetException;

import com.arcadsoftware.aev.core.messages.MessageDetail;
import com.arcadsoftware.aev.core.messages.MessageManager;
import com.arcadsoftware.aev.core.ui.actions.AbstractMultiItemAction.RunWithProgress;
import com.arcadsoftware.aev.core.ui.actions.AbstractMultiItemWithWizardAction;

/**
 * @author MD
 */
public class AbstractMultiItemWizard extends AbstractWizard {

	private AbstractMultiItemWithWizardAction action;

	/**
	 * Constructeur de la classe.<br>
	 *
	 * @param action
	 *            {@link com.arcadsoftware.core.ui.actions.AbstractSimpleItemWithWizardAction
	 *            AbstractSimpleItemWithWizardAction} Action utilisant cet assistant
	 * @param title
	 *            String : Titre de l'assistant
	 */
	public AbstractMultiItemWizard(final AbstractMultiItemWithWizardAction action, final String title) {
		this(title);
		this.action = action;
	}

	public AbstractMultiItemWizard(final String title) {
		super();
		setNeedsProgressMonitor(true);
		setWindowTitle(title);
	}

	/**
	 * M�thode d'ajout des pages de l'assistant.<br>
	 * Cette m�thode fait appel � la m�thode
	 * {@link com.arcadsoftware.core.ui.actions.AbstractSimpleItemWithWizardAction#getPages() getPages()} de la classe
	 * action pass� en param�tre au constructeur pour r�cup�rer les pages sp�cifiques n�cessaires � l'ex�cution de
	 * l'action.<br>
	 * Cette m�thode permet aussi d'int�grer automatiquement la page de type {@link SimpleItemCommandWizardPage
	 * SimpleItemCommandWizardPage} permettant l'affichage du r�sum� et du prompt de commande.
	 *
	 * @see org.eclipse.jface.wizard.IWizard#addPages()
	 * @see com.arcadsoftware.core.ui.actions.AbstractSimpleItemWithWizardAction#getPages()
	 */
	@Override
	public void addPages() {
		pages = action.getPages();
		for (final AbstractSimpleItemWizardPage page : pages) {
			addPage(page);
		}
	}

	/**
	 * @return Returns the action.
	 */
	public AbstractMultiItemWithWizardAction getAction() {
		return action;
	}

	/**
	 * M�thode permettant le d�clenchement de l'action.<br>
	 * Cette m�thode est appel� lorsque l'utilisateur clique sur le bouton "Terminer" de l'assistant.<br>
	 * Cette m�thode a �t� red�finie pour :
	 * <ul>
	 * <li>Tenir compte des modifications �ventuelles de l'action r�alis�e.</li>
	 * <li>D�l�guer l'ex�cution r�elle � l'action appelante via l'appel de la m�thode
	 * {@link com.arcadsoftware.core.ui.actions.AbstractSimpleItemWithWizardAction#doExecuteCommand(String)
	 * doExecuteCommand(String command)}</li>
	 * </ul>
	 *
	 * @return boolean : <b>True</b> si l'ex�cution s'est d�roul�e correctement, <b>false</b> sinon.<br>
	 *         Ce code retour d'ex�cution est stock� dans la variable d'instance "executionSucceed" pour pouvoir �tre
	 *         utilis� par la suite.
	 * @see org.eclipse.jface.wizard.Wizard#performFinish()
	 */
	@Override
	public boolean performFinish() {
		final RunWithProgress runCommand = action.getRunnableAction();
		MessageManager.clear();
		try {
			getContainer().run(false, false, runCommand);
		} catch (final InvocationTargetException e) {
			MessageManager.addException(e, MessageManager.LEVEL_PRODUCTION).addDetail(MessageDetail.ERROR,
					this.getClass().toString());
		} catch (final InterruptedException e) {
			MessageManager.addException(e, MessageManager.LEVEL_PRODUCTION).addDetail(MessageDetail.ERROR,
					this.getClass().toString());
			Thread.currentThread().interrupt();
		}
		executionSucceed = runCommand.getResult();
		return executionSucceed;
	}

}
