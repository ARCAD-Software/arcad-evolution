/*
 * Créé le 25 mai 04
 *
 * Pour changer le modèle de ce fichier généré, allez à :
 * Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
package com.arcadsoftware.aev.core.ui.wizards;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
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
	
	public static final String WIZARDIMG_ID = "com.arcadsoftware.aev.core.ui.branding.wizard";
	
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

	private ImageDescriptor getWizardImage(){
		IExtensionRegistry registry = Platform.getExtensionRegistry();
		IConfigurationElement[] elements = registry.getConfigurationElementsFor(WIZARDIMG_ID);
		IConfigurationElement selectElement = null;
		for (IConfigurationElement element : elements) {
			selectElement = element;
			break;
		}	
		if (selectElement!=null) {
			String bundleId = selectElement.getAttribute("bundleid");
			String path = selectElement.getAttribute("path");
			if ((bundleId==null) || (bundleId.length()==0)){
				bundleId = EvolutionCoreUIPlugin.getDefault().getBundle().getSymbolicName();
			}
			return EvolutionCoreUIPlugin.getDefault().getRegisteredImageDescriptor(bundleId+":"+path);
		} else {
			return ARCAD_IMDDESCRIPTOR_WIZARD;
		}		
		

	}
	
	
	/**
	 * @param pageName
	 * @param title
	 */
	public ArcadWizardPage(String pageName, String title) {
		super(pageName, title, null);
		setImageDescriptor(getWizardImage());
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
