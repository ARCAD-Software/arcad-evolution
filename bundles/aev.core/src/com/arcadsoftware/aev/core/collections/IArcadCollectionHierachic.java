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
public interface IArcadCollectionHierachic {
	public Object[] getChildren(IArcadCollectionItem item);
	public IArcadCollectionItem getParent(IArcadCollectionItem item);	
	public boolean hasChildren(IArcadCollectionItem item);
	public void removeBranch(IArcadCollectionItem item, boolean removeParent);	
	
}
