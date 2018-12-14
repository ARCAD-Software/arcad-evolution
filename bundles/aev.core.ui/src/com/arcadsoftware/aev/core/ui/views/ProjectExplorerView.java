/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package com.arcadsoftware.aev.core.ui.views;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.IPartListener2;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchPartReference;
import org.eclipse.ui.part.ViewPart;

import com.arcadsoftware.aev.core.tools.StringTools;
import com.arcadsoftware.aev.core.ui.EvolutionCoreUIPlugin;
import com.arcadsoftware.aev.core.ui.actions.ArcadPropertyDialogAction;
import com.arcadsoftware.aev.core.ui.container.Container;
import com.arcadsoftware.aev.core.ui.container.ContainerProvider;
import com.arcadsoftware.aev.core.ui.container.IContainer;
import com.arcadsoftware.aev.core.ui.container.RootContainerInput;
import com.arcadsoftware.aev.core.ui.listeners.IContainerSelectListener;
import com.arcadsoftware.aev.core.ui.mementos.DefaultExplorerMementoTools;
import com.arcadsoftware.aev.core.ui.mementos.ExplorerMementoTools;
import com.arcadsoftware.aev.core.ui.tools.CoreUILabels;
import com.arcadsoftware.aev.core.ui.treeviewers.ContainerTreeViewer;

/**
 * This sample class demonstrates how to plug-in a new workbench view. The view
 * shows data obtained from the model. The sample creates a dummy model on the
 * fly, but a real implementation would connect to the model available either in
 * this or another plug-in (e.g. the workspace). The view is connected to the
 * model using a content provider.
 * <p>
 * The view uses a label provider to define how model objects should be
 * presented in the view. Each view can present the same model objects using
 * different labels and icons, if needed. Alternatively, a single label provider
 * can be shared between views in order to ensure that objects of the same type
 * are presented in the same way everywhere.
 * <p>
 */
public abstract class ProjectExplorerView extends ArcadTableView implements IPartListener2, IContainerSelectListener {

	private static final int[] defaultContainer = new int[] {
	// Mettre ici les identifiants des container autorisés
	};
	private Action refreshAction;
	protected int[] containerStyle;
	RootContainerInput sc;
	ExplorerMementoTools explorerMemento;

	// TODO [DL] regarder pourquoi la sauvegarde du deploiement et la relecture
	// ne marche plus...

	public ProjectExplorerView(int style, int[] containerStyle) {
		super(style);
		this.containerStyle = containerStyle;
	}

	public ProjectExplorerView(int style) {
		this(style, defaultContainer);
	}

	/**
	 * This is a callback that will allow us to create the viewer and initialize
	 * it.
	 */
	@Override
	public void createPartControl(Composite parent) {
		setViewName("ProjectExplorerView"); //$NON-NLS-1$
		viewer = initializeContainerTreeViewer(parent);
		((ContainerTreeViewer) viewer).setView(this);
		super.createPartControl(parent);
		sc = defineInput();
		viewer.setInput(sc);
		// explorerMemento = createMementoTools();

		this.getSite().getWorkbenchWindow().getPartService().addPartListener(this);
		restoreState();
	}

	/**
	 * Méthode permettant de rattacher un ExplorerMementoTools spécifique Le
	 * DefaultExplorerMementoTools ici ne fait aucune sauvegarde
	 * 
	 * @return
	 */
	protected ExplorerMementoTools createMementoTools() {
		return new DefaultExplorerMementoTools(getViewId());
	}

	protected abstract RootContainerInput defineInput();

	protected ContainerTreeViewer initializeContainerTreeViewer(Composite parent) {
		return new ContainerTreeViewer(parent, SWT.SINGLE | SWT.H_SCROLL | SWT.V_SCROLL, containerStyle);
	}

	@Override
	protected void defineLocalContextMenu(IMenuManager manager) {
		IContainer c = ((ContainerTreeViewer) viewer).getSelectedElement();
		if (c != null) {
			c.manageMenuAction(manager);
			if (c instanceof Container) {
				manager.add(new Separator());
				manager.add(refreshAction);
			}
			if (c instanceof ContainerProvider) {
				if (((ContainerProvider) c).isPropertyMenuVisible()) {
					showPropertiesAction = new ArcadPropertyDialogAction(EvolutionCoreUIPlugin.getDefault()
							.getShellProvider(), viewer.getViewer());
					showPropertiesAction.setText(CoreUILabels.resString("ArcadTableView.properties.Text")); //$NON-NLS-1$
					showPropertiesAction.setToolTipText(CoreUILabels.resString("ArcadTableView.properties.Tooltip")); //$NON-NLS-1$
					showPropertiesAction.setImageDescriptor(CoreUILabels
							.getImageDescriptor(EvolutionCoreUIPlugin.ACT_PROPERTIES));
					manager.add(showPropertiesAction);
				}
			}
		}
		// Other plug-ins can contribute there actions here
		manager.add(new Separator("Additions")); //$NON-NLS-1$
	}

	@Override
	protected void defineLocalPullDown(IMenuManager manager) {
		IContainer c = ((ContainerTreeViewer) viewer).getSelectedElement();
		if (c != null)
			c.manageMenuAction(manager);
	}

	@Override
	protected void defineLocalToolbar(IToolBarManager manager) {
		IContainer c = ((ContainerTreeViewer) viewer).getSelectedElement();
		if (c != null)
			c.manageToolbarAction(manager);
	}

	@Override
	protected void defineActions() {
		refreshAction = new Action() {
			@Override
			public void run() {
				IContainer c = ((ContainerTreeViewer) viewer).getSelectedElement();
				((ContainerTreeViewer) viewer).refresh(c);
			}
		};
		refreshAction.setText(CoreUILabels.resString("ProjectExplorerView.refreshAction.Text")); //$NON-NLS-1$
		refreshAction.setToolTipText(CoreUILabels.resString("ProjectExplorerView.refreshAction.Tooltips")); //$NON-NLS-1$
		refreshAction.setImageDescriptor(CoreUILabels.getImageDescriptor(EvolutionCoreUIPlugin.ACT_REFRESH));
	}

	@Override
	protected void doOnDoubleClick() {
		IStructuredSelection sel = viewer.getSelection();
		Object o = sel.getFirstElement();
		if (o instanceof IContainer)
			doOnDoubleClickonContainer((IContainer) o);
	}

	protected abstract void doOnDoubleClickonContainer(IContainer o);

	public abstract void doOnSelectContainer(Object o);

	protected String getViewId() {
		return StringTools.EMPTY;
	}

	@Override
	public void saveState(IMemento memento) {
		saveKey();
		saveViewId();
		saveState();
	}

	public void saveKey() {
		// if (!viewer.getSelection().isEmpty()){
		// IStructuredSelection selection =
		// (IStructuredSelection)getViewer().getSelection();
		// Object o = selection.getFirstElement();
		// String key = ((IContainer)o).getUniqueKey();
		// explorerMemento.setKeyValue(key);
		// } else
		//			explorerMemento.setKeyValue("");//$NON-NLS-1$				
	}

	public void saveViewId() {
		// explorerMemento.setViewId(getViewId());
	}

	public void saveState() {
		// explorerMemento.save();
	}

	@Override
	public void restoreState() {
		// explorerMemento.restore();
		// if (explorerMemento.getKeyValue()!=null)
		// ((ContainerTreeViewer)viewer).expandFromKey(explorerMemento.getKeyValue());
	}

	@Override
	public void dispose() {
		this.getSite().getWorkbenchWindow().getPartService().removePartListener(this);
		super.dispose();
	}

	public void partClosed(IWorkbenchPartReference ref) {
		saveState();
	}

	/**
	 * @param part
	 */
	public void partClosed(IWorkbenchPart part) {
		saveState();
	}

	/**
	 * @param part
	 */
	public void partActivated(IWorkbenchPart part) {
		// Do nothing
	}

	/**
	 * @param part
	 */
	public void partBroughtToTop(IWorkbenchPart part) {
		// Do nothing
	}

	/**
	 * @param part
	 */
	public void partDeactivated(IWorkbenchPart part) {
		// Do nothing
	}

	/**
	 * @param part
	 */
	public void partOpened(IWorkbenchPart part) {
		// Do nothing
	}

	public void partActivated(IWorkbenchPartReference ref) {
		// Do nothing
	}

	public void partBroughtToTop(IWorkbenchPartReference ref) {
		// Do nothing
	}

	public void partDeactivated(IWorkbenchPartReference ref) {
		// Do nothing
	}

	public void partOpened(IWorkbenchPartReference ref) {
		// Do nothing
	}

	public void partHidden(IWorkbenchPartReference ref) {
		saveKey();
		saveViewId();
	}

	public void partVisible(IWorkbenchPartReference ref) {
		// Do nothing
	}

	public void partInputChanged(IWorkbenchPartReference ref) {
		// Do nothing
	}

	public ViewPart getView() {
		return this;
	}
}