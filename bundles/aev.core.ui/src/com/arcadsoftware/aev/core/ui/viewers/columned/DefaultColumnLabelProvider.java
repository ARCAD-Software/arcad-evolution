package com.arcadsoftware.aev.core.ui.viewers.columned;

import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.graphics.Image;

/**
 * @author ACL
 * 
 * Default ColumnLabel (CellLabel) provider for columns that require additional properties (e.g. Tooltip)
 *
 */
public class DefaultColumnLabelProvider extends ColumnLabelProvider  {

    private ITableLabelProvider tableLableProvider;
    private int columnIndex;
    
	public DefaultColumnLabelProvider(int columnIndex) {
        super();
        this.columnIndex = columnIndex;
    }
	public DefaultColumnLabelProvider(int columnIndex, ITableLabelProvider tableLableProvider) {
        this(columnIndex);
        this.setTableLableProvider(tableLableProvider);
    }
	
	@Override
	public String getText(Object element) {
		return getTableLableProvider().getColumnText(element, columnIndex);
	}
	
	@Override
	public Image getImage(Object element) {
		return getTableLableProvider().getColumnImage(element, columnIndex);
	}
	
	@Override
	public int getToolTipTimeDisplayed(Object object) {
		// Default so that tooltip is only displayed for 3 seconds...
		return 3000;
	}
	
	public ITableLabelProvider getTableLableProvider() {
		return tableLableProvider;
	}
	public void setTableLableProvider(ITableLabelProvider tableLableProvider) {
		this.tableLableProvider = tableLableProvider;
	}
	
}
