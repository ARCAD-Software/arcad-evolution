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
	 * Méthode d'ajout des pages de l'assistant.<br>
	 * Cette méthode fait appel à la méthode
	 * {@link com.arcadsoftware.core.ui.actions.AbstractSimpleItemWithWizardAction#getPages() getPages()} de la classe
	 * action passé en paramétre au constructeur pour récupérer les pages spécifiques nécessaires à l'exécution de
	 * l'action.<br>
	 * Cette méthode permet aussi d'intégrer automatiquement la page de type {@link SimpleItemCommandWizardPage
	 * SimpleItemCommandWizardPage} permettant l'affichage du résumé et du prompt de commande.
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
	 * Méthode permettant le déclenchement de l'action.<br>
	 * Cette méthode est appelé lorsque l'utilisateur clique sur le bouton "Terminer" de l'assistant.<br>
	 * Cette méthode a été redéfinie pour :
	 * <ul>
	 * <li>Tenir compte des modifications éventuelles de l'action réalisée.</li>
	 * <li>Déléguer l'exécution réelle à l'action appelante via l'appel de la méthode
	 * {@link com.arcadsoftware.core.ui.actions.AbstractSimpleItemWithWizardAction#doExecuteCommand(String)
	 * doExecuteCommand(String command)}</li>
	 * </ul>
	 *
	 * @return boolean : <b>True</b> si l'exécution s'est déroulée correctement, <b>false</b> sinon.<br>
	 *         Ce code retour d'exécution est stocké dans la variable d'instance "executionSucceed" pour pouvoir être
	 *         utilisé par la suite.
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
