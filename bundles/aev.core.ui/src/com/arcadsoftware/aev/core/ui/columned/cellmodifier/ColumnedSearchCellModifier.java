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
package com.arcadsoftware.aev.core.ui.columned.cellmodifier;

import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.swt.widgets.TableItem;

import com.arcadsoftware.aev.core.tools.StringTools;
import com.arcadsoftware.aev.core.ui.columned.model.ArcadColumns;
import com.arcadsoftware.aev.core.ui.columned.model.ColumnedSearchCriteria;
import com.arcadsoftware.aev.core.ui.viewers.columned.impl.ColumnedSearchTableViewer;

/**
 * @author dlelong
 */
public class ColumnedSearchCellModifier implements ICellModifier {

	private final ArcadColumns referenceColumns;
	private final ColumnedSearchTableViewer tableViewer;

	/**
	 * Constructor
	 *
	 * @param ColumnedDialogTableViewer
	 *            an instance of a ColumnedDialogTableViewer
	 */
	public ColumnedSearchCellModifier(final ColumnedSearchTableViewer tableViewer,
			final ArcadColumns referenceColumns) {
		super();
		this.tableViewer = tableViewer;
		this.referenceColumns = referenceColumns;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ICellModifier#canModify(java.lang.Object, java.lang.String)
	 */
	@Override
	public boolean canModify(final Object element, final String property) {
		if (property.equals(ColumnedSearchTableViewer.COL_ID)) {
			return false;
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ICellModifier#getValue(java.lang.Object, java.lang.String)
	 */
	@Override
	public Object getValue(final Object element, final String property) {
		// Find the index of the column
		final int columnIndex = tableViewer.getColumnNames().indexOf(property);

		Object result = null;
		final ColumnedSearchCriteria criterion = (ColumnedSearchCriteria) element;

		switch (columnIndex) {
		case 1: // COLUMN_NAME_COLUMN
			String stringValue = criterion.getColumnName();
			String[] choices = tableViewer.getChoices(property);
			int i = choices.length - 1;
			while (!stringValue.equals(choices[i]) && i > 0) {
				--i;
			}
			result = new Integer(i);
			break;
		case 2: // OPERATOR_COLUMN
			stringValue = criterion.getOperator();
			choices = tableViewer.getChoices(property);
			i = choices.length - 1;
			while (!stringValue.equals(choices[i]) && i > 0) {
				--i;
			}
			result = new Integer(i);
			break;
		case 3: // KEYWORD_COLUMN
			result = criterion.getKeyword();
			break;
		default:
			result = StringTools.EMPTY;
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ICellModifier#modify(java.lang.Object, java.lang.String, java.lang.Object)
	 */
	@Override
	public void modify(final Object element, final String property, final Object value) {

		// Find the index of the column
		final int columnIndex = tableViewer.getColumnNames().indexOf(property);

		final TableItem item = (TableItem) element;
		final ColumnedSearchCriteria criterion = (ColumnedSearchCriteria) item.getData();
		String valueString;

		switch (columnIndex) {
		case 1: // COLUMN_NAME_COLUMN
			valueString = tableViewer.getChoices(property)[((Integer) value)].trim();
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
		case 2: // OPERATOR_COLUMN
			valueString = tableViewer.getChoices(property)[((Integer) value)].trim();
			if (!criterion.getOperator().equals(valueString)) {
				criterion.setOperator(valueString);
			}
			break;
		case 3: // KEYWORD_COLUMN
			valueString = ((String) value).trim();
			criterion.setKeyword(valueString);
			break;
		default:
		}
		tableViewer.refresh();
	}
}
