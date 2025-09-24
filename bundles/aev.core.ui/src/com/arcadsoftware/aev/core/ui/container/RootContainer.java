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
 * Cr�� le 13 mai 04
 *
 * Pour changer le mod�le de ce fichier g�n�r�, allez � :
 * Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
 */
package com.arcadsoftware.aev.core.ui.container;

import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.swt.graphics.Image;

/**
 * @author MD Pour changer le mod�le de ce commentaire de type g�n�r�, allez � :
 *         Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
 */
public class RootContainer extends Container {

	protected Object[] children;
	protected Image rootImage;
	protected String rootLabel;

	public RootContainer(final ContainerProvider parent, final StructuredViewer viewer, final String rootLabel,
			final Image rootImage,
			final Object[] children) {
		super(parent);
		this.viewer = viewer;
		this.rootLabel = rootLabel;
		this.rootImage = rootImage;
		this.children = children;
	}

	@Override
	public Object[] getChildren() {
		return children;
	}

	@Override
	public Image getImage() {
		return rootImage;
	}

	@Override
	public String getLabel() {
		return rootLabel;
	}

	@Override
	public String getUniqueKey() {
		if (getParent() != null) {
			return getParent().getUniqueKey().concat("/ROOT"); //$NON-NLS-1$
		}
		return "/ROOT"; //$NON-NLS-1$
	}

	@Override
	public StructuredViewer getViewer() {
		return viewer;
	}

	@Override
	public boolean hasChildren() {
		return true;
	}

	@Override
	public void refresh() {
		getViewer().refresh();
	}
}
