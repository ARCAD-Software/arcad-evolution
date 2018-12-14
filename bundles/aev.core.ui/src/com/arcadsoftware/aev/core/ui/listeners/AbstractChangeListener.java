/*
 * Créé le 18 mars 05
 *
 * Pour changer le modèle de ce fichier généré, allez à :
 * Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
package com.arcadsoftware.aev.core.ui.listeners;

import com.arcadsoftware.aev.core.ui.controlers.IContentChangeListener;
import com.arcadsoftware.aev.core.model.ArcadListenerList;

/**
 * @author MD
 *
 */
public class AbstractChangeListener {
	private ArcadListenerList contentChangedListeners = new ArcadListenerList(3);
	/**
	 * 
	 */
	public AbstractChangeListener() {
		super();
	}
	
	public void fireContentChanged() {		
		Object[] listeners = contentChangedListeners.getListeners();
		for (int i = 0; i < listeners.length; ++i) {
			final IContentChangeListener l = (IContentChangeListener)listeners[i];
			try {
				l.contentChanged();
			} catch (RuntimeException e1) {
				removeContentChangedListener(l);
			}				
		}	
	}		
	
	public void addContentChangedListener(IContentChangeListener listener) {
		contentChangedListeners.add(listener);
	}
	
	/* (non-Javadoc)
	 * Method declared on ISelectionProvider.
	 */
	public void removeContentChangedListener(IContentChangeListener listener) {
		contentChangedListeners.remove(listener);
	}	
}
