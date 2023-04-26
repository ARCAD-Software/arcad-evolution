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
 *         Pour changer le mod�le de ce commentaire de type g�n�r�, allez � :
 *         Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et
 *         commentaires
 */
public interface IArcadCollectionItem extends IArcadDisplayable {
	public int getTag();

	public void setTag(int tag);

	public int getLevel();

	public void setLevel(int level);

	public void setIconID(String iconId);

	/**
	 * Egalit� simple de l'�l�ment sous �galit� de la version de la classe
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
