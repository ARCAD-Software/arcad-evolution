package com.arcadsoftware.ae.core.logger.messages;

import org.dom4j.Element;

public interface IMessageXmlPartProvider {
	
	/**
	 * Cette méthode permet de paramétrer l'entité "message" mère de toutes les
	 * les entités. Elle permet en outre de rajouter des attributs spécifiques.
	 * @param Element : Entité racine du message.
	 */
	public void setXMLHeaderPart(Element root);	
	
	/**
	 * Cette méthode permet de retourner une chaine de caractére
	 * représentant une partie de message sous forme XML.<br>
	 * Ce flux xml pourra étre rattaché é l'élément root passé en paramétre.
	 * @param Element : Entité de rattachement
	 */
	public void setXMLPart(Element root);
	
}
