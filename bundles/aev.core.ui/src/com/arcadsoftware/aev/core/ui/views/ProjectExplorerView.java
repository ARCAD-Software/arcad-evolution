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
import com.arcadsoftware.aev.icons.Icon;

/**
 * This sample class demonstrates how to plug-in a new workbench view. The view shows data obtained from the model. The
 * sample creates a dummy model on the fly, but a real implementation would connect to the model available either in
 * this or another plug-in (e.g. the workspace). The view is connected to the model using a content provider.
 * <p>
 * The view uses a label provider to define how model objects should be presented in the view. Each view can present the
 * same model objects using different labels and icons, if needed. Alternatively, a single label provider can be shared
 * between views in order to ensure that objects of the same type are presented in the same way everywhere.
 * <p>
 */
public abstract class ProjectExplorerView extends ArcadTableView implements IPartListener2, IContainerSelectListener {

	private static final int[] defaultContainer = new int[] {
			// Mettre ici les identifiants des container autorisés
	};
	protected int[] containerStyle;
	ExplorerMementoTools explorerMemento;
	private Action refreshAction;
	RootContainerInput sc;

	// TODO [DL] regarder pourquoi la sauvegarde du deploiement et la relecture
	// ne marche plus...

	public ProjectExplorerView(final int style) {
		this(style, defaultContainer);
	}

	public ProjectExplorerView(final int style, final int[] containerStyle) {
		super(style);
		this.containerStyle = containerStyle;
	}

	/**
	 * Méthode permettant de rattacher un ExplorerMementoTools spécifique Le DefaultExplorerMementoTools ici ne fait
	 * aucune sauvegarde
	 *
	 * @return
	 */
	protected ExplorerMementoTools createMementoTools() {
		return new DefaultExplorerMementoTools(getViewId());
	}

	/**
	 * This is a callback that will allow us to create the viewer and initialize it.
	 */
	@Override
	public void createPartControl(final Composite parent) {
		setViewName("ProjectExplorerView"); //$NON-NLS-1$
		viewer = initializeContainerTreeViewer(parent);
		((ContainerTreeViewer) viewer).setView(this);
		super.createPartControl(parent);
		sc = defineInput();
		viewer.setInput(sc);
		// explorerMemento = createMementoTools();

		getSite().getWorkbenchWindow().getPartService().addPartListener(this);
		restoreState();
	}

	@Override
	protected void defineActions() {
		refreshAction = new Action() {
			@Override
			public void run() {
				final IContainer c = ((ContainerTreeViewer) viewer).getSelectedElement();
				((ContainerTreeViewer) viewer).refresh(c);
			}
		};
		refreshAction.setText(CoreUILabels.resString("ProjectExplorerView.refreshAction.Text")); //$NON-NLS-1$
		refreshAction.setToolTipText(CoreUILabels.resString("ProjectExplorerView.refreshAction.Tooltips")); //$NON-NLS-1$
		refreshAction.setImageDescriptor(Icon.REFRESH.imageDescriptor());
	}

	protected abstract RootContainerInput defineInput();

	@Override
	protected void defineLocalContextMenu(final IMenuManager manager) {
		final IContainer c = ((ContainerTreeViewer) viewer).getSelectedElement();
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
					showPropertiesAction.setImageDescriptor(Icon.PROPERTIES.imageDescriptor());
					manager.add(showPropertiesAction);
				}
			}
		}
		// Other plug-ins can contribute there actions here
		manager.add(new Separator("Additions")); //$NON-NLS-1$
	}

	@Override
	protected void defineLocalPullDown(final IMenuManager manager) {
		final IContainer c = ((ContainerTreeViewer) viewer).getSelectedElement();
		if (c != null) {
			c.manageMenuAction(manager);
		}
	}

	@Override
	protected void defineLocalToolbar(final IToolBarManager manager) {
		final IContainer c = ((ContainerTreeViewer) viewer).getSelectedElement();
		if (c != null) {
			c.manageToolbarAction(manager);
		}
	}

	@Override
	public void dispose() {
		getSite().getWorkbenchWindow().getPartService().removePartListener(this);
		super.dispose();
	}

	@Override
	protected void doOnDoubleClick() {
		final IStructuredSelection sel = viewer.getSelection();
		final Object o = sel.getFirstElement();
		if (o instanceof IContainer) {
			doOnDoubleClickonContainer((IContainer) o);
		}
	}

	protected abstract void doOnDoubleClickonContainer(IContainer o);

	@Override
	public abstract void doOnSelectContainer(Object o);

	public ViewPart getView() {
		return this;
	}

	protected String getViewId() {
		return StringTools.EMPTY;
	}

	protected ContainerTreeViewer initializeContainerTreeViewer(final Composite parent) {
		return new ContainerTreeViewer(parent, SWT.SINGLE | SWT.H_SCROLL | SWT.V_SCROLL, containerStyle);
	}

	/**
	 * @param part
	 */
	public void partActivated(final IWorkbenchPart part) {
		// Do nothing
	}

	@Override
	public void partActivated(final IWorkbenchPartReference ref) {
		// Do nothing
	}

	/**
	 * @param part
	 */
	public void partBroughtToTop(final IWorkbenchPart part) {
		// Do nothing
	}

	@Override
	public void partBroughtToTop(final IWorkbenchPartReference ref) {
		// Do nothing
	}

	/**
	 * @param part
	 */
	public void partClosed(final IWorkbenchPart part) {
		saveState();
	}

	@Override
	public void partClosed(final IWorkbenchPartReference ref) {
		saveState();
	}

	/**
	 * @param part
	 */
	public void partDeactivated(final IWorkbenchPart part) {
		// Do nothing
	}

	@Override
	public void partDeactivated(final IWorkbenchPartReference ref) {
		// Do nothing
	}

	@Override
	public void partHidden(final IWorkbenchPartReference ref) {
		saveKey();
		saveViewId();
	}

	@Override
	public void partInputChanged(final IWorkbenchPartReference ref) {
		// Do nothing
	}

	/**
	 * @param part
	 */
	public void partOpened(final IWorkbenchPart part) {
		// Do nothing
	}

	@Override
	public void partOpened(final IWorkbenchPartReference ref) {
		// Do nothing
	}

	@Override
	public void partVisible(final IWorkbenchPartReference ref) {
		// Do nothing
	}

	@Override
	public void restoreState() {
		// explorerMemento.restore();
		// if (explorerMemento.getKeyValue()!=null)
		// ((ContainerTreeViewer)viewer).expandFromKey(explorerMemento.getKeyValue());
	}

	public void saveKey() {
		// if (!viewer.getSelection().isEmpty()){
		// IStructuredSelection selection =
		// (IStructuredSelection)getViewer().getSelection();
		// Object o = selection.getFirstElement();
		// String key = ((IContainer)o).getUniqueKey();
		// explorerMemento.setKeyValue(key);
		// } else
		// explorerMemento.setKeyValue("");//$NON-NLS-1$
	}

	public void saveState() {
		// explorerMemento.save();
	}

	@Override
	public void saveState(final IMemento memento) {
		saveKey();
		saveViewId();
		saveState();
	}

	public void saveViewId() {
		// explorerMemento.setViewId(getViewId());
	}
}