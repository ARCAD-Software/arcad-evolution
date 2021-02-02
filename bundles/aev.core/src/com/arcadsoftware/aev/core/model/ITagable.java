/*
 * Cr�� le 24 oct. 07
 *
 * TODO Pour changer le mod�le de ce fichier g�n�r�, allez � :
 * Fen�tre - Pr�f�rences - Java - Style de code - Mod�les de code
 */
package com.arcadsoftware.aev.core.model;

public interface ITagable {
	int getTag();

	boolean isUseTagEquality();

	void setTag(int flag);

	void setUseTagEquality(boolean useTagEquality);
}
