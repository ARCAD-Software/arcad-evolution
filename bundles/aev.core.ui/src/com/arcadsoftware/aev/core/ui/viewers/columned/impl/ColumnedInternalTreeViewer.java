package com.arcadsoftware.aev.core.ui.viewers.columned.impl;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ICellModifier;



import com.arcadsoftware.aev.core.ui.viewers.columned.AbstractInternalColumnedViewer;


/**
 * Cette classe est un adaptater de ColumnedTableViewer.<br>
 * Elle permet de répondre à l'implémentation de IColumnedOptions
 * pour une viewer de type TabelViewer.
 * @author MD
 *
 */ 
public class ColumnedInternalTreeViewer extends AbstractInternalColumnedViewer {

    /**
     * @param viewer
     */
    public ColumnedInternalTreeViewer(ColumnedTreeViewer viewer) {
        super(viewer);
    }
    public void setHeaderVisible(boolean show) {
    	((ColumnedTreeViewer)viewer).getTree().setHeaderVisible(show);
    }

    public void setLinesVisible(boolean show) {
    	((ColumnedTreeViewer)viewer).getTree().setLinesVisible(show);
    }

    public boolean getHeaderVisible() {
    	return ((ColumnedTreeViewer)viewer).getTree().getHeaderVisible();
    }
    public boolean getLinesVisible() {
    	return ((ColumnedTreeViewer)viewer).getTree().getLinesVisible();
    }
    public Object[] getColumnProperties() {
    	return ((ColumnedTreeViewer)viewer).getColumnProperties();
    }
    public void setColumnProperties(String[] columnProperties) { 
    	((ColumnedTreeViewer)viewer).setColumnProperties(columnProperties);
    }

    public void setCellEditors(CellEditor[] editors) {
    	((ColumnedTreeViewer)viewer).setCellEditors(editors);
    }

    public void setCellModifier(ICellModifier modifier) {
    	((ColumnedTreeViewer)viewer).setCellModifier(modifier);
    }

}
