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

	public static final String COL_ID = "ID"; //$NON-NLS-1$
	private static final String COL_COLUMN_NAME = "Columns"; //$NON-NLS-1$
	private static final String COL_OPERATOR = "Operator"; //$NON-NLS-1$
	public static final String COL_KEYWORD = "Keyword"; //$NON-NLS-1$

	private ArcadColumns referenceColumns;

	private String[] columnNames = new String[] { COL_ID, COL_COLUMN_NAME, COL_OPERATOR, COL_KEYWORD };

	public ColumnedSearchTableViewer(Composite parent, int style, ArcadColumns referenceColumns) {
		super(parent, style);
		this.referenceColumns = referenceColumns;
		this.addChildControls(parent);
	}

	/**
	 * Create a new shell, add the widgets, open the shell
	 */
	private void addChildControls(Composite composite) {
		createTable(composite);
		createTableViewer();
		this.setContentProvider(new ColumnedSearchDialogContentProvider());
		this.setLabelProvider(new ColumnedSearchDialogLabelProvider());
	}

	/**
	 * Create the Table
	 * 
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
		this.setColumnProperties(columnNames);
		CellEditor[] editors = new CellEditor[columnNames.length];
		editors[0] = null;
		editors[1] = new ComboBoxCellEditor(getTable(), referenceColumns.getUserNameValues(), SWT.READ_ONLY);
		editors[2] = new ComboBoxCellEditor(getTable(), ColumnedSearchCriteriaList.OPERATOR_ARRAY, SWT.READ_ONLY);
		editors[3] = new TextCellEditor(getTable(), SWT.MULTI);
		this.setCellEditors(editors);
		this.setCellModifier(new ColumnedSearchCellModifier(this, referenceColumns));
		this.setSorter(new ColumnedCriteriaSorter());
	}

	class ColumnedSearchDialogContentProvider implements IStructuredContentProvider {
		public void inputChanged(Viewer v, Object oldInput, Object newInput) {
			// Do nothing
		}

		public Object[] getElements(Object parent) {
			if (parent instanceof ColumnedSearchCriteriaList) {
				ColumnedSearchCriteriaList l = (ColumnedSearchCriteriaList) parent;
				return l.getCriteria().toArray();
			}
			return new Object[0];
		}

		public void dispose() {
			// Do nothing
		}

	}

	public class ColumnedSearchDialogLabelProvider extends LabelProvider implements ITableLabelProvider {

		public Image getColumnImage(Object element, int columnIndex) {
			return null;
		}

		public String getColumnText(Object element, int columnIndex) {
			ColumnedSearchCriteria criteria = (ColumnedSearchCriteria) element;
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

	public String[] getChoices(String property) {
		if (COL_COLUMN_NAME.equals(property))
			return referenceColumns.getUserNameValues();
		else if (COL_OPERATOR.equals(property))
			return ColumnedSearchCriteriaList.OPERATOR_ARRAY;
		else
			return new String[] {};
	}

	public List<String> getColumnNames() {
		return Arrays.asList(columnNames);
	}
}
