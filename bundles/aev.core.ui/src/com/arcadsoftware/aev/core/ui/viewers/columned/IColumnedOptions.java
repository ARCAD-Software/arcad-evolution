package com.arcadsoftware.aev.core.ui.viewers.columned;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ICellModifier;

/**
 * @author MD
 *
 */
public interface IColumnedOptions {
    public void setHeaderVisible (boolean show);
    public void setLinesVisible (boolean show);
    public boolean getHeaderVisible();
    public boolean getLinesVisible(); 
    
    public Object[] getColumnProperties(); 
    public void setColumnProperties(String[] columnProperties);    
    public void setCellEditors(CellEditor[] editors);
    public void setCellModifier(ICellModifier modifier); 
    
}

