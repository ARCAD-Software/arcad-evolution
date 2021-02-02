/*
 * Créé le 13 mai 04
 *
 * Pour changer le modèle de ce fichier généré, allez à :
 * Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
package com.arcadsoftware.aev.core.ui.container;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.swt.dnd.TransferData;

import com.arcadsoftware.aev.core.ui.actions.ArcadActions;

/**
 * @author MD Pour changer le modèle de ce commentaire de type généré, allez à :
 *         Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
public abstract class ContainerProvider implements IContainer {

	protected static final Object[] EMPTYARRAY = new Object[0];
	protected ArcadActions actions = null;
	private Container parent;

	public ContainerProvider(final Container parent) {
		super();
		this.parent = parent;
	}

	@Override
	public <T> T getAdapter(final Class<T> clazz) {
		return null;
	}

	@Override
	public Object[] getChildren() {
		return getContainers();
	}

	public abstract Object[] getContainers();

	public Object[] getFilteredChildren() {
		return getContainers();
	}

	public String getOverlayId() {
		return null;
	}

	@Override
	public IContainer getParent() {
		return parent;
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
	public boolean hasChildren() {
		return getContainers().length > 0;
	}

	public boolean hasFilteredChildren() {
		return hasChildren();
	}

	@Override
	public boolean isDragable() {
		return true;
	}

	@Override
	public boolean isPropertyMenuVisible() {
		return false;
	}

	@Override
	public void manageMenuAction(final IMenuManager manager) {
		if (actions != null) {
			actions.fillMenuAction(manager);
		}
	}

	@Override
	public void manageToolbarAction(final IToolBarManager manager) {
		if (actions != null) {
			actions.fillToolbarAction(manager);
		}
	}

	@Override
	public boolean performDrop(final IContainer source) {
		return false;
	}

	// TODO [MDA] A reporter
	public void setParent(final Container parent) {
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
