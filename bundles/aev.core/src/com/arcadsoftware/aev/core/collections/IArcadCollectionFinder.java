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
public interface IArcadCollectionFinder {
	int find(IArcadCollectionItem c, int startpos);

	int findByInstance(IArcadCollectionItem c);

	int findFirst(IArcadCollectionItem c);

	int findFirstWithLevel(IArcadCollectionItem c);

	int findWithLevel(IArcadCollectionItem c, int startpos);
}
