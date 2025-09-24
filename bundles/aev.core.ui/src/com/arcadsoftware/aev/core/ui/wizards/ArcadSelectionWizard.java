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
 * Cr�� le 10 juin 04
 *
 * Pour changer le mod�le de ce fichier g�n�r�, allez � :
 * Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
 */
package com.arcadsoftware.aev.core.ui.wizards;

/**
 * @author MD Pour changer le mod�le de ce commentaire de type g�n�r�, allez � :
 *         Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
 */
public class ArcadSelectionWizard extends ArcadWizard {
	protected WizardSelectionPage listPage;

	/**
	 *
	 */
	public ArcadSelectionWizard() {
		super();
	}

	/*
	 * (non-Javadoc)
	 * @see com.arcadsoftware.aev.core.ui.wizards.ArcadWizard#beforeShow()
	 */
	@Override
	public void beforeShow() {
		if (listPage != null) {
			listPage.makeInput();
		}
	}

	// --------------------------------------------------------------------------
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.jface.wizard.IWizard#canFinish()
	 */
	@Override
	public boolean canFinish() {
		if (listPage != null) {
			return listPage.isPageComplete();
		}
		return super.canFinish();
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.jface.wizard.Wizard#performFinish()
	 */
	@Override
	public boolean performFinish() {
		return false;
	}
}
