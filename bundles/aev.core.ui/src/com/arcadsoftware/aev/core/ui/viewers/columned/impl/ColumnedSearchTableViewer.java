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
package com.arcadsoftware.aev.core.ui.viewers.columned.impl;

import java.util.Arrays;
import java.util.List;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

import com.arcadsoftware.aev.core.tools.StringTools;
import com.arcadsoftware.aev.core.ui.columned.cellmodifier.ColumnedSearchCellModifier;
import com.arcadsoftware.aev.core.ui.columned.model.ArcadColumns;
import com.arcadsoftware.aev.core.ui.columned.model.ColumnedSearchCriteria;
import com.arcadsoftware.aev.core.ui.columned.model.ColumnedSearchCriteriaList;
import com.arcadsoftware.aev.core.ui.tools.CoreUILabels;
import com.arcadsoftware.aev.core.ui.viewers.sorters.ColumnedCriteriaSorter;

/**
 * @author dlelong
 */
public class ColumnedSearchTableViewer extends TableViewer {

	class ColumnedSearchDialogContentProvider implements IStructuredContentProvider {
		@Override
		public void dispose() {
			// Do nothing
		}

		@Override
		public Object[] getElements(final Object parent) {
			if (parent instanceof ColumnedSearchCriteriaList) {
				final ColumnedSearchCriteriaList l = (ColumnedSearchCriteriaList) parent;
				return l.getCriteria().toArray();
			}
			return new Object[0];
		}

		@Override
		public void inputChanged(final Viewer v, final Object oldInput, final Object newInput) {
			// Do nothing
		}

	}

	public class ColumnedSearchDialogLabelProvider extends LabelProvider implements ITableLabelProvider {

		@Override
		public Image getColumnImage(final Object element, final int columnIndex) {
			return null;
		}

		@Override
		public String getColumnText(final Object element, final int columnIndex) {
			final ColumnedSearchCriteria criteria = (ColumnedSearchCriteria) element;
			switch (columnIndex) {
			case 0:
				return String.valueOf(criteria.getId());
			case 1:
				return criteria.getColumnName();
			case 2:
				return criteria.getOperator();
			case 3:
				return criteria.getKeyword();
			default:
				return StringTools.EMPTY;
			}
		}
	}

	private static final String COL_COLUMN_NAME = "Columns"; //$NON-NLS-1$
	public static final String COL_ID = "ID"; //$NON-NLS-1$

	public static final String COL_KEYWORD = "Keyword"; //$NON-NLS-1$

	private static final String COL_OPERATOR = "Operator"; //$NON-NLS-1$

	private final String[] columnNames = new String[] { COL_ID, COL_COLUMN_NAME, COL_OPERATOR, COL_KEYWORD };

	private final ArcadColumns referenceColumns;

	public ColumnedSearchTableViewer(final Composite parent, final int style, final ArcadColumns referenceColumns) {
		super(parent, style);
		this.referenceColumns = referenceColumns;
		addChildControls(parent);
	}

	/**
	 * Create a new shell, add the widgets, open the shell
	 */
	private void addChildControls(final Composite composite) {
		createTable(composite);
		createTableViewer();
		setContentProvider(new ColumnedSearchDialogContentProvider());
		setLabelProvider(new ColumnedSearchDialogLabelProvider());
	}

	/**
	 * Create the Table
	 *
	 * @param parent
	 */
	private void createTable(final Composite parent) {
		final Table table = getTable();
		if (parent.getLayout() instanceof GridLayout) {
			final GridData gridData = new GridData(GridData.FILL_BOTH);
			gridData.grabExcessVerticalSpace = true;
			gridData.horizontalSpan = 3;
			table.setLayoutData(gridData);
		}
		table.setLinesVisible(true);
		table.setHeaderVisible(true);

		// 1�re colonne
		TableColumn column = new TableColumn(table, SWT.NONE);
		column.setText("ID"); //$NON-NLS-1$
		column.setWidth(23);
		column.setAlignment(SWT.CENTER);
		column.setResizable(false);
		column.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				setSorter(new ColumnedCriteriaSorter());
			}
		});

		// 2�me colonne
		column = new TableColumn(table, SWT.NONE);
		column.setText(CoreUILabels.resString("tableColumn.clm.Columns")); //$NON-NLS-1$
		column.setWidth(189);
		column.setResizable(true);

		// 3�me colonne
		column = new TableColumn(table, SWT.NONE);
		column.setText(CoreUILabels.resString("tableColumn.clm.OperatorType")); //$NON-NLS-1$
		column.setWidth(95);
		column.setResizable(true);

		// 4�me colonne
		column = new TableColumn(table, SWT.NONE);
		column.setText(CoreUILabels.resString("tableColumn.clm.ValueSearch")); //$NON-NLS-1$
		column.setWidth(130);
		column.setResizable(true);
	}

	/**
	 * Create the TableViewer
	 */
	private void createTableViewer() {
		setColumnProperties(columnNames);
		final CellEditor[] editors = new CellEditor[columnNames.length];
		editors[0] = null;
		editors[1] = new ComboBoxCellEditor(getTable(), referenceColumns.getUserNameValues(), SWT.READ_ONLY);
		editors[2] = new ComboBoxCellEditor(getTable(), ColumnedSearchCriteriaList.getOperatorArray(), SWT.READ_ONLY);
		editors[3] = new TextCellEditor(getTable(), SWT.MULTI);
		setCellEditors(editors);
		setCellModifier(new ColumnedSearchCellModifier(this, referenceColumns));
		setSorter(new ColumnedCriteriaSorter());
	}

	public String[] getChoices(final String property) {
		if (COL_COLUMN_NAME.equals(property)) {
			return referenceColumns.getUserNameValues();
		} else if (COL_OPERATOR.equals(property)) {
			return ColumnedSearchCriteriaList.getOperatorArray();
		} else {
			return new String[] {};
		}
	}

	public List<String> getColumnNames() {
		return Arrays.asList(columnNames);
	}
}
