/*
 * Created on 12 avr. 2006
 *
 */
package com.arcadsoftware.aev.core.ui.wizards;

import org.eclipse.jface.wizard.IWizardPage;

import com.arcadsoftware.aev.core.ui.actions.AbstractSimpleItemWithWizardAction;

/**
 * Classe de base de déclaration d'assistant permettant l'exécution d'une
 * commande ARCAD.<br>
 * Cet assistant doit être utilisé en conjonction avec une action de type
 * {@link com.arcadsoftware.core.ui.actions.AbstractSimpleItemWithWizardAction
 * AbstractSimpleItemWithWizardAction} qui doit être passée en paramétre du
 * constructeur.<br>
 * 
 * @author MD
 * 
 */
public class AbstractSimpleItemWizard extends AbstractWizard {

	protected AbstractSimpleItemWithWizardAction action;

	/**
	 * Constructeur de la classe.<br>
	 * 
	 * @param action
	 *            {@link com.arcadsoftware.core.ui.actions.AbstractSimpleItemWithWizardAction
	 *            AbstractSimpleItemWithWizardAction} Action utilisant cet
	 *            assistant
	 * @param title
	 *            String : Titre de l'assistant
	 */
	public AbstractSimpleItemWizard(AbstractSimpleItemWithWizardAction action, String title) {
		this(title);
		this.action = action;
	}

	public AbstractSimpleItemWizard(String title) {
		super();
		setNeedsProgressMonitor(true);
		setWindowTitle(title);
	}

	@Override
	public void addPage(IWizardPage page) {
		super.addPage(page);
		((AbstractSimpleItemWizardPage) page).makePageData();
	}

	/**
	 * Méthode d'ajout des pages de l'assistant.<br>
	 * Cette méthode fait appel à la méthode
	 * {@link com.arcadsoftware.core.ui.actions.AbstractSimpleItemWithWizardAction#getPages()
	 * getPages()} de la classe action passé en paramétre au constructeur pour
	 * récupérer les pages spécifiques nécessaires à l'exécution de l'action.<br>
	 * Cette méthode permet aussi d'intégrer automatiquement la page de type
	 * {@link SimpleItemCommandWizardPage SimpleItemCommandWizardPage}
	 * permettant l'affichage du résumé et du prompt de commande si la valeur de
	 * la variable "addSummaryPage" est vrai;
	 * 
	 * @see org.eclipse.jface.wizard.IWizard#addPages()
	 * @see com.arcadsoftware.core.ui.actions.AbstractSimpleItemWithWizardAction#getPages()
	 */
	@Override
	public void addPages() {
		pages = action.getPages();
		for (int i = 0; i < pages.length; i++) {
			addPage(pages[i]);
			pages[i].setWizard(this);
		}
	}

	/**
	 * Méthode permettant le déclenchement de l'action.<br>
	 * Cette méthode est appelé lorsque l'utilisateur clique sur le bouton
	 * "Terminer" de l'assistant.<br>
	 * Cette méthode a été redéfinie pour :
	 * <ul>
	 * <li>Tenir compte des modifications éventuelles de la commande réalisée
	 * par l'utilisation du prompter de commande.</li>
	 * <li>Déléguer l'exécution réelle à l'action appelante via l'appel de la
	 * méthode
	 * {@link com.arcadsoftware.core.ui.actions.AbstractSimpleItemWithWizardAction#doExecuteCommand(String)
	 * doExecuteCommand(String command)}</li>
	 * <li>Pour opérations plus complexes, déléguer l'exécution réelle à
	 * l'action appelante via l'appel de la méthode
	 * {@link com.arcadsoftware.core.ui.actions.AbstractSimpleItemWithWizardAction#runActions()}
	 * </li>
	 * </ul>
	 * <p>
	 * <b>NOTE :</b><br>
	 * Si la commande est nulle ou égale à chaine vide, on cosidére que
	 * l'exécution s'est déroulée correctement.
	 * </p>
	 * 
	 * @return boolean : <b>True</b> si l'exécution s'est déroulée correctement,
	 *         <b>false</b> sinon.<br>
	 *         Ce code retour d'exécution est stocké dans la variable d'instance
	 *         "executionSucceed" pour pouvoir être utilisé par la suite.
	 * 
	 * @see org.eclipse.jface.wizard.Wizard#performFinish()
	 */
	@Override
	public boolean performFinish() {
		for (int i = 0; i < getPages().length; i++) {
			if (getPages()[i] instanceof AbstractSimpleItemWizardPage)
				((AbstractSimpleItemWizardPage) getPages()[i]).makePageData();
		}
		return executionSucceed = action.isActionsToRunAfterWizard() ? true : action.runActions();
	}

	/**
	 * @return Returns the action.
	 */
	public AbstractSimpleItemWithWizardAction getAction() {
		return action;
	}

	@Override
	public IWizardPage getNextPage(IWizardPage page) {
		IWizardPage nextPage = null;
		if (((AbstractSimpleItemWizardPage) page).isPageComplete()) {
			nextPage = super.getNextPage(page);
		}
		return nextPage;
	}

	@Override
	public boolean canFinish() {
		boolean result = true;
		for (int i = 0; i < getPages().length; i++) {
			if (getPages()[i] instanceof AbstractSimpleItemWizardPage)
				result = result && ((AbstractSimpleItemWizardPage) getPages()[i]).isPageComplete();
		}
		return result;
	}

}