package com.arcadsoftware.aev.core.ui.viewers.columned;
import org.eclipse.jface.viewers.StructuredViewer;


/**
 * Cette classe est un adaptater de viewer.<br>
 * Elle permet à un AbstractColumnedViewer de voir un TableViewer 
 * et un TreeViewer de la même manière via l'implémentation de
 * l'interface IColumnedOptions.<br>
 * La variable viewer est un StructuredViewer (classe ancêtre des tableViewers
 * et de treeViewer.<br>  
 * @author MD
 *
 */
public abstract class AbstractInternalColumnedViewer
implements IColumnedOptions{    
    protected StructuredViewer viewer; 

    /**
     * 
     */
    public AbstractInternalColumnedViewer(StructuredViewer viewer) {
        super();
        this.viewer = viewer;
    }

    /**
     * @return Returns the viewer.
     */
    public StructuredViewer getViewer() {
        return viewer;
    }
    
    
}
