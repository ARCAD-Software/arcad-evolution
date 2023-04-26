/*
 * Cr�� le 18 mai 04
 *
 * Pour changer le mod�le de ce fichier g�n�r�, allez � :
 * Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
 */
package com.arcadsoftware.aev.core.ui.selectionproviders;

import org.eclipse.core.runtime.ListenerList;
import org.eclipse.core.runtime.SafeRunner;
import org.eclipse.jface.util.SafeRunnable;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;

/**
 * @author MD
 * 
 *         Pour changer le mod�le de ce commentaire de type g�n�r�, allez � :
 *         Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et
 *         commentaires
 */
public class SelectionChangedProvider {

	// TODO [ListenerList] Verifier si le parametre passe dans le constructeur
	// est correct
	private ListenerList<ISelectionChangedListener> selectionChangedListeners = new ListenerList<ISelectionChangedListener>(ListenerList.EQUALITY);

	public void fireSelectionChanged(final SelectionChangedEvent event) {
		for (final ISelectionChangedListener l : selectionChangedListeners) {			
			SafeRunner.run(new SafeRunnable() {
				public void run() {
					l.selectionChanged(event);
				}

				@Override
				public void handleException(Throwable e) {
					super.handleException(e);
					// If and unexpected exception happens, remove it
					// to make sure the workbench keeps running.
					removeSelectionChangedListener(l);
				}
			});
		}
	}

	public void addSelectionChangedListener(ISelectionChangedListener listener) {
		selectionChangedListeners.add(listener);
	}

	/*
	 * (non-Javadoc) Method declared on ISelectionProvider.
	 */
	public void removeSelectionChangedListener(ISelectionChangedListener listener) {
		selectionChangedListeners.remove(listener);
	}

}
