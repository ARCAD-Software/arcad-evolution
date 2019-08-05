package com.arcadsoftware.aev.core.ui.tools;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import com.arcadsoftware.aev.core.tools.ArcadCoreUtilsHelper;
import com.arcadsoftware.aev.core.ui.EvolutionCoreUIPlugin;

public class ArcadCoreUIUtilsHelper extends ArcadCoreUtilsHelper {
	private static Cursor cursor;
	
	@Override
	public void beginAction(){
		Shell shell = EvolutionCoreUIPlugin.getDefault().getPluginShell();
		if (shell!=null){
			if (shell.getDisplay()!=null){
				Display display = shell.getDisplay(); 
				cursor = new Cursor (display, SWT.CURSOR_WAIT);
				shell.setCursor(cursor);
			}				
		}
    }
    @Override
	public void endAction(){
		Shell shell = EvolutionCoreUIPlugin.getDefault().getPluginShell();
		if (shell!=null){
			shell.setCursor(null);
			cursor.dispose();			
		}
	}
}
