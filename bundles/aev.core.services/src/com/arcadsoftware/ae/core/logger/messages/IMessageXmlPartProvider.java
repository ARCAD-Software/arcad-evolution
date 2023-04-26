/*
 * Cr�� le 13 mars 2007

 */
package com.arcadsoftware.ae.core.logger.messages;

import org.dom4j.Element;

/**
 * Interface permettant de fournir un partie sp�cifique sous forme de xml.<br>
 * Cette interface devra �tre impl�ment�e par les messages d�sirant 
 * fournir une partie des donn�es encapsul�es sous forme XML.
 * @author MD
 */
public interface IMessageXmlPartProvider {
	
	/**
	 * Cette m�thode permet de param�trer l'entit� "message" m�ere de toules
	 * les entit�s. Elle permet en outre de rajouter des attributs sp�cifiques.
	 * @param Element : Entit� racine du message.
	 */
	public void setXMLHeaderPart(Element root);	
	
	/**
	 * Cette m�thode permet de retourner une chaine de caract�re
	 * repr�sentant une partie de message sous forme XML.<br>
	 * Ce flux xml pourra �tre rattach� � l'�l�ment root pass� en param�tre.
	 * @param Element : Entit� de rattachement
	 */
	public void setXMLPart(Element root);
	
}
