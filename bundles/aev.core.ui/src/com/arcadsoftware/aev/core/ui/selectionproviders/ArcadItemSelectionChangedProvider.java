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
 * Cr�� le 18 mai 04
 *
 * Pour changer le mod�le de ce fichier g�n�r�, allez � :
 * Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
 */
package com.arcadsoftware.aev.core.ui.selectionproviders;

import org.eclipse.jface.viewers.SelectionChangedEvent;

/**
 * @author MD Pour changer le mod�le de ce commentaire de type g�n�r�, allez � :
 *         Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
 */
public class ArcadItemSelectionChangedProvider {
	// d�finir la liste des providers...
	// private static final SelectionChangedProvider applicationProvider =
	// new SelectionChangedProvider();

	/**
	 * @param event
	 */
	public void fireSelectionChanged(final SelectionChangedEvent event) {

		// Object sel =
		// ((IStructuredSelection)event.getSelection()).getFirstElement();

		// En fonction de "sel" faire le traitement adequat
		// if ( (sel instanceof IApplicationLinked) &&
		// !(sel instanceof IVersionLinked) &&
		// !(sel instanceof FunctionalItem))
		// applicationProvider.fireSelectionChanged(event);

	}

	// gestion des listeners...
	// public void
	// addApplicationSelectionChangedListener(ISelectionChangedListener
	// listener) {
	// applicationProvider.addSelectionChangedListener(listener);
	// }
	// public void
	// removeApplicationSelectionChangedListener(ISelectionChangedListener
	// listener) {
	// applicationProvider.removeSelectionChangedListener(listener);
	// }
}
