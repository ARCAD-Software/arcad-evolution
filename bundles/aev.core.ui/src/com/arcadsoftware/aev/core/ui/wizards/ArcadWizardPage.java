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
 * Cr�� le 25 mai 04
 *
 * Pour changer le mod�le de ce fichier g�n�r�, allez � :
 * Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
 */
package com.arcadsoftware.aev.core.ui.wizards;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.wizard.WizardPage;

import com.arcadsoftware.aev.core.osgi.ServiceRegistry;
import com.arcadsoftware.aev.core.ui.tools.GuiFormatTools;
import com.arcadsoftware.aev.icons.Brand;

/**
 * @author MD
 * @author MLafon
 */
public abstract class ArcadWizardPage extends WizardPage {

	/**
	 * @param pageName
	 */
	public ArcadWizardPage(final String pageName) {
		super(pageName);
	}

	/**
	 * @param pageName
	 * @param title
	 */
	public ArcadWizardPage(final String pageName, final String title) {
		super(pageName, title, null);
		setImageDescriptor(getWizardImage());
	}

	/**
	 * @param pageName
	 * @param title
	 * @param titleImage
	 */
	public ArcadWizardPage(final String pageName, final String title, final ImageDescriptor titleImage) {
		super(pageName, title, titleImage);
	}

	/**
	 * Doit retourner l'identifiant dans l'aide de la page courante.
	 *
	 * @return l'ID de la page
	 */
	protected abstract String getPageHelpContextId();

	private ImageDescriptor getWizardImage() {
		return ServiceRegistry.lookup(IWizardBranding.class)
				.map(IWizardBranding::getBrandingImage)
				.orElseGet(Brand.ARCAD_LOGO_64::imageDescriptor);

	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.jface.dialogs.IDialogPage#setVisible(boolean)
	 */
	@Override
	public void setVisible(final boolean visible) {
		super.setVisible(visible);
		if (visible) {
			final String helpID = getPageHelpContextId();
			if (helpID != null) {
				GuiFormatTools.setHelp(getControl(), helpID);
			}
		}
	}
}
