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
package com.arcadsoftware.aev.core.ui.dialogs.columned;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;

import com.arcadsoftware.aev.core.ui.columned.model.ArcadColumns;
import com.arcadsoftware.aev.core.ui.columned.model.ColumnedSearchCriteriaList;
import com.arcadsoftware.aev.core.ui.tools.CoreUILabels;

/**
 * @author dlelong
 */
public class ColumnedFilterDialog extends ColumnedSearchDialog {

	public ColumnedFilterDialog(final Shell parentShell, final ColumnedSearchCriteriaList filters,
			final ArcadColumns displayedColumns) {
		super(parentShell, filters, displayedColumns);
		removeCriterionMessage = "messageBox.clm.removeFilterCriterionMessage"; //$NON-NLS-1$
	}

	@Override
	protected void configureShell(final Shell newShell) {
		super.configureShell(newShell);
		newShell.setText(CoreUILabels.resString("filterDialog.title")); //$NON-NLS-1$
	}

	@Override
	protected void createButtonsForButtonBar(final Composite parent) {
		okButton = createButton(parent, IDialogConstants.OK_ID, CoreUILabels.resString("button.clm.FilterButton"), //$NON-NLS-1$
				true);
		okButton.addSelectionListener(new SelectionAdapter() {
			@SuppressWarnings("synthetic-access")
			@Override
			public void widgetSelected(final SelectionEvent e) {
				okPressed();
			}
		});
		cancelButton = createButton(parent, IDialogConstants.CANCEL_ID, CoreUILabels
				.resString("button.clm.CancelButton"), true); //$NON-NLS-1$
		cancelButton.addSelectionListener(new SelectionAdapter() {
			@SuppressWarnings("synthetic-access")
			@Override
			public void widgetSelected(final SelectionEvent e) {
				cancelPressed();
			}
		});
	}
}
