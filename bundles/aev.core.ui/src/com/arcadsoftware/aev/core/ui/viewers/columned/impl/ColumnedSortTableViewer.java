package com.arcadsoftware.aev.core.ui.viewers.columned.impl;

import java.util.Arrays;
import java.util.List;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
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
import com.arcadsoftware.aev.core.ui.columned.cellmodifier.ColumnedSortCellModifier;
import com.arcadsoftware.aev.core.ui.columned.model.ArcadColumns;
import com.arcadsoftware.aev.core.ui.columned.model.ColumnedSortCriteria;
import com.arcadsoftware.aev.core.ui.columned.model.ColumnedSortCriteriaList;
import com.arcadsoftware.aev.core.ui.tools.CoreUILabels;
import com.arcadsoftware.aev.core.ui.viewers.sorters.ColumnedCriteriaSorter;

/**
 * @author dlelong
 */
public class ColumnedSortTableViewer extends TableViewer {

	class ColumnedSortDialogContentProvider implements IStructuredContentProvider {
		@Override
		public void dispose() {
			// Do nothing
		}

		@Override
		public Object[] getElements(final Object parent) {
			if (parent instanceof ColumnedSortCriteriaList) {
				final ColumnedSortCriteriaList l = (ColumnedSortCriteriaList) parent;
				return l.getCriteria().toArray();
			}
			return new Object[0];
		}

		@Override
		public void inputChanged(final Viewer v, final Object oldInput, final Object newInput) {
			// Do nothing
		}
	}

	public class ColumnedSortDialogLabelProvider extends LabelProvider implements ITableLabelProvider {

		@Override
		public Image getColumnImage(final Object element, final int columnIndex) {
			return null;
		}

		@Override
		public String getColumnText(final Object element, final int columnIndex) {
			final ColumnedSortCriteria criteria = (ColumnedSortCriteria) element;
			switch (columnIndex) {
			case 0:
				return String.valueOf(criteria.getId());
			case 1:
				return criteria.getColumnName();
			case 2:
				return criteria.getSortOrder();
			default:
				return StringTools.EMPTY;
			}
		}
	}

	private static final String COL_COLUMN_NAME = "Columns"; //$NON-NLS-1$

	public static final String COL_ID = "ID"; //$NON-NLS-1$

	private static final String COL_ORDER = "SortOrder"; //$NON-NLS-1$

	private final String[] columnNames = new String[] { COL_ID, COL_COLUMN_NAME, COL_ORDER };

	private final ArcadColumns referenceColumns;

	public ColumnedSortTableViewer(final Composite parent, final int style, final ArcadColumns referenceColumns) {
		super(parent, style);
		this.referenceColumns = referenceColumns;
		addChildControls(parent);
	}

	private void addChildControls(final Composite composite) {
		createTable(composite);
		createTableViewer();
		setContentProvider(new ColumnedSortDialogContentProvider());
		setLabelProvider(new ColumnedSortDialogLabelProvider());
	}

	/**
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

		column = new TableColumn(table, SWT.NONE);
		column.setText(CoreUILabels.resString("tableColumn.clm.Columns")); //$NON-NLS-1$
		column.setWidth(277);
		column.setResizable(false);

		column = new TableColumn(table, SWT.NONE);
		column.setText(CoreUILabels.resString("tableColumn.clm.SortOrder")); //$NON-NLS-1$
		column.setWidth(140);
		column.setResizable(false);
	}

	private void createTableViewer() {

		setColumnProperties(columnNames);
		final CellEditor[] editors = new CellEditor[columnNames.length];
		editors[0] = null;
		editors[1] = new ComboBoxCellEditor(getTable(), referenceColumns.getUserNameValues(), SWT.READ_ONLY);
		editors[2] = new ComboBoxCellEditor(getTable(), ColumnedSortCriteriaList.getSortOrderArray(), SWT.READ_ONLY);

		setCellEditors(editors);
		setCellModifier(new ColumnedSortCellModifier(this, referenceColumns));
		setSorter(new ColumnedCriteriaSorter());
	}

	public String[] getChoices(final String property) {
		if (COL_COLUMN_NAME.equals(property)) {
			return referenceColumns.getUserNameValues();
		}
		if (COL_ORDER.equals(property)) {
			return ColumnedSortCriteriaList.getSortOrderArray();
		}
		return new String[] {};
	}

	public List<String> getColumnNames() {
		return Arrays.asList(columnNames);
	}
}
