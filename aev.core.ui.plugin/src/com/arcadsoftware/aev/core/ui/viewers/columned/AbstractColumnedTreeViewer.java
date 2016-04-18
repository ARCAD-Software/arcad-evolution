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
 * 
 */
public abstract class AbstractColumnedTreeViewer extends AbstractColumnedViewer {

	/**
	 * @param parent
	 * @param style
	 */
	public AbstractColumnedTreeViewer(Composite parent, int style) {
		super(parent, style);
	}

	public AbstractColumnedTreeViewer(Composite parent, int style, boolean withInit) {
		super(parent, style, withInit);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.arcadsoftware.aev.core.ui.viewers.columned.AbstractColumnedViewer
	 * #createViewer(org.eclipse.swt.widgets.Composite, int)
	 */
	@Override
	public AbstractInternalColumnedViewer createViewer(Composite viewerParent, int viewerStyle) {
		return new ColumnedInternalTreeViewer(new ColumnedTreeViewer(viewerParent, viewerStyle));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.arcadsoftware.aev.core.ui.viewers.columned.AbstractColumnedViewer
	 * #createContentProvider()
	 */
	@Override
	public IContentProvider createContentProvider() {
		return new HierarchicTreeContentProvider();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.arcadsoftware.aev.core.ui.viewers.columned.AbstractColumnedViewer
	 * #createLabelProvider
	 * (com.arcadsoftware.aev.core.ui.viewers.columned.AbstractColumnedViewer)
	 */
	@Override
	public AbstractColumnedLabelProviderAdapter createLabelProvider(AbstractColumnedViewer viewer) {
		return createTreeLabelProvider(viewer);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.arcadsoftware.aev.core.ui.viewers.columned.AbstractColumnedViewer
	 * #createColumn(org.eclipse.swt.widgets.Widget, int, int)
	 */
	@Override
	public Item createColumn(Widget widget, int columnStyle, int index) {
		return new TreeColumn((Tree) widget, columnStyle, index);
	}

	@Override
	public void removeAllColumns() {
		Tree tree = getTree();
		for (int i = tree.getColumnCount() - 1; i >= 0; i--) {
			tree.getColumn(i).dispose();
		}
	}

	public Tree getTree() {
		return (Tree) getControl();
	}

	public TreeViewer getTreeViewer() {
		return (TreeViewer) getViewer();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.arcadsoftware.aev.core.ui.viewers.columned.AbstractColumnedViewer
	 * #getControl()
	 */
	@Override
	public Widget getControl() {
		return getViewer().getControl();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.arcadsoftware.aev.core.ui.viewers.columned.AbstractColumnedViewer
	 * #setColumnValues(org.eclipse.swt.widgets.Item,
	 * com.arcadsoftware.aev.core.ui.tableviewers.ArcadColumn)
	 */
	@Override
	public void setColumnValues(Item item, ArcadColumn c) {
		((TreeColumn) item).setWidth(c.getWidth());
	}

	public abstract AbstractColumnedTreeLabelProvider createTreeLabelProvider(AbstractColumnedViewer viewer);

	@Override
	protected Action[] makeActions() {
		Object[] previous = getPreviousActions().toArray();
		Action[] makeActions = super.makeActions();
		Object[] next = getNextActions().toArray();
		Action[] result = new Action[previous.length + makeActions.length + next.length + 2];
		System.arraycopy(previous, 0, result, 0, previous.length);
		result[previous.length] = new ColumnedActionSeparator();
		System.arraycopy(makeActions, 0, result, previous.length + 1, makeActions.length);
		int size = previous.length + makeActions.length + 1;
		result[size] = new ColumnedActionSeparator();
		System.arraycopy(next, 0, result, size + 1, next.length);
		return result;
	}
}
