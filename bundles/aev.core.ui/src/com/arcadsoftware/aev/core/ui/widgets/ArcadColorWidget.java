package com.arcadsoftware.aev.core.ui.widgets;

import com.arcadsoftware.aev.core.model.ArcadListenerList;
import com.arcadsoftware.aev.core.ui.listeners.IModifyListener;
import com.arcadsoftware.aev.core.ui.tools.ArcadColorUI;

public class ArcadColorWidget {

	protected ArcadColorUI colorUI;
	protected ArcadListenerList modifyListener = new ArcadListenerList();
	
	public ArcadColorUI getColorUI() {
		return colorUI;
	}

	public void setColorUI(ArcadColorUI colorUI) {
		this.colorUI = colorUI;
		fireModifyListener();
	}

	public ArcadColorWidget(){
		super();
	}
	
	public void addModifyListener(IModifyListener listener) {
		modifyListener.add(listener);
	}

	public void removeModifyListener(IModifyListener listener) {
		modifyListener.remove(listener);
	}

	public void fireModifyListener() {		
		Object[] listeners = modifyListener.getListeners();
		for (int i = 0; i < listeners.length; ++i) {
			IModifyListener l = (IModifyListener)listeners[i];
			try {
				l.changed();
			} catch (RuntimeException e1) {
				removeModifyListener(l);
			}				
		}	
	}	
}
