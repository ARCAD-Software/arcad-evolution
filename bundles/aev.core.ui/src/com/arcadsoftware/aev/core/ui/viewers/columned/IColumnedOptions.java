package com.arcadsoftware.aev.core.ui.viewers.columned;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ICellModifier;

/**
 * @author MD
 */
public interface IColumnedOptions {
	Object[] getColumnProperties();

	boolean getHeaderVisible();

	boolean getLinesVisible();

	void setCellEditors(CellEditor[] editors);

	void setCellModifier(ICellModifier modifier);

	void setColumnProperties(String[] columnProperties);

	void setHeaderVisible(boolean show);

	void setLinesVisible(boolean show);

}
