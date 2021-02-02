package com.arcadsoftware.aev.core.ui.viewers.columned.impl;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ICellModifier;

import com.arcadsoftware.aev.core.ui.viewers.columned.AbstractInternalColumnedViewer;

/**
 * Cette classe est un adaptater de ColumnedTableViewer.<br>
 * Elle permet de répondre à l'implémentation de IColumnedOptions pour une viewer de type TabelViewer.
 * 
 * @author MD
 */
public class ColumnedInternalTreeViewer extends AbstractInternalColumnedViewer {

	/**
	 * @param viewer
	 */
	public ColumnedInternalTreeViewer(final ColumnedTreeViewer viewer) {
		super(viewer);
	}

	@Override
	public Object[] getColumnProperties() {
		return ((ColumnedTreeViewer) viewer).getColumnProperties();
	}

	@Override
	public boolean getHeaderVisible() {
		return ((ColumnedTreeViewer) viewer).getTree().getHeaderVisible();
	}

	@Override
	public boolean getLinesVisible() {
		return ((ColumnedTreeViewer) viewer).getTree().getLinesVisible();
	}

	@Override
	public void setCellEditors(final CellEditor[] editors) {
		((ColumnedTreeViewer) viewer).setCellEditors(editors);
	}

	@Override
	public void setCellModifier(final ICellModifier modifier) {
		((ColumnedTreeViewer) viewer).setCellModifier(modifier);
	}

	@Override
	public void setColumnProperties(final String[] columnProperties) {
		((ColumnedTreeViewer) viewer).setColumnProperties(columnProperties);
	}

	@Override
	public void setHeaderVisible(final boolean show) {
		((ColumnedTreeViewer) viewer).getTree().setHeaderVisible(show);
	}

	@Override
	public void setLinesVisible(final boolean show) {
		((ColumnedTreeViewer) viewer).getTree().setLinesVisible(show);
	}

}
