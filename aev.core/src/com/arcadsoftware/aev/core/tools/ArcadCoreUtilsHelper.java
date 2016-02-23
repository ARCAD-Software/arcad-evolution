/*
 * Created on Sep 21, 2006
 */
package com.arcadsoftware.aev.core.tools;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import com.arcadsoftware.aev.core.EvolutionCorePlugin;

/**
 * @author MD
 */
public class ArcadCoreUtilsHelper implements IHelper {
    
    private static Cursor cursor; 

    public ArcadCoreUtilsHelper() {
        super();
    }

    public void beginAction(){
		Shell shell = EvolutionCorePlugin.getDefault().getPluginShell();
		if (shell!=null){
			if (shell.getDisplay()!=null){
				Display display = shell.getDisplay(); 
				cursor = new Cursor (display, SWT.CURSOR_WAIT);
				shell.setCursor(cursor);
			}				
		}
    }
	public void endAction(){
		Shell shell = EvolutionCorePlugin.getDefault().getPluginShell();
		if (shell!=null){
			shell.setCursor(null);
			cursor.dispose();			
		}
	}	

	public String getBasedLocation(){
	  return EvolutionCorePlugin.getDefault().getStateLocation().toString();	    
	}

	public String getCompliantFileName() {
		return EvolutionCorePlugin.getCompliantFileName();
	}
}
