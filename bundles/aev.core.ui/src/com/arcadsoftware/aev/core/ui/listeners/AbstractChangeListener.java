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
 * Cr�� le 18 mars 05
 *
 * Pour changer le mod�le de ce fichier g�n�r�, allez � :
 * Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
 */
package com.arcadsoftware.aev.core.ui.listeners;

import com.arcadsoftware.aev.core.model.ArcadListenerList;
import com.arcadsoftware.aev.core.ui.controlers.IContentChangeListener;

/**
 * @author MD
 */
public class AbstractChangeListener {
	private final ArcadListenerList contentChangedListeners = new ArcadListenerList(3);

	/**
	 *
	 */
	public AbstractChangeListener() {
		super();
	}

	public void addContentChangedListener(final IContentChangeListener listener) {
		contentChangedListeners.add(listener);
	}

	public void fireContentChanged() {
		final Object[] listeners = contentChangedListeners.getListeners();
		for (final Object listener : listeners) {
			final IContentChangeListener l = (IContentChangeListener) listener;
			try {
				l.contentChanged();
			} catch (final RuntimeException e1) {
				removeContentChangedListener(l);
			}
		}
	}

	/*
	 * (non-Javadoc) Method declared on ISelectionProvider.
	 */
	public void removeContentChangedListener(final IContentChangeListener listener) {
		contentChangedListeners.remove(listener);
	}
}
