package com.arcadsoftware.aev.core.ui.viewers.columned;

import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.graphics.Image;

/**
 * @author ACL Default ColumnLabel (CellLabel) provider for columns that require additional properties (e.g. Tooltip)
 */
public class DefaultColumnLabelProvider extends ColumnLabelProvider {

	private final int columnIndex;
	private ITableLabelProvider tableLableProvider;

	public DefaultColumnLabelProvider(final int columnIndex) {
		super();
		this.columnIndex = columnIndex;
	}

	public DefaultColumnLabelProvider(final int columnIndex, final ITableLabelProvider tableLableProvider) {
		this(columnIndex);
		setTableLableProvider(tableLableProvider);
	}

	@Override
	public Image getImage(final Object element) {
		return getTableLableProvider().getColumnImage(element, columnIndex);
	}

	public ITableLabelProvider getTableLableProvider() {
		return tableLableProvider;
	}

	@Override
	public String getText(final Object element) {
		return getTableLableProvider().getColumnText(element, columnIndex);
	}

	@Override
	public int getToolTipTimeDisplayed(final Object object) {
		// Default so that tooltip is only displayed for 3 seconds...
		return 3000;
	}

	public void setTableLableProvider(final ITableLabelProvider tableLableProvider) {
		this.tableLableProvider = tableLableProvider;
	}

}
