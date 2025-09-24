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
 * Cr�� le 11 juin 04
 *
 * Pour changer le mod�le de ce fichier g�n�r�, allez � :
 * Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
 */
package com.arcadsoftware.aev.core.ui.contentproviders;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

import com.arcadsoftware.aev.core.collections.ArcadCollection;
import com.arcadsoftware.aev.core.collections.IArcadCollectionItem;

/**
 * @author MD Pour changer le mod�le de ce commentaire de type g�n�r�, allez � :
 *         Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
 */
public class HierarchicTreeContentProvider implements ITreeContentProvider {
	protected static final Object[] EMPTYARRAY = new Object[0];
	private int firstDisplayLevel = 1;

	/**
	 *
	 */
	public HierarchicTreeContentProvider() {
		this(1);
	}

	public HierarchicTreeContentProvider(final int firstDisplayLevel) {
		super();
		this.firstDisplayLevel = firstDisplayLevel;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.jface.viewers.IContentProvider#dispose()
	 */
	@Override
	public void dispose() {
		// Do nothing
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ITreeContentProvider#getChildren(java.lang. Object)
	 */
	@Override
	public Object[] getChildren(final Object parentElement) {
		if (parentElement instanceof IArcadCollectionItem) {
			final IArcadCollectionItem i = (IArcadCollectionItem) parentElement;
			if (i.getParent() == null) {
				return EMPTYARRAY;
			}
			return i.getParent().getChildren(i);
		}
		return EMPTYARRAY;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.jface.viewers.IStructuredContentProvider#getElements(java .lang.Object)
	 */
	@Override
	public Object[] getElements(final Object inputElement) {
		if (inputElement instanceof ArcadCollection) {
			final ArcadCollection c = (ArcadCollection) inputElement;
			if (c.count() > 0) {
				final Object[] o = c.getElementsByLevel(firstDisplayLevel);
				return o;
			}
			return EMPTYARRAY;
		}
		return EMPTYARRAY;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ITreeContentProvider#getParent(java.lang.Object )
	 */
	@Override
	public Object getParent(final Object element) {
		if (element instanceof IArcadCollectionItem) {
			final IArcadCollectionItem i = (IArcadCollectionItem) element;
			if (i.getParent() == null) {
				return null;
			}
			return i.getParent().getParent(i);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ITreeContentProvider#hasChildren(java.lang. Object)
	 */
	@Override
	public boolean hasChildren(final Object element) {
		if (element instanceof IArcadCollectionItem) {
			final IArcadCollectionItem i = (IArcadCollectionItem) element;
			if (i.getParent() == null) {
				return false;
			}
			return i.getParent().hasChildren(i);
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.jface.viewers.IContentProvider#inputChanged(org.eclipse.jface .viewers.Viewer, java.lang.Object,
	 * java.lang.Object)
	 */
	@Override
	public void inputChanged(final Viewer viewer, final Object oldInput, final Object newInput) {
		// Do nothing
	}

}
