/*
 * Créé le 26 avr. 04
 *
 * Pour changer le modèle de ce fichier généré, allez à :
 * Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
package com.arcadsoftware.aev.core.collections;

/**
 * @author MD
 *
 * Pour changer le modèle de ce commentaire de type généré, allez à :
 * Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
public interface IArcadCollectionFinder {
	public int find (IArcadCollectionItem c,int startpos);
	public int findFirst (IArcadCollectionItem c);
	public int findWithLevel (IArcadCollectionItem c,int startpos);
	public int findFirstWithLevel (IArcadCollectionItem c);	
	public int findByInstance(IArcadCollectionItem c);
}
