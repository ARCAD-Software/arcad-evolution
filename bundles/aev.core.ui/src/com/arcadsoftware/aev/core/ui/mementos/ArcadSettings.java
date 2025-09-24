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
/*
 * Cr�� le 18 nov. 04
 *
 * Pour changer le mod�le de ce fichier g�n�r�, allez � :
 * Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
 */
package com.arcadsoftware.aev.core.ui.mementos;

/**
 * @author MD Pour changer le mod�le de ce commentaire de type g�n�r�, allez � :
 *         Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
 */
public class ArcadSettings {

	private String serverName;
	private String userName;

	/**
	 *
	 */
	public ArcadSettings(final String serverName, final String userName) {
		super();
		this.serverName = serverName;
		this.userName = userName;
	}

	/**
	 * @return String
	 */
	public String getServerName() {
		return serverName;
	}

	/**
	 * @return String
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * @param string
	 */
	public void setServerName(final String string) {
		serverName = string;
	}

	/**
	 * @param string
	 */
	public void setUserName(final String string) {
		userName = string;
	}

}
