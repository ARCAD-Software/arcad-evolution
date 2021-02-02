package com.arcadsoftware.aev.core.ui.viewers.columned.impl;

import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.swt.widgets.Widget;

/**
 * @author MD
 */
public class ColumnedTreeViewer extends TreeViewer {

	/**
	 * @param parent
	 */
	public ColumnedTreeViewer(final Composite parent) {
		super(parent);
	}

	/**
	 * @param parent
	 * @param style
	 */
	public ColumnedTreeViewer(final Composite parent, final int style) {
		super(parent, style);
	}

	/**
	 * @param tree
	 */
	public ColumnedTreeViewer(final Tree tree) {
		super(tree);
	}

	public TreeItem findTreeItem(final Object element) {
		final Widget w = findItem(element);
		if (w != null && w instanceof TreeItem) {
			return (TreeItem) w;
		}
		return null;
	}

}
