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
