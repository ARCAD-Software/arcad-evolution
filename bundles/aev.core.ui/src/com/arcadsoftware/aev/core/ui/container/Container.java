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
 * @author MD
 * 
 *         Pour changer le modèle de ce commentaire de type généré, allez à :
 *         Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et
 *         commentaires
 */
public abstract class Container implements IContainer, IRefreshControl {

	private boolean actionMerged = false;
	private ContainerProvider parent = null;
	private Container parentContainer = null;
	protected ArcadActions actions = null;
	protected StructuredViewer viewer;
	protected Action filterAction;
	protected boolean enableFilted = false;

	public Container(ContainerProvider parent) {
		super();
		this.parent = parent;
	}
	
	public void setParent(ContainerProvider parent){
		this.parent = parent;
	}
	
	public void setParent(Container parent){
		this.parentContainer = parent;
	}
	
	public Container(Container parent) {
		super();
		this.parentContainer = parent;
	}

	private ArcadActions getActions() {
		if (enableFilted) {
			if (actions == null)
				actions = new ContainerActions(this);
			else if (!actionMerged)
				actions.mergeAction(new ContainerActions(this));
			actionMerged = true;
		}
		return actions;
	}

	public StructuredViewer getViewer() {
		return getParent().getViewer();
	}

	public IContainer getParent() {
		return (parent != null ? parent : parentContainer);
	}

	public boolean isDragable() {
		return false;
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
		if (getActions() != null)
			getActions().fillMenuAction(manager);
	}

	public void manageToolbarAction(IToolBarManager manager) {
		if (getActions() != null)
			getActions().fillToolbarAction(manager);
	}
	
	@Override
	public Object getAdapter(Class adapter) {
		return null;
	}

	public boolean isPropertyMenuVisible() {
		return false;
	}

	public RootContainerInput getRootContainerInput() {
		if (getViewer() != null && getViewer().getInput() instanceof RootContainerInput)
			return (RootContainerInput) getViewer().getInput();
		return null;
	}

	public int getIdentifier() {
		return -1;
	}

	public void setActions(ArcadActions actions) {
		this.actions = actions;
	}

	public ArcadActions getArcadActions() {
		return actions;
	}
}
