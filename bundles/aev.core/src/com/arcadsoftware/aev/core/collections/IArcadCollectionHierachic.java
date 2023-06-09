/*
 * Cr�� le 26 avr. 04
 *
 * Pour changer le mod�le de ce fichier g�n�r�, allez � :
 * Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
 */
package com.arcadsoftware.aev.core.collections;

/**
 * @author MD Pour changer le mod�le de ce commentaire de type g�n�r�, allez � :
 *         Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
 */
public interface IArcadCollectionHierachic {
	Object[] getChildren(IArcadCollectionItem item);

	IArcadCollectionItem getParent(IArcadCollectionItem item);

	boolean hasChildren(IArcadCollectionItem item);

	void removeBranch(IArcadCollectionItem item, boolean removeParent);

}
