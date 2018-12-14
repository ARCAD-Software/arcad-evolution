package com.arcadsoftware.aev.core.ui.editors;


import java.util.ArrayList;

import org.eclipse.ui.part.EditorPart;

import com.arcadsoftware.aev.core.model.ArcadEntity;
import com.arcadsoftware.aev.core.ui.listeners.IDirtyListener;
import com.arcadsoftware.aev.core.ui.listeners.IEntitySaved;


public abstract class AbstractArcadEditorPart extends EditorPart 
implements IDirtyListener{
	
	private boolean dirtyPart = false;
	
	@Override
	public boolean isDirty() {
		return dirtyPart;
	}
	
	public void dirtyEvent(boolean dirty) {
		this.dirtyPart = dirty;
		firePropertyChange(PROP_DIRTY);			
	}
	
	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public final int LABEL_MAXLENGTH = 30;
	
	String initialName="";	 //$NON-NLS-1$
	String errorMessage = null;
	
	
	ArrayList<IEntitySaved> savedListeners = new ArrayList<IEntitySaved>();;

	@Override
	public void doSaveAs() {
	}

	@Override
	public boolean isSaveAsAllowed() {
		return false;
	}

	@Override
	public void setFocus() {
	}
	
	
	public void addSavedListener(IEntitySaved listener) {
		savedListeners.add(listener);
	}
	public void removeSavedListener(IEntitySaved listener) {
		savedListeners.remove(listener);
	}
	
	public void fireSaved(ArcadEntity entity) {		
		for (int i = 0; i < savedListeners.size(); ++i) {
			final IEntitySaved l = savedListeners.get(i);
			try {
				l.doAfterSaving(entity);
			} catch (RuntimeException e1) {
				removeSavedListener(l);
			}				
		}	
	}		

	protected String truncateName(String label){
		if (label.length()>LABEL_MAXLENGTH) {
			return label.substring(0,LABEL_MAXLENGTH-3)+"..."; //$NON-NLS-1$
		}
		return label;
		
	}
	
	public String getInitialName(){
		return initialName;
	}
	public void setInitialName(String initialName){
		this.initialName= initialName;
	}
	
	public boolean doBeforeSaving(){
		if (!checkData()) {
			if (errorMessage!=null) {
				//EvolutionCoreUIPlugin.openError(errorMessage);
			}
			return false;
		}		
		return true;
	}
	
	
	
	public abstract boolean checkData();
	
}
