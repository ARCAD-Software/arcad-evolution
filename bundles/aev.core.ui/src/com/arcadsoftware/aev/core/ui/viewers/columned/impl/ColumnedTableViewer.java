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

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Widget;

/**
 * @author MD
 */
public class ColumnedTableViewer extends TableViewer {

	/**
	 * @param parent
	 */
	public ColumnedTableViewer(final Composite parent) {
		super(parent);
	}

	/**
	 * @param parent
	 * @param style
	 */
	public ColumnedTableViewer(final Composite parent, final int style) {
		super(parent, style);
	}

	/**
	 * @param table
	 */
	public ColumnedTableViewer(final Table table) {
		super(table);
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.jface.viewers.StructuredViewer#doFindItem(java.lang.Object)
	 */
	public TableItem findTableItem(final Object element) {
		final Widget w = findItem(element);
		if (w != null && w instanceof TableItem) {
			return (TableItem) w;
		}
		return null;
	}
}
