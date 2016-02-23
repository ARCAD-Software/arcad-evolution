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
 * @author MD
 * 
 *         Pour changer le modèle de ce commentaire de type généré, allez à :
 *         Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et
 *         commentaires
 */
public abstract class ContainerProvider implements IContainer {

	protected ArcadActions actions = null;
	protected static final Object[] EMPTYARRAY = new Object[0];
	private Container parent;

	public ContainerProvider(Container parent) {
		super();
		this.parent = parent;
	}

	public IContainer getParent() {
		return parent;
	}

	//TODO [MDA] A reporter
	public void setParent(Container parent) {
		this.parent = parent;
	}

	
	
	public abstract Object[] getContainers();

	public Object[] getChildren() {
		return getContainers();
	}

	public boolean hasChildren() {
		return (getContainers().length > 0);
	}

	public boolean isDragable() {
		return true;
	}

	public boolean valideDrop(Object source) {
		return false;
	}

	public boolean valideDrop(TransferData source) {
		return false;
	}

	public boolean performDrop(IContainer source) {
		return false;
	}

	public void manageMenuAction(IMenuManager manager) {
		if (actions != null)
			actions.fillMenuAction(manager);
	}

	public void manageToolbarAction(IToolBarManager manager) {
		if (actions != null)
			actions.fillToolbarAction(manager);
	}

	public String getOverlayId() {
		return null;
	}

	@SuppressWarnings("unchecked")
	public Object getAdapter(Class adapter) {
		return null;
	}

	public boolean isPropertyMenuVisible() {
		return false;
	}

	public StructuredViewer getViewer() {
		return getParent().getViewer();
	}

	public Object[] getFilteredChildren() {
		return getContainers();
	}

	public boolean hasFilteredChildren() {
		return hasChildren();
	}

	public RootContainerInput getRootContainerInput() {
		if (getViewer() != null && getViewer().getInput() instanceof RootContainerInput)
			return (RootContainerInput) getViewer().getInput();
		return null;
	}
}
