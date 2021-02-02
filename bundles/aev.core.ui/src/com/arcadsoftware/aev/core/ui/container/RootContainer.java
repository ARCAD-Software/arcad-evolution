/*
 * Créé le 13 mai 04
 *
 * Pour changer le modèle de ce fichier généré, allez à :
 * Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
package com.arcadsoftware.aev.core.ui.container;

import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.swt.graphics.Image;

/**
 * @author MD Pour changer le modèle de ce commentaire de type généré, allez à :
 *         Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
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
