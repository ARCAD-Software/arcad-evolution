package com.arcadsoftware.aev.core.ui.labelproviders.columned;

import org.eclipse.jface.viewers.ITableLabelProvider;

public interface IArcadTableLabelProvider extends ITableLabelProvider {
	/*
	 * (non-Javadoc) To override to sort column by the values instead of the string values
	 * @see org.eclipse.jface.viewers.ITableLabelProvider#getColumnText(java.lang .Object, int)
	 */
	Object getColumnValue(Object element, int columnIndex);
}
