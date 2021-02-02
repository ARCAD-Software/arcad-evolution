package com.arcadsoftware.aev.core.ui.viewers.columned;

import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Item;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Widget;

import com.arcadsoftware.aev.core.ui.columned.model.ArcadColumn;
import com.arcadsoftware.aev.core.ui.contentproviders.ArcadCollectionItemContentProvider;
import com.arcadsoftware.aev.core.ui.labelproviders.columned.AbstractColumnedLabelProviderAdapter;
import com.arcadsoftware.aev.core.ui.labelproviders.columned.AbstractColumnedTableLabelProvider;
import com.arcadsoftware.aev.core.ui.viewers.columned.impl.ColumnedInternalTableViewer;
import com.arcadsoftware.aev.core.ui.viewers.columned.impl.ColumnedTableViewer;

/**
 * @author dlelong
 */
public abstract class AbstractColumnedInPlaceEditorTableViewer extends AbstractColumnedTableViewer {

	public AbstractColumnedInPlaceEditorTableViewer(final Composite parent, final int style) {
		super(parent, style);
	}

	/*
	 * (non-Javadoc)
	 * @see com.arcadsoftware.aev.core.ui.viewers.columned.AbstractColumnedViewer
	 * #createColumn(org.eclipse.swt.widgets.Widget, int, int)
	 */
	@Override
	public Item createColumn(final Widget widget, final int columnStyle, final int index) {
		return new TableColumn((Table) widget, columnStyle, index);
	}

	/*
	 * (non-Javadoc)
	 * @see com.arcadsoftware.aev.core.ui.viewers.columned.AbstractColumnedViewer #createContentProvider()
	 */
	@Override
	public IContentProvider createContentProvider() {
		return new ArcadCollectionItemContentProvider();
	}

	/*
	 * (non-Javadoc)
	 * @see com.arcadsoftware.aev.core.ui.viewers.columned.AbstractColumnedViewer #createLabelProvider
	 * (com.arcadsoftware.aev.core.ui.viewers.columned.AbstractColumnedViewer)
	 */
	@Override
	public AbstractColumnedLabelProviderAdapter createLabelProvider(final AbstractColumnedViewer viewer) {
		return createTableLabelProvider(viewer);
	}

	@Override
	public abstract AbstractColumnedTableLabelProvider createTableLabelProvider(AbstractColumnedViewer viewer);

	/*
	 * (non-Javadoc)
	 * @see com.arcadsoftware.aev.core.ui.viewers.columned.AbstractColumnedViewer
	 * #createViewer(org.eclipse.swt.widgets.Composite, int)
	 */
	@Override
	public AbstractInternalColumnedViewer createViewer(final Composite viewerParent, final int viewerStyle) {
		return new ColumnedInternalTableViewer(new ColumnedTableViewer(viewerParent, viewerStyle));
	}

	/*
	 * (non-Javadoc)
	 * @see com.arcadsoftware.aev.core.ui.viewers.columned.AbstractColumnedViewer #getControl()
	 */
	@Override
	public Widget getControl() {
		return getViewer().getControl();
	}

	/*
	 * (non-Javadoc)
	 * @see com.arcadsoftware.aev.core.ui.viewers.columned.AbstractColumnedViewer #removeAllColumns()
	 */
	@Override
	public void removeAllColumns() {
		final Table table = (Table) getViewer().getControl();
		for (int i = table.getColumnCount() - 1; i >= 0; i--) {
			table.getColumn(i).dispose();
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.arcadsoftware.aev.core.ui.viewers.columned.AbstractColumnedViewer
	 * #setColumnValues(org.eclipse.swt.widgets.Item, com.arcadsoftware.aev.core.ui.tableviewers.ArcadColumn)
	 */
	@Override
	public void setColumnValues(final Item item, final ArcadColumn c) {
		((TableColumn) item).setWidth(c.getWidth());
	}

}
