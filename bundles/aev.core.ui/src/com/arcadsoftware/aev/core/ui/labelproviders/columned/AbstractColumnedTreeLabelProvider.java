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
/*
 * Created on 13 nov. 2006
 */
package com.arcadsoftware.aev.core.ui.labelproviders.columned;

import org.eclipse.swt.widgets.TreeItem;

import com.arcadsoftware.aev.core.ui.viewers.columned.AbstractColumnedViewer;
import com.arcadsoftware.aev.core.ui.viewers.columned.impl.ColumnedTreeViewer;

/**
 * @author MD
 */
public abstract class AbstractColumnedTreeLabelProvider extends AbstractColumnedTableLabelProvider {

	public AbstractColumnedTreeLabelProvider(final AbstractColumnedViewer viewer) {
		super(viewer);
	}

	public TreeItem getTreeItem(final Object element) {
		if (viewer != null) {
			if (viewer.getViewer() instanceof ColumnedTreeViewer) {
				return ((ColumnedTreeViewer) viewer.getViewer()).findTreeItem(element);
			}
		}
		return null;
	}
}
