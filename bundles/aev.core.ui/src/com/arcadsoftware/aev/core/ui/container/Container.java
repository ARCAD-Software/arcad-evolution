/*
 * Créé le 13 mai 04
 *
 * Pour changer le modèle de ce fichier généré, allez à :
 * Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
package com.arcadsoftware.aev.core.ui.container;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.swt.dnd.TransferData;

import com.arcadsoftware.aev.core.ui.actions.ArcadActions;
import com.arcadsoftware.aev.core.ui.actions.ContainerActions;
import com.arcadsoftware.aev.core.ui.actions.IRefreshControl;

/**
 * @author MD Pour changer le modèle de ce commentaire de type généré, allez à :
 *         Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
public abstract class Container implements IContainer, IRefreshControl {

	private boolean actionMerged = false;
	protected ArcadActions actions = null;
	protected boolean enableFilted = false;
	protected Action filterAction;
	private ContainerProvider parent = null;
	private Container parentContainer = null;
	protected StructuredViewer viewer;

	public Container(final Container parent) {
		super();
		parentContainer = parent;
	}

	public Container(final ContainerProvider parent) {
		super();
		this.parent = parent;
	}

	private ArcadActions getActions() {
		if (enableFilted) {
			if (actions == null) {
				actions = new ContainerActions(this);
			} else if (!actionMerged) {
				actions.mergeAction(new ContainerActions(this));
			}
			actionMerged = true;
		}
		return actions;
	}

	@Override
	public <T> T getAdapter(final Class<T> clazz) {
		return null;
	}

	public ArcadActions getArcadActions() {
		return actions;
	}

	public int getIdentifier() {
		return -1;
	}

	@Override
	public IContainer getParent() {
		return parent != null ? parent : parentContainer;
	}

	public RootContainerInput getRootContainerInput() {
		if (getViewer() != null && getViewer().getInput() instanceof RootContainerInput) {
			return (RootContainerInput) getViewer().getInput();
		}
		return null;
	}

	@Override
	public StructuredViewer getViewer() {
		return getParent().getViewer();
	}

	@Override
	public boolean isDragable() {
		return false;
	}

	@Override
	public boolean isPropertyMenuVisible() {
		return false;
	}

	@Override
	public void manageMenuAction(final IMenuManager manager) {
		if (getActions() != null) {
			getActions().fillMenuAction(manager);
		}
	}

	@Override
	public void manageToolbarAction(final IToolBarManager manager) {
		if (getActions() != null) {
			getActions().fillToolbarAction(manager);
		}
	}

	@Override
	public boolean performDrop(final IContainer source) {
		return false;
	}

	public void setActions(final ArcadActions actions) {
		this.actions = actions;
	}

	public void setParent(final Container parent) {
		parentContainer = parent;
	}

	public void setParent(final ContainerProvider parent) {
		this.parent = parent;
	}

	@Override
	public boolean valideDrop(final Object source) {
		return false;
	}

	@Override
	public boolean valideDrop(final TransferData source) {
		return false;
	}
}
