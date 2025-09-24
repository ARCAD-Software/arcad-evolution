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

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ICellModifier;

import com.arcadsoftware.aev.core.ui.viewers.columned.AbstractInternalColumnedViewer;

/**
 * Cette classe est un adaptater de ColumnedTableViewer.<br>
 * Elle permet de r�pondre � l'impl�mentation de IColumnedOptions pour une viewer de type TabelViewer.
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
