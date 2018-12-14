package com.arcadsoftware.aev.core.ui.viewers.columned.impl;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Widget;

/**
 * @author MD
 *
 */
public class ColumnedTableViewer extends TableViewer {

    /**
     * @param parent
     */
    public ColumnedTableViewer(Composite parent) {
        super(parent);
    }

    /**
     * @param parent
     * @param style
     */
    public ColumnedTableViewer(Composite parent, int style) {
        super(parent, style);
    }

    /**
     * @param table
     */
    public ColumnedTableViewer(Table table) {
        super(table);
    }
    
	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.StructuredViewer#doFindItem(java.lang.Object)
	 */
	public TableItem findTableItem(Object element) {
		Widget w = findItem(element);
		if ((w !=null) && (w instanceof TableItem)) 
			return (TableItem)w;
		return null;
	}    
}
