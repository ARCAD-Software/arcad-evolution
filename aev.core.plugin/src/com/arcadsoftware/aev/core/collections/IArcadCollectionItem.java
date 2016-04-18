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
 *         Pour changer le modèle de ce commentaire de type généré, allez à :
 *         Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et
 *         commentaires
 */
public interface IArcadCollectionItem extends IArcadDisplayable {
	public int getTag();

	public void setTag(int tag);

	public int getLevel();

	public void setLevel(int level);

	public void setIconID(String iconId);

	/**
	 * Egalité simple de l'élément sous égalité de la version de la classe
	 * Object.
	 * 
	 * @param item
	 * @return boolean
	 */
	public boolean equalsItem(IArcadCollectionItem item);

	public boolean equalsWithLevel(IArcadCollectionItem item);

	public ArcadCollection getParent();

	public void setParent(ArcadCollection parent);

	public IArcadCollectionItem duplicate();

	public boolean hasChildren();
}
