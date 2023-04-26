package com.arcadsoftware.aev.core.ui.viewers.columned;
import org.eclipse.jface.viewers.StructuredViewer;


/**
 * Cette classe est un adaptater de viewer.<br>
 * Elle permet � un AbstractColumnedViewer de voir un TableViewer 
 * et un TreeViewer de la m�me mani�re via l'impl�mentation de
 * l'interface IColumnedOptions.<br>
 * La variable viewer est un StructuredViewer (classe anc�tre des tableViewers
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
