/*
 * Créé le 25 mai 04
 *
 * Pour changer le modèle de ce fichier généré, allez à :
 * Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
package com.arcadsoftware.aev.core.ui.wizards;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.wizard.WizardPage;

import com.arcadsoftware.aev.core.ui.EvolutionCoreUIPlugin;
import com.arcadsoftware.aev.core.ui.tools.CoreUILabels;
import com.arcadsoftware.aev.core.ui.tools.GuiFormatTools;

/**
 * @author MD
 * @author MLafon
 */
abstract public class ArcadWizardPage extends WizardPage {
	public static final ImageDescriptor ARCAD_IMDDESCRIPTOR_WIZARD = CoreUILabels
			.getImageDescriptor(EvolutionCoreUIPlugin.IMG_WIZARD);

	/**
	 * @param pageName
	 */
	public ArcadWizardPage(String pageName) {
		super(pageName);
	}

	/**
	 * @param pageName
	 * @param title
	 * @param titleImage
	 */
	public ArcadWizardPage(String pageName, String title, ImageDescriptor titleImage) {
		super(pageName, title, titleImage);
	}

	/**
	 * @param pageName
	 * @param title
	 */
	public ArcadWizardPage(String pageName, String title) {
		super(pageName, title, ARCAD_IMDDESCRIPTOR_WIZARD);
	}

	/**
	 * Doit retourner l'identifiant dans l'aide de la page courante.
	 * 
	 * @return l'ID de la page
	 */
	abstract protected String getPageHelpContextId();

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.dialogs.IDialogPage#setVisible(boolean)
	 */
	@Override
	public void setVisible(boolean visible) {
		super.setVisible(visible);
		if (visible) {
			String helpID = getPageHelpContextId();
			if (helpID != null)
				GuiFormatTools.setHelp(getControl(), helpID);
		}
	}
}
