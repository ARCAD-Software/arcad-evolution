/*
 * Créé le 23 sept. 04
 *
 * Pour changer le modèle de ce fichier généré, allez à :
 * Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
package com.arcadsoftware.aev.core.ui.container;

import org.eclipse.jface.viewers.StructuredViewer;

import com.arcadsoftware.aev.core.ui.actions.ArcadActions;

/**
 * @author MD
 * 
 *         Pour changer le modèle de ce commentaire de type généré, allez à :
 *         Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et
 *         commentaires
 */
public abstract class RootContainerInput extends Container {

	protected RootContainer rootContainer;

	public RootContainerInput(StructuredViewer viewer) {
		super((Container) null);
		this.viewer = viewer;
		init(null);
	}

	protected void init(ContainerProvider parent) {
		rootContainer = new RootContainer(parent, viewer, getLabel(), getImage(), getChildren());
		rootContainer.setActions(getRootContainerActions());
	}

	public abstract ArcadActions getRootContainerActions();

	public boolean hasChildren() {
		return true;
	}

	public String getUniqueKey() {
		if (getParent() != null)
			return getParent().getUniqueKey().concat("/ROOT"); //$NON-NLS-1$
		return "/ROOT"; //$NON-NLS-1$
	}

	public void refresh() {
		viewer.refresh();
	}

	@Override
	public StructuredViewer getViewer() {
		return viewer;
	}

	public RootContainer getRootContainer() {
		return rootContainer;
	}
}
