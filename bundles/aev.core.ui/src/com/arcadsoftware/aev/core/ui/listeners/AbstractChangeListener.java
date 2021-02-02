/*
 * Créé le 18 mars 05
 *
 * Pour changer le modèle de ce fichier généré, allez à :
 * Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
package com.arcadsoftware.aev.core.ui.listeners;

import com.arcadsoftware.aev.core.model.ArcadListenerList;
import com.arcadsoftware.aev.core.ui.controlers.IContentChangeListener;

/**
 * @author MD
 */
public class AbstractChangeListener {
	private final ArcadListenerList contentChangedListeners = new ArcadListenerList(3);

	/**
	 *
	 */
	public AbstractChangeListener() {
		super();
	}

	public void addContentChangedListener(final IContentChangeListener listener) {
		contentChangedListeners.add(listener);
	}

	public void fireContentChanged() {
		final Object[] listeners = contentChangedListeners.getListeners();
		for (final Object listener : listeners) {
			final IContentChangeListener l = (IContentChangeListener) listener;
			try {
				l.contentChanged();
			} catch (final RuntimeException e1) {
				removeContentChangedListener(l);
			}
		}
	}

	/*
	 * (non-Javadoc) Method declared on ISelectionProvider.
	 */
	public void removeContentChangedListener(final IContentChangeListener listener) {
		contentChangedListeners.remove(listener);
	}
}
