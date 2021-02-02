package com.arcadsoftware.aev.core.ui.viewers.columned.impl;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ICellModifier;

import com.arcadsoftware.aev.core.ui.viewers.columned.AbstractInternalColumnedViewer;

/**
 * Cette classe est un adaptater de ColumnedTableViewer.<br>
 * Elle permet de répondre à l'implémentation de IColumnedOptions pour une viewer de type TavbelViewer.
 * 
 * @author MD
 */
public class ColumnedInternalTableViewer extends AbstractInternalColumnedViewer {

	/**
	 * @param viewer
	 */
	public ColumnedInternalTableViewer(final ColumnedTableViewer viewer) {
		super(viewer);
	}

	/*
	 * (non-Javadoc)
	 * @see com.arcadsoftware.aev.core.ui.viewers.columned.IColumnedOptions#getColumnProperties()
	 */
	@Override
	public Object[] getColumnProperties() {
		return ((ColumnedTableViewer) viewer).getColumnProperties();
	}

	/*
	 * (non-Javadoc)
	 * @see com.arcadsoftware.aev.core.ui.viewers.columned.IColumnedOptions#getHeaderVisible()
	 */
	@Override
	public boolean getHeaderVisible() {
		return ((ColumnedTableViewer) viewer).getTable().getHeaderVisible();
	}

	/*
	 * (non-Javadoc)
	 * @see com.arcadsoftware.aev.core.ui.viewers.columned.IColumnedOptions#getLinesVisible()
	 */
	@Override
	public boolean getLinesVisible() {
		return ((ColumnedTableViewer) viewer).getTable().getLinesVisible();
	}

	/*
	 * (non-Javadoc)
	 * @see com.arcadsoftware.aev.core.ui.viewers.columned.IColumnedOptions#setCellEditors(org.eclipse.jface.viewers.
	 * CellEditor[])
	 */
	@Override
	public void setCellEditors(final CellEditor[] editors) {
		((ColumnedTableViewer) viewer).setCellEditors(editors);
	}

	/*
	 * (non-Javadoc)
	 * @see com.arcadsoftware.aev.core.ui.viewers.columned.IColumnedOptions#setCellModifier(org.eclipse.jface.viewers.
	 * ICellModifier)
	 */
	@Override
	public void setCellModifier(final ICellModifier modifier) {
		((ColumnedTableViewer) viewer).setCellModifier(modifier);

	}

	/*
	 * (non-Javadoc)
	 * @see com.arcadsoftware.aev.core.ui.viewers.columned.IColumnedOptions#setColumnProperties(java.lang.String[])
	 */
	@Override
	public void setColumnProperties(final String[] columnProperties) {
		((ColumnedTableViewer) viewer).setColumnProperties(columnProperties);
	}

	/*
	 * (non-Javadoc)
	 * @see com.arcadsoftware.aev.core.ui.viewers.columned.IColumnedOptions#setHeaderVisible(boolean)
	 */
	@Override
	public void setHeaderVisible(final boolean show) {
		((ColumnedTableViewer) viewer).getTable().setHeaderVisible(show);
	}

	/*
	 * (non-Javadoc)
	 * @see com.arcadsoftware.aev.core.ui.viewers.columned.IColumnedOptions#setLinesVisible(boolean)
	 */
	@Override
	public void setLinesVisible(final boolean show) {
		((ColumnedTableViewer) viewer).getTable().setLinesVisible(show);
	}

}
