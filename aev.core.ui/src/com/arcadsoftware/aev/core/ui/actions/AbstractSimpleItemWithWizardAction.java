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
 * Classe des actions n'agissant que sur une seule entité ARCAD par envoi d'une
 * commande sur le serveur iSeries et utilisant un assistant<br>
 * 
 * @author MD
 * 
 */
public abstract class AbstractSimpleItemWithWizardAction extends AbstractSimpleItemAction {

	private AbstractSimpleItemWizard wizard;
	// booléen utilisé pour l'appel de la méthode runActions()
	// après la fermetuire du wizard
	protected boolean actionsToRunAfterWizard = true;

	public AbstractSimpleItemWithWizardAction() {
		super();
	}

	public AbstractSimpleItemWithWizardAction(ArcadActions containerActions) {
		super(containerActions);
	}

	/**
	 * Méthode de spécification de l'assistant à utiliser dans l'action<br>
	 * Implémentez cette méthode pour spécifier quel assistant votre action doit
	 * utiliser lors de son exécution. Par défaut : <br>
	 * - l'action passée est l'instance elle même (this)<br>
	 * - Le titre passé est celui défini par la méthode
	 * 
	 * @param action
	 *            AbstractSimpleItemWithWizardAction : Action appelante
	 * @param title
	 *            String : Titre de l'assistant {@link #getWizardTitle()
	 *            getWizardTitle()}
	 * @return AbstractSimpleItemWizard : Renoit un assistant du type
	 *         {@link com.arcadsoftware.core.ui.wizards.AbstractSimpleItemWizard
	 *         AbstractSimpleItemWizard}
	 */
	public abstract AbstractSimpleItemWizard createWizard(AbstractSimpleItemWithWizardAction action, String title);

	/**
	 * Méthode permettant de spécifier les pages à ajouter à l'assistant.<br>
	 * Cette méthode permet de définir un tableau contenant les pages de type
	 * {@link com.arcadsoftware.core.ui.wizards.AbstractSimpleItemWizardPage
	 * AbstractSimpleItemWizardPage} à ajouter dans l'assistant.<br>
	 * Cette méthode est appelée directement par la méthode
	 * {@link com.arcadsoftware.core.ui.wizards.AbstractSimpleItemWizard#addPages()
	 * addPages()} de l'assistant.
	 * 
	 * @return Tableau d'AbstractSimpleItemWizardPage : Pages à ajouter à
	 *         l'assistant.
	 */
	public abstract AbstractSimpleItemWizardPage[] getPages();

	/**
	 * Méthode de définition du titre de l'assistant.<br>
	 * 
	 * @return String : Titre de l'assistant
	 */
	public abstract String getWizardTitle();

	/**
	 * Renvoi une entité ARCAD sur laquelle porte éventuellement la commande
	 * 
	 * @return ArcadEntity : Entité traitée
	 */
	public abstract ArcadEntity getItem();

	/**
	 * Méthode permettant d'appeler les actions exécuter. Méthode appelée dans
	 * {@link com.arcadsoftware.core.ui.wizards.AbstractSimpleItemWizard#performFinish()}
	 * . A surcharger si l'on souhaite que toutes les opérations s'éxecutent
	 * avant la fermeture du wizard.
	 * 
	 * @return <b>true</b> si les actions se sont bien déroulées <b>false</b>
	 *         sinon
	 */
	public boolean runActions() {
		return false;
	}

	/**
	 * Méthode de déclenchement de l'exécution de l'action.<br>
	 * Cette méthode prend en charge l'ensemble du cycle d'exécution de
	 * l'action, y compris l'affichage d'un assistant.
	 * <p>
	 * <b><u>Les étapes du cycles</u></b><br>
	 * <ol>
	 * <li>Appel à la méthode {@link #getWizardTitle() getWizardTitle()} pour
	 * récupération du titre de l'assistant.</li>
	 * <li>Appel à la méthode
	 * {@link #createWizard(AbstractSimpleItemWithWizardAction, String)
	 * createWizard()} pour génération d'un assistant.</li>
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
		wizard = createWizard(this, getWizardTitle());
		if (wizard != null) {
			ArcadWizardDialog dialog = new ArcadWizardDialog(EvolutionCoreUIPlugin.getDefault().getPluginShell(),
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

	/**
	 * Méthode qui renvoie la hauteur du dialogue A surcharger si l'on souhaite
	 * changer la hauteur du dialogue
	 * 
	 * @return hauteur du dialogue de l'assistant
	 */
	protected int getHeight() {
		return 400;
	}

	/**
	 * Méthode qui renvoie la largeur du dialogue A surcharger si l'on souhaite
	 * changer la largeur du dialogue
	 * 
	 * @return largeur du dialogue de l'assistant
	 */
	protected int getWidth() {
		return 550;
	}

	public boolean isActionsToRunAfterWizard() {
		return actionsToRunAfterWizard;
	}

	public void setActionsToRunAfterWizard(boolean actionsToRunAfterWizard) {
		this.actionsToRunAfterWizard = actionsToRunAfterWizard;
	}

	protected void doAfterWizardCreation(Dialog dialog){
		
	}
}