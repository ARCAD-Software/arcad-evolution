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
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.arcadsoftware.aev.core.ui.actions;

import com.arcadsoftware.aev.core.collections.ArcadCollection;
import com.arcadsoftware.aev.core.model.ArcadEntity;
import com.arcadsoftware.aev.core.ui.EvolutionCoreUIPlugin;
import com.arcadsoftware.aev.core.ui.wizards.AbstractMultiItemWizard;
import com.arcadsoftware.aev.core.ui.wizards.AbstractSimpleItemWizardPage;
import com.arcadsoftware.aev.core.ui.wizards.ArcadWizardDialog;

/**
 * Classe permettant l'ex�cution d'une action sur plusieurs �l�ments.<br>
 * Cette classe prend en charge l'appel � un assistant de saisie des donn�es utilisateur n�cessaires au param�trage de
 * l'ex�cution de la commande.
 *
 * @author MD
 */
public abstract class AbstractMultiItemWithWizardAction extends AbstractMultiItemAction {

	ArcadWizardDialog dialog;

	private AbstractMultiItemWizard wizard;

	/**
	 *
	 */
	public AbstractMultiItemWithWizardAction() {
		super();
	}

	/**
	 * @param containerActions
	 */
	public AbstractMultiItemWithWizardAction(final ArcadActions containerActions) {
		super(containerActions);
	}

	/**
	 * @param collection
	 */
	public AbstractMultiItemWithWizardAction(final ArcadCollection collection) {
		super(collection);
	}

	/**
	 * @param entity
	 */
	public AbstractMultiItemWithWizardAction(final ArcadEntity entity) {
		super(entity);
	}

	/**
	 * @param collectionProvider
	 */
	public AbstractMultiItemWithWizardAction(final IArcadCollectionProvider collectionProvider) {
		super(collectionProvider);
	}

	/**
	 * M�thode de sp�cification de l'assistant � utiliser dans l'action<br>
	 * Impl�mentez cette m�thode pour sp�cifier quel assistant votre action doit utiliser lors de son ex�cution. Par
	 * d�faut : <br>
	 * - l'action pass�e est l'instance elle m�me (this)<br>
	 * - Le titre pass� est celui d�fini par la m�thode
	 *
	 * @param action
	 *            AbstractMultiItemWithWizardAction : Action appelante
	 * @param title
	 *            String : Titre de l'assistant {@link #getWizardTitle() getWizardTitle()}
	 * @return AbstractMultiItemWizard : Renoit un assistant du type
	 *         {@link com.arcadsoftware.core.ui.wizards.AbstractMultiItemWizard AbstractMultiItemWizard}
	 */
	public abstract AbstractMultiItemWizard createWizard(AbstractMultiItemWithWizardAction action, String title);

	/**
	 * M�thode de d�clenchement de l'ex�cution de l'action.<br>
	 * Cette m�thode prend en charge l'ensemble du cycle d'ex�cution de l'action, y compris l'affichage d'un assistant.
	 * Lors de circonstances particuli�res ou en fonction de param�tres particuliers vous d�sirez ne pas faire l'appel �
	 * l'assistant de saisie vous devez surcharger la m�thode {@link #useWizard() <code>useWizard()</code>} pour lui
	 * faire renvoyer "false". Dans cas, l'ex�cution sera la m�me que pour une ex�cution standard.
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
		if (!useWizard()) {
			return super.execute();
		}
		wizard = createWizard(this, getWizardTitle());
		if (wizard != null) {
			dialog = new ArcadWizardDialog(EvolutionCoreUIPlugin.getDefault().getPluginShell(),
					wizard);
			dialog.setPageSize(550, 400);
			dialog.create();
			wizard.beforeShow();
			dialog.open();
			return wizard.isExecutionSucceed();
		}
		return false;
	}

	protected ArcadWizardDialog getDialog() {
		return dialog;
	}

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
	 * M�thode de d�finition du titre de l'assistant.<br>
	 *
	 * @return String : Titre de l'assistant
	 */
	public abstract String getWizardTitle();

	protected boolean useWizard() {
		return true;
	}
}
