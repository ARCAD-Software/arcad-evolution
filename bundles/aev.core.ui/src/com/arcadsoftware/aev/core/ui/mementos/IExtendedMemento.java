/*
 * Créé le 27 sept. 04
 *
 * Pour changer le modèle de ce fichier généré, allez à :
 * Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
package com.arcadsoftware.aev.core.ui.mementos;

import org.eclipse.ui.IMemento;

/**
 * @author MD Pour changer le modèle de ce commentaire de type généré, allez à :
 *         Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
public interface IExtendedMemento {
	void addStateKey(IMemento memento);

	void loadStateKey(IMemento memento);

	void updateStateKey(IMemento memento);
}
