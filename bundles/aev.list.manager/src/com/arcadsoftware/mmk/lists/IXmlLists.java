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
package com.arcadsoftware.mmk.lists;

import com.arcadsoftware.mmk.lists.impl.xml.XmlCashManager;

public interface IXmlLists {

	XmlCashManager getCashManager();

	AbstractArcadList getList();

	/**
	 * Renvoit le nom du fichier xml de la structure sous-jacente.
	 * 
	 * @return the xmlFileName String :
	 */
	String getXmlFileName();

	/**
	 * Fixe le nom du fichier fichier xml de la structure sous-jacente.
	 * 
	 * @param xmlfileName
	 *            String : nom du fichier
	 */
	void setXmlFileName(String xmlfileName);

}
