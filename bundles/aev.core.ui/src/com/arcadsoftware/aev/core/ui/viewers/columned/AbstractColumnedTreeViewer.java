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
package com.arcadsoftware.aev.core.ui.viewers.columned;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Item;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.swt.widgets.Widget;

import com.arcadsoftware.aev.core.ui.columned.model.ArcadColumn;
import com.arcadsoftware.aev.core.ui.contentproviders.HierarchicTreeContentProvider;
import com.arcadsoftware.aev.core.ui.labelproviders.columned.AbstractColumnedLabelProviderAdapter;
import com.arcadsoftware.aev.core.ui.labelproviders.columned.AbstractColumnedTreeLabelProvider;
import com.arcadsoftware.aev.core.ui.viewers.columned.impl.ColumnedInternalTreeViewer;
import com.arcadsoftware.aev.core.ui.viewers.columned.impl.ColumnedTreeViewer;

/**
 * @author MD
 */
public abstract class AbstractColumnedTreeViewer extends AbstractColumnedViewer {

	/**
	 * @param parent
	 * @param style
	 */
	public AbstractColumnedTreeViewer(final Composite parent, final int style) {
		super(parent, style);
	}

	public AbstractColumnedTreeViewer(final Composite parent, final int style, final boolean withInit) {
		super(parent, style, withInit);
	}

	/*
	 * (non-Javadoc)
	 * @see com.arcadsoftware.aev.core.ui.viewers.columned.AbstractColumnedViewer
	 * #createColumn(org.eclipse.swt.widgets.Widget, int, int)
	 */
	@Override
	public Item createColumn(final Widget widget, final int columnStyle, final int index) {
		return new TreeColumn((Tree) widget, columnStyle, index);
	}

	/*
	 * (non-Javadoc)
	 * @see com.arcadsoftware.aev.core.ui.viewers.columned.AbstractColumnedViewer #createContentProvider()
	 */
	@Override
	public IContentProvider createContentProvider() {
		return new HierarchicTreeContentProvider();
	}

	/*
	 * (non-Javadoc)
	 * @see com.arcadsoftware.aev.core.ui.viewers.columned.AbstractColumnedViewer #createLabelProvider
	 * (com.arcadsoftware.aev.core.ui.viewers.columned.AbstractColumnedViewer)
	 */
	@Override
	public AbstractColumnedLabelProviderAdapter createLabelProvider(final AbstractColumnedViewer viewer) {
		return createTreeLabelProvider(viewer);
	}

	public abstract AbstractColumnedTreeLabelProvider createTreeLabelProvider(AbstractColumnedViewer viewer);

	/*
	 * (non-Javadoc)
	 * @see com.arcadsoftware.aev.core.ui.viewers.columned.AbstractColumnedViewer
	 * #createViewer(org.eclipse.swt.widgets.Composite, int)
	 */
	@Override
	public AbstractInternalColumnedViewer createViewer(final Composite viewerParent, final int viewerStyle) {
		return new ColumnedInternalTreeViewer(new ColumnedTreeViewer(viewerParent, viewerStyle));
	}

	/*
	 * (non-Javadoc)
	 * @see com.arcadsoftware.aev.core.ui.viewers.columned.AbstractColumnedViewer #getControl()
	 */
	@Override
	public Widget getControl() {
		return getViewer().getControl();
	}

	public Tree getTree() {
		return (Tree) getControl();
	}

	public TreeViewer getTreeViewer() {
		return (TreeViewer) getViewer();
	}

	@Override
	protected Action[] makeActions() {
		final Object[] previous = getPreviousActions().toArray();
		final Action[] makeActions = super.makeActions();
		final Object[] next = getNextActions().toArray();
		final Action[] result = new Action[previous.length + makeActions.length + next.length + 2];
		System.arraycopy(previous, 0, result, 0, previous.length);
		result[previous.length] = new ColumnedActionSeparator();
		System.arraycopy(makeActions, 0, result, previous.length + 1, makeActions.length);
		final int size = previous.length + makeActions.length + 1;
		result[size] = new ColumnedActionSeparator();
		System.arraycopy(next, 0, result, size + 1, next.length);
		return result;
	}

	@Override
	public void removeAllColumns() {
		final Tree tree = getTree();
		for (int i = tree.getColumnCount() - 1; i >= 0; i--) {
			tree.getColumn(i).dispose();
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.arcadsoftware.aev.core.ui.viewers.columned.AbstractColumnedViewer
	 * #setColumnValues(org.eclipse.swt.widgets.Item, com.arcadsoftware.aev.core.ui.tableviewers.ArcadColumn)
	 */
	@Override
	public void setColumnValues(final Item item, final ArcadColumn c) {
		((TreeColumn) item).setWidth(c.getWidth());
	}
}
