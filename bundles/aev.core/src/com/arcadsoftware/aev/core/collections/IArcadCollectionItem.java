/*
 * Créé le 26 avr. 04
 *
 */
package com.arcadsoftware.aev.core.collections;

/**
 * @author MD
 * 
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
	public void unsetParent(ArcadCollection parent);

	public IArcadCollectionItem duplicate();

	public boolean hasChildren();
}
