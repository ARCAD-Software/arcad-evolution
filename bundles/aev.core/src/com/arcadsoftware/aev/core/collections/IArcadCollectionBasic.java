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
public interface IArcadCollectionBasic {
	
	public int count();	
	public void add(IArcadCollectionItem c);
	public void copyAndAdd(IArcadCollectionItem c);	
	public void copyAndInsert(int index,IArcadCollectionItem c);		
	public void insert(int index,IArcadCollectionItem c);
	public IArcadCollectionItem items(int index);		
	public void delete(int index);
	public void clear();		
	public Object[] toArray();
	
}
