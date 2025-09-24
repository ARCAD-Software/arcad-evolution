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
package com.arcadsoftware.aev.core.ui.actions;

import org.eclipse.jface.dialogs.Dialog;

import com.arcadsoftware.aev.core.model.ArcadEntity;
import com.arcadsoftware.aev.core.ui.EvolutionCoreUIPlugin;
import com.arcadsoftware.aev.core.ui.wizards.AbstractSimpleItemWizard;
import com.arcadsoftware.aev.core.ui.wizards.AbstractSimpleItemWizardPage;
import com.arcadsoftware.aev.core.ui.wizards.ArcadWizardDialog;

/**
 * Classe des actions n'agissant que sur une seule entit� ARCAD par envoi d'une commande sur le serveur iSeries et
 * utilisant un assistant<br>
 *
 * @author MD
 */
public abstract class AbstractSimpleItemWithWizardAction extends AbstractSimpleItemAction {

	// bool�en utilis� pour l'appel de la m�thode runActions()
	// apr�s la fermetuire du wizard
	protected boolean actionsToRunAfterWizard = true;
	ArcadWizardDialog dialog;

	private AbstractSimpleItemWizard wizard;

	public AbstractSimpleItemWithWizardAction() {
		super();
	}

	public AbstractSimpleItemWithWizardAction(final ArcadActions containerActions) {
		super(containerActions);
	}

	/**
	 * M�thode de sp�cification de l'assistant � utiliser dans l'action<br>
	 * Impl�mentez cette m�thode pour sp�cifier quel assistant votre action doit utiliser lors de son ex�cution. Par
	 * d�faut : <br>
	 * - l'action pass�e est l'instance elle m�me (this)<br>
	 * - Le titre pass� est celui d�fini par la m�thode
	 *
	 * @param action
	 *            AbstractSimpleItemWithWizardAction : Action appelante
	 * @param title
	 *            String : Titre de l'assistant {@link #getWizardTitle() getWizardTitle()}
	 * @return AbstractSimpleItemWizard : Renoit un assistant du type
	 *         {@link com.arcadsoftware.core.ui.wizards.AbstractSimpleItemWizard AbstractSimpleItemWizard}
	 */
	public abstract AbstractSimpleItemWizard createWizard(AbstractSimpleItemWithWizardAction action, String title);

	protected void doAfterWizardCreation(final Dialog dialog) {

	}

	/**
	 * M�thode de d�clenchement de l'ex�cution de l'action.<br>
	 * Cette m�thode prend en charge l'ensemble du cycle d'ex�cution de l'action, y compris l'affichage d'un assistant.
	 * <p>
	 * <b><u>Les �tapes du cycles</u></b><br>
	 * <ol>
	 * <li>Appel � la m�thode {@link #getWizardTitle() getWizardTitle()} pour r�cup�ration du titre de l'assistant.</li>
	 * <li>Appel � la m�thode {@link #createWizard(AbstractSimpleItemWithWizardAction, String) createWizard()} pour
	 * g�n�ration d'un assistant.</li>
	 * <li>SI l'assistant cr�� n'est pas null ALORS
	 * <ul>
	 * <li>Affichage de l'assistant</li>
	 * <li>Retourne la valeur "isExecutionSucceed" de l'assistant</li>
	 * </ul>
	 * </ol>
	 * </p>
	 *
	 * @see com.arcadsoftware.core.ui.actions.ArcadAction#execute()
	 */
	@Override
	protected boolean execute() {
		wizard = createWizard(this, getWizardTitle());
		if (wizard != null) {
			dialog = new ArcadWizardDialog(EvolutionCoreUIPlugin.getDefault().getPluginShell(),
					wizard);
			dialog.setPageSize(getWidth(), getHeight());
			dialog.create();
			doAfterWizardCreation(dialog);
			wizard.beforeShow();
			dialog.open();
			return wizard.isExecutionSucceed();
		}
		return true;
	}

	protected ArcadWizardDialog getDialog() {
		return dialog;
	}

	/**
	 * M�thode qui renvoie la hauteur du dialogue A surcharger si l'on souhaite changer la hauteur du dialogue
	 *
	 * @return hauteur du dialogue de l'assistant
	 */
	protected int getHeight() {
		return 400;
	}

	/**
	 * Renvoi une entit� ARCAD sur laquelle porte �ventuellement la commande
	 *
	 * @return ArcadEntity : Entit� trait�e
	 */
	public abstract ArcadEntity getItem();

	/**
	 * M�thode permettant de sp�cifier les pages � ajouter � l'assistant.<br>
	 * Cette m�thode permet de d�finir un tableau contenant les pages de type
	 * {@link com.arcadsoftware.core.ui.wizards.AbstractSimpleItemWizardPage AbstractSimpleItemWizardPage} � ajouter
	 * dans l'assistant.<br>
	 * Cette m�thode est appel�e directement par la m�thode
	 * {@link com.arcadsoftware.core.ui.wizards.AbstractSimpleItemWizard#addPages() addPages()} de l'assistant.
	 *
	 * @return Tableau d'AbstractSimpleItemWizardPage : Pages � ajouter � l'assistant.
	 */
	public abstract AbstractSimpleItemWizardPage[] getPages();

	/**
	 * M�thode qui renvoie la largeur du dialogue A surcharger si l'on souhaite changer la largeur du dialogue
	 *
	 * @return largeur du dialogue de l'assistant
	 */
	protected int getWidth() {
		return 550;
	}

	/**
	 * M�thode de d�finition du titre de l'assistant.<br>
	 *
	 * @return String : Titre de l'assistant
	 */
	public abstract String getWizardTitle();

	public boolean isActionsToRunAfterWizard() {
		return actionsToRunAfterWizard;
	}

	/**
	 * M�thode permettant d'appeler les actions ex�cuter. M�thode appel�e dans
	 * {@link com.arcadsoftware.core.ui.wizards.AbstractSimpleItemWizard#performFinish()} . A surcharger si l'on
	 * souhaite que toutes les op�rations s'�xecutent avant la fermeture du wizard.
	 *
	 * @return <b>true</b> si les actions se sont bien d�roul�es <b>false</b> sinon
	 */
	public boolean runActions() {
		return false;
	}

	public void setActionsToRunAfterWizard(final boolean actionsToRunAfterWizard) {
		this.actionsToRunAfterWizard = actionsToRunAfterWizard;
	}
}