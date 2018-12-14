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
 * @author MD
 * 
 *         Pour changer le modèle de ce commentaire de type généré, allez à :
 *         Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et
 *         commentaires
 */
public class RootContainer extends Container {

	protected String rootLabel;
	protected Image rootImage;
	protected Object[] children;

	public RootContainer(ContainerProvider parent, StructuredViewer viewer, String rootLabel, Image rootImage,
			Object[] children) {
		super(parent);
		this.viewer = viewer;
		this.rootLabel = rootLabel;
		this.rootImage = rootImage;
		this.children = children;
	}

	public String getLabel() {
		return rootLabel;
	}

	public Object[] getChildren() {
		return children;
	}

	public boolean hasChildren() {
		return true;
	}

	public String getUniqueKey() {
		if (getParent() != null)
			return getParent().getUniqueKey().concat("/ROOT"); //$NON-NLS-1$
		return "/ROOT"; //$NON-NLS-1$
	}

	public void refresh() {
		getViewer().refresh();
	}

	@Override
	public StructuredViewer getViewer() {
		return viewer;
	}

	public Image getImage() {
		return rootImage;
	}
}
