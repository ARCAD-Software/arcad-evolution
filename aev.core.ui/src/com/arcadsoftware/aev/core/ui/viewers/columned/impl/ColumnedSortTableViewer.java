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

	public static final String COL_ID = "ID"; //$NON-NLS-1$
	private static final String COL_COLUMN_NAME = "Columns"; //$NON-NLS-1$
	private static final String COL_ORDER = "SortOrder"; //$NON-NLS-1$

	private ArcadColumns referenceColumns;

	private String[] columnNames = new String[] { COL_ID, COL_COLUMN_NAME, COL_ORDER };

	public ColumnedSortTableViewer(Composite parent, int style, ArcadColumns referenceColumns) {
		super(parent, style);
		this.referenceColumns = referenceColumns;
		this.addChildControls(parent);
	}

	private void addChildControls(Composite composite) {
		createTable(composite);
		createTableViewer();
		this.setContentProvider(new ColumnedSortDialogContentProvider());
		this.setLabelProvider(new ColumnedSortDialogLabelProvider());
	}

	/**
	 * @param parent
	 */
	private void createTable(Composite parent) {
		Table table = this.getTable();
		if (parent.getLayout() instanceof GridLayout) {
			GridData gridData = new GridData(GridData.FILL_BOTH);
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
			public void widgetSelected(SelectionEvent e) {
				setSorter(new ColumnedCriteriaSorter());
			}
		});

		// 2�me colonne
		column = new TableColumn(table, SWT.NONE);
		column.setText(CoreUILabels.resString("tableColumn.clm.Columns")); //$NON-NLS-1$
		column.setWidth(277);
		column.setResizable(false);

		// 3�me colonne
		column = new TableColumn(table, SWT.NONE);
		column.setText(CoreUILabels.resString("tableColumn.clm.SortOrder")); //$NON-NLS-1$
		column.setWidth(140);
		column.setResizable(false);
	}

	private void createTableViewer() {

		this.setColumnProperties(columnNames);
		CellEditor[] editors = new CellEditor[columnNames.length];
		editors[0] = null;
		editors[1] = new ComboBoxCellEditor(getTable(), referenceColumns.getUserNameValues(), SWT.READ_ONLY);
		editors[2] = new ComboBoxCellEditor(getTable(), ColumnedSortCriteriaList.SORT_ORDER_ARRAY, SWT.READ_ONLY);

		this.setCellEditors(editors);
		this.setCellModifier(new ColumnedSortCellModifier(this, referenceColumns));
		this.setSorter(new ColumnedCriteriaSorter());
	}

	class ColumnedSortDialogContentProvider implements IStructuredContentProvider {
		public void inputChanged(Viewer v, Object oldInput, Object newInput) {
			// Do nothing
		}

		public void dispose() {
			// Do nothing
		}

		public Object[] getElements(Object parent) {
			if (parent instanceof ColumnedSortCriteriaList) {
				ColumnedSortCriteriaList l = (ColumnedSortCriteriaList) parent;
				return l.getCriteria().toArray();
			}
			return new Object[0];
		}
	}

	public class ColumnedSortDialogLabelProvider extends LabelProvider implements ITableLabelProvider {

		public Image getColumnImage(Object element, int columnIndex) {
			return null;
		}

		public String getColumnText(Object element, int columnIndex) {
			ColumnedSortCriteria criteria = (ColumnedSortCriteria) element;
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

	public String[] getChoices(String property) {
		if (COL_COLUMN_NAME.equals(property))
			return referenceColumns.getUserNameValues();
		if (COL_ORDER.equals(property))
			return ColumnedSortCriteriaList.SORT_ORDER_ARRAY;
		return new String[] {};
	}

	@SuppressWarnings("unchecked")
	public List getColumnNames() {
		return Arrays.asList(columnNames);
	}
}
