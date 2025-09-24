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
 * Created on 2 mars 2006
 *
 */
package com.arcadsoftware.aev.core.ui.dialogs;

import org.eclipse.jface.preference.IPreferenceNode;
import org.eclipse.jface.preference.IPreferencePage;
import org.eclipse.jface.preference.PreferenceManager;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.internal.dialogs.PropertyDialog;

import com.arcadsoftware.aev.core.ui.propertypages.ArcadPropertyPage;

/**
 * @author MD
 */
public class ArcadPropertyDialog extends PropertyDialog implements ISelectionChangedListener {

	/**
	 * @param parentShell
	 * @param mng
	 * @param selection
	 */
	@SuppressWarnings("restriction")
	public ArcadPropertyDialog(final Shell parentShell, final PreferenceManager mng, final ISelection selection) {
		super(parentShell, mng, selection);
	}

	public void doAfterCreation() {
		getTreeViewer().addPostSelectionChangedListener(this);
		// Comme on n'enregistre cette page qu'apr�s sa cr�ation, pour �viter
		// qu'elle ne prenne la taille de
		// sa liste � la cr�ation, elle n'a pas re�u l'�v�nement pour la 1�re
		// s�lection (celle par d�faut)
		// On lui envoie donc cet �v�nement.
		getTreeViewer().setSelection(getTreeViewer().getSelection());
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ISelectionChangedListener#selectionChanged(
	 * org.eclipse.jface.viewers.SelectionChangedEvent)
	 */
	@Override
	public void selectionChanged(final SelectionChangedEvent event) {
		try {
			final TreeViewer tv = getTreeViewer();
			final StructuredSelection selection = (StructuredSelection) tv.getSelection();
			if (!selection.isEmpty()) {
				final Object o = selection.getFirstElement();
				final IPreferencePage p = ((IPreferenceNode) o).getPage();
				if (p instanceof ArcadPropertyPage) {
					((ArcadPropertyPage) p).doAfterCreating();
				}
			}
		} catch (final Exception e) {
			// Do nothing
		}
	}
}
