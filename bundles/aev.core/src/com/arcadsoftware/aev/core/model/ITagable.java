/*
 * Cr�� le 24 oct. 07
 *
 * TODO Pour changer le mod�le de ce fichier g�n�r�, allez � :
 * Fen�tre - Pr�f�rences - Java - Style de code - Mod�les de code
 */
package com.arcadsoftware.aev.core.model;

public interface ITagable {
	public boolean isUseTagEquality() ;
	public void setUseTagEquality(boolean useTagEquality) ;
	public void setTag(int flag);
	public int getTag();	
}

