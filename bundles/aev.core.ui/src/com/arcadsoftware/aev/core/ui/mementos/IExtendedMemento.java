/*
 * Cr�� le 27 sept. 04
 *
 * Pour changer le mod�le de ce fichier g�n�r�, allez � :
 * Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
 */
package com.arcadsoftware.aev.core.ui.mementos;

import org.eclipse.ui.IMemento;

/**
 * @author MD
 *
 * Pour changer le mod�le de ce commentaire de type g�n�r�, allez � :
 * Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
 */
public interface IExtendedMemento {
	public void addStateKey(IMemento memento);
	public void updateStateKey(IMemento memento);
	public void loadStateKey(IMemento memento);	
}
