/*******************************************************************************
 * Copyright (c) 2025 ARCAD Software.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     ARCAD Software - initial API and implementation
 *******************************************************************************/
/*
 * Created on Jul 26, 2006
 */
package com.arcadsoftware.aev.core.ui.wizards;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

import com.arcadsoftware.aev.core.ui.tools.GuiFormatTools;

/**
 * @author MD
 */
public abstract class AbstractSelectionPage extends AbstractSimpleItemWizardPage {

	protected class SelectionChangedListener implements ISelectionChangedListener {
		@Override
		public void selectionChanged(final SelectionChangedEvent event) {
			setPageComplete(false);
			if (event.getSelection().isEmpty()) {
				return;
			}
			if (event.getSelection() instanceof IStructuredSelection) {
				doOnSelect((IStructuredSelection) event.getSelection());
				setPageComplete(checkSelection((IStructuredSelection) event.getSelection()));
			}
		}

	}

	Action doubleClickAction = null;
	protected SelectionChangedListener selectionChangedListener;

	protected StructuredViewer viewer;

	/**
	 * @param pageName
	 * @param title
	 * @param description
	 */
	public AbstractSelectionPage(final String pageName, final String title, final String description) {
		super(pageName, title, description);
	}

	public abstract boolean checkSelection(IStructuredSelection sel);

	/*
	 * (non-Javadoc)
	 * @seecom.arcadsoftware.aev.core.ui.wizards.AbstractSimpleItemWizardPage#
	 * createControlPage(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	protected void createControlPage(final Composite parent) {
		final Composite composite = GuiFormatTools.createComposite(parent);
		createExtendedControlBefore(composite);
		makeViewer(composite);
		if (viewer != null) {
			if (viewer.getControl().getParent().getLayout() instanceof GridLayout) {
				final GridData gridData = new GridData();
				gridData.verticalAlignment = GridData.FILL;
				gridData.horizontalAlignment = GridData.FILL;
				gridData.grabExcessVerticalSpace = true;
				gridData.grabExcessHorizontalSpace = true;
				gridData.horizontalSpan = 3;
				viewer.getControl().setLayoutData(gridData);
			}
			selectionChangedListener = new SelectionChangedListener();
			viewer.addSelectionChangedListener(selectionChangedListener);
		}
		createExtendedControlAfter(composite);
		setControl(composite);

		if (doubleClickAction == null) {
			doubleClickAction = new Action() {
				@Override
				public void run() {
					doOnDoubleClick();
				}
			};
		}
		hookDoubleClickAction();
	}

	/**
	 * @param parent
	 */
	protected void createExtendedControlAfter(final Composite parent) {
		// Do nothing
	}

	/**
	 * @param parent
	 */
	protected void createExtendedControlBefore(final Composite parent) {
		// Do nothing
	}

	protected void doOnDoubleClick() {
		// Do nothing
	}

	/**
	 * @param sel
	 */
	protected void doOnSelect(final IStructuredSelection sel) {
		// Do nothing
	}

	private void hookDoubleClickAction() {
		viewer.addDoubleClickListener(event -> doubleClickAction.run());
	}

	public abstract void makeInput();

	public abstract void makeViewer(Composite composite);

}
