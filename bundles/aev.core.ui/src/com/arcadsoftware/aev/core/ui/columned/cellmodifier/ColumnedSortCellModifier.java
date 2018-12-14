package com.arcadsoftware.aev.core.ui.columned.cellmodifier;

import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.swt.widgets.TableItem;

import com.arcadsoftware.aev.core.tools.StringTools;
import com.arcadsoftware.aev.core.ui.columned.model.ArcadColumns;
import com.arcadsoftware.aev.core.ui.columned.model.ColumnedSortCriteria;
import com.arcadsoftware.aev.core.ui.viewers.columned.impl.ColumnedSortTableViewer;

/**
 * @author dlelong
 */
public class ColumnedSortCellModifier implements ICellModifier {

	private ColumnedSortTableViewer tableViewer;
	private ArcadColumns referenceColumns;

	/**
	 * Constructor
	 * 
	 * @param ColumnedDialogTableViewer
	 *            an instance of a ColumnedDialogTableViewer
	 */
	public ColumnedSortCellModifier(ColumnedSortTableViewer tableViewer, ArcadColumns referenceColumns) {
		super();
		this.tableViewer = tableViewer;
		this.referenceColumns = referenceColumns;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.ICellModifier#canModify(java.lang.Object,
	 * java.lang.String)
	 */
	public boolean canModify(Object element, String property) {
		if (property.equals(ColumnedSortTableViewer.COL_ID))
			return false;
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.ICellModifier#getValue(java.lang.Object,
	 * java.lang.String)
	 */
	public Object getValue(Object element, String property) {
		// Find the index of the column
		int columnIndex = tableViewer.getColumnNames().indexOf(property);

		Object result = null;
		ColumnedSortCriteria criterion = (ColumnedSortCriteria) element;

		switch (columnIndex) {
		case 1: // COLUMN_NAME_COLUMN
			String stringValue = criterion.getColumnName();
			String[] choices = tableViewer.getChoices(property);
			int i = choices.length - 1;
			while (!stringValue.equals(choices[i]) && i > 0)
				--i;
			result = new Integer(i);
			break;
		case 2: // SORT_ORDER_COLUMN
			stringValue = criterion.getSortOrder();
			choices = tableViewer.getChoices(property);
			i = choices.length - 1;
			while (!stringValue.equals(choices[i]) && i > 0)
				--i;
			result = new Integer(i);
			break;
		default:
			result = StringTools.EMPTY;
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.ICellModifier#modify(java.lang.Object,
	 * java.lang.String, java.lang.Object)
	 */
	public void modify(Object element, String property, Object value) {

		// Find the index of the column
		int columnIndex = tableViewer.getColumnNames().indexOf(property);

		TableItem item = (TableItem) element;
		ColumnedSortCriteria criterion = (ColumnedSortCriteria) item.getData();
		String valueString;

		switch (columnIndex) {
		case 0: // ID_COLUMN
			break;
		case 1: // COLUMN_NAME_COLUMN
			valueString = tableViewer.getChoices(property)[((Integer) value).intValue()].trim();
			if (!criterion.getColumnName().equals(valueString)) {
				criterion.setColumnName(valueString);
				int actualIndex = -1;
				for (int i = 0; i < referenceColumns.count(); i++) {
					if (referenceColumns.itemsByUserNameValue(valueString).equals(referenceColumns.items(i))) {
						actualIndex = referenceColumns.items(i).getPosition();
						break;
					}
				}
				criterion.setColumnIndex(actualIndex);
			}
			break;
		case 2: // SORT_ORDER_COLUMN
			valueString = tableViewer.getChoices(property)[((Integer) value).intValue()].trim();
			if (!criterion.getSortOrder().equals(valueString)) {
				criterion.setSortOrder(valueString);
			}
			break;
		default:
		}
		tableViewer.refresh();
	}
}
