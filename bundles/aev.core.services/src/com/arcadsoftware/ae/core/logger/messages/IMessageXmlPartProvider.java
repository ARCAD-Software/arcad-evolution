/*******************************************************************************
 * Copyright (c) 2025 ARCAD Software.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     ARCAD Software - initial API and implementation
 *******************************************************************************/
package com.arcadsoftware.ae.core.logger.messages;

import org.w3c.dom.Element;

public interface IMessageXmlPartProvider {

	/**
	 * Cette méthode permet de paramétrer l'entité "message" mère de toutes les les entités. Elle permet en outre de
	 * rajouter des attributs spécifiques.
	 *
	 * @param Element
	 *            : Entité racine du message.
	 */
	void setXMLHeaderPart(Element root);

	/**
	 * Cette méthode permet de retourner une chaine de caractére représentant une partie de message sous forme XML.<br>
	 * Ce flux xml pourra étre rattaché é l'élément root passé en paramétre.
	 *
	 * @param Element
	 *            : Entité de rattachement
	 */
	void setXMLPart(Element root);

}
