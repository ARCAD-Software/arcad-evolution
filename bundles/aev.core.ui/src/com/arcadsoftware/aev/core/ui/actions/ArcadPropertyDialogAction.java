/*
 * Créé le 18 juin 04
 *
 * Pour changer le modèle de ce fichier généré, allez à :
 * Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
package com.arcadsoftware.aev.core.ui.actions;

import java.util.Iterator;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.preference.PreferenceManager;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.window.IShellProvider;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.dialogs.PropertyDialogAction;
import org.eclipse.ui.internal.dialogs.PropertyPageContributorManager;
import org.eclipse.ui.internal.dialogs.PropertyPageManager;

import com.arcadsoftware.aev.core.model.ArcadEntity;
import com.arcadsoftware.aev.core.tools.StringTools;
import com.arcadsoftware.aev.core.ui.container.IContainer;
import com.arcadsoftware.aev.core.ui.dialogs.ArcadPropertyDialog;
import com.arcadsoftware.aev.core.ui.tools.CoreUILabels;

/**
 * @author MD
 * 
 *         Pour changer le modèle de ce commentaire de type généré, allez à :
 *         Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et
 *         commentaires
 */
public class ArcadPropertyDialogAction extends PropertyDialogAction {
	IAdaptable targetElement;
	protected Shell shell;

	/**
	 * @param shell
	 * @param provider
	 */
	public ArcadPropertyDialogAction(IShellProvider shellProvider, ISelectionProvider provider) {
		super(shellProvider, provider);
		this.shell = shellProvider.getShell();
	}

	/**
	 * Returns the name of the given element.
	 * 
	 * @param element
	 *            the element
	 * @return the name of the element
	 */
	private String getName(IAdaptable element) {
		if (element instanceof IContainer)
			return ((IContainer) element).getLabel();
		else if (element instanceof ArcadEntity)
			return ((ArcadEntity) element).getLabel();
		else
			return StringTools.EMPTY;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.action.IAction#run()
	 */
	@Override
	public void run() {
		// TODO [DL] tester propertyPage
		PropertyPageManager pageManager = new PropertyPageManager();
		// PreferenceManager pageManager = new PreferenceManager();
		String title = StringTools.EMPTY;

		IAdaptable element = null;
		// get selection
		if (targetElement == null)
			element = (IAdaptable) getStructuredSelection().getFirstElement();
		else
			element = targetElement;
		if (element == null)
			return;

		// load pages for the selection
		// fill the manager with contributions from the matching contributors
		// if (element instanceof IComponentLinked)
		// PropertyPageContributorManager.getManager().contribute(pageManager,
		// ((IComponentLinked)element).getLinkedComponent());
		// else if (element instanceof IVersionLinked)
		// PropertyPageContributorManager.getManager().contribute(pageManager,
		// ((IVersionLinked)element).getLinkedVersion());
		// //<FM numer=2006/00330 version=v 2.0.0.2>
		// else if (element instanceof IEnvironmentLinked)
		// PropertyPageContributorManager.getManager().contribute(pageManager,
		// ((IEnvironmentLinked)element).getLinkedEnvironment());
		// //</FM>
		// else if (element instanceof IApplicationLinked)
		// PropertyPageContributorManager.getManager().contribute(pageManager,
		// ((IApplicationLinked)element).getLinkedApplication());
		// else if (element instanceof HistoryElement)
		// PropertyPageContributorManager.getManager().contribute(pageManager,
		// (HistoryElement)element);
		// else
		// return;
		PropertyPageContributorManager.getManager().contribute(pageManager, element);
		// testing if there are pages in the manager
		Iterator<?> pages = pageManager.getElements(PreferenceManager.PRE_ORDER).iterator();

		String name = getName(element);
		if (!pages.hasNext()) {
			MessageDialog.openInformation(shell, CoreUILabels.resString("msg.commonTitle"), //$NON-NLS-1$
					CoreUILabels.resString("msg.noPropertyPage")); //$NON-NLS-1$
			return;
		}
		StringBuffer sb = new StringBuffer(CoreUILabels.resString("msg.propertyPageTitle"));//$NON-NLS-1$ 
		sb.append(name);
		title = sb.toString();
		ArcadPropertyDialog propertyDialog = new ArcadPropertyDialog(shell, pageManager, getStructuredSelection());
		propertyDialog.create();
		propertyDialog.doAfterCreation();
		propertyDialog.getShell().setText(title);
		propertyDialog.open();
	}

	/**
	 * @param adaptable
	 */
	public void setTargetElement(IAdaptable adaptable) {
		targetElement = adaptable;
	}

}
