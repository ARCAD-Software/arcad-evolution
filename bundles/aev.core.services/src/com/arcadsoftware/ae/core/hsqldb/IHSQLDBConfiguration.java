/*
 * Cr�� le 1 nov. 07
 *
 * TODO Pour changer le mod�le de ce fichier g�n�r�, allez � :
 * Fen�tre - Pr�f�rences - Java - Style de code - Mod�les de code
 */
package com.arcadsoftware.ae.core.hsqldb;



public interface IHSQLDBConfiguration {
	public String getDbFilterDateFormat(); 
	public String getDblocation();
	public String getPassword() ;
	public String getUserName();
	public String getModuleName();	
}
