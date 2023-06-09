/*
 * Cr�� le 23 sept. 04
 *
 * Pour changer le mod�le de ce fichier g�n�r�, allez � :
 * Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
 */
package com.arcadsoftware.aev.core.ui.container;

import org.eclipse.jface.viewers.StructuredViewer;

import com.arcadsoftware.aev.core.ui.actions.ArcadActions;

/**
 * @author MD Pour changer le mod�le de ce commentaire de type g�n�r�, allez � :
 *         Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
 */
public abstract class RootContainerInput extends Container {

	protected RootContainer rootContainer;

	public RootContainerInput(final StructuredViewer viewer) {
		super((Container) null);
		this.viewer = viewer;
		init(null);
	}

	public RootContainer getRootContainer() {
		return rootContainer;
	}

	public abstract ArcadActions getRootContainerActions();

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

	protected void init(final ContainerProvider parent) {
		rootContainer = new RootContainer(parent, viewer, getLabel(), getImage(), getChildren());
		rootContainer.setActions(getRootContainerActions());
	}

	@Override
	public void refresh() {
		viewer.refresh();
	}
}
