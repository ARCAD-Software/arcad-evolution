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
 * Classe permettant l'exécution d'une action sur plusieurs éléments.<br>
 * Cette classe prend en charge l'appel à un assistant de saisie des données utilisateur nécessaires au paramétrage de
 * l'exécution de la commande.
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
	 * Méthode de spécification de l'assistant à utiliser dans l'action<br>
	 * Implémentez cette méthode pour spécifier quel assistant votre action doit utiliser lors de son exécution. Par
	 * défaut : <br>
	 * - l'action passée est l'instance elle même (this)<br>
	 * - Le titre passé est celui défini par la méthode
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
	 * Méthode de déclenchement de l'exécution de l'action.<br>
	 * Cette méthode prend en charge l'ensemble du cycle d'exécution de l'action, y compris l'affichage d'un assistant.
	 * Lors de circonstances particulières ou en fonction de paramétres particuliers vous désirez ne pas faire l'appel à
	 * l'assistant de saisie vous devez surcharger la méthode {@link #useWizard() <code>useWizard()</code>} pour lui
	 * faire renvoyer "false". Dans cas, l'exécution sera la même que pour une exécution standard.
	 * <p>
	 * <b><u>Les étapes du cycles</u></b><br>
	 * <ol>
	 * <li>Appel à la méthode {@link #getWizardTitle() getWizardTitle()} pour récupération du titre de l'assistant.</li>
	 * <li>Appel à la méthode {@link #createWizard(AbstractSimpleItemWithWizardAction, String) createWizard()} pour
	 * génération d'un assistant.</li>
	 * <li>SI l'assistant créé n'est pas null ALORS
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
	 * Méthode permettant de spécifier les pages à ajouter à l'assistant.<br>
	 * Cette méthode permet de définir un tableau contenant les pages de type
	 * {@link com.arcadsoftware.core.ui.wizards.AbstractSimpleItemWizardPage AbstractSimpleItemWizardPage} à ajouter
	 * dans l'assistant.<br>
	 * Cette méthode est appelée directement par la méthode
	 * {@link com.arcadsoftware.core.ui.wizards.AbstractSimpleItemWizard#addPages() addPages()} de l'assistant.
	 *
	 * @return Tableau d'AbstractSimpleItemWizardPage : Pages à ajouter à l'assistant.
	 */
	public abstract AbstractSimpleItemWizardPage[] getPages();

	/**
	 * Méthode de définition du titre de l'assistant.<br>
	 *
	 * @return String : Titre de l'assistant
	 */
	public abstract String getWizardTitle();

	protected boolean useWizard() {
		return true;
	}
}
