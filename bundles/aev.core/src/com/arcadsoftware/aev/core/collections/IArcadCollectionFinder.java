/*
 * Cr�� le 26 avr. 04
 *
 * Pour changer le mod�le de ce fichier g�n�r�, allez � :
 * Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
 */
package com.arcadsoftware.aev.core.collections;

/**
 * @author MD
 *
 * Pour changer le mod�le de ce commentaire de type g�n�r�, allez � :
 * Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
 */
public interface IArcadCollectionFinder {
	public int find (IArcadCollectionItem c,int startpos);
	public int findFirst (IArcadCollectionItem c);
	public int findWithLevel (IArcadCollectionItem c,int startpos);
	public int findFirstWithLevel (IArcadCollectionItem c);	
	public int findByInstance(IArcadCollectionItem c);
}
