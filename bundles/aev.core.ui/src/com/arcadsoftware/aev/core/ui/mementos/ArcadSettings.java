/*
 * Cr�� le 18 nov. 04
 *
 * Pour changer le mod�le de ce fichier g�n�r�, allez � :
 * Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
 */
package com.arcadsoftware.aev.core.ui.mementos;

/**
 * @author MD
 * 
 *         Pour changer le mod�le de ce commentaire de type g�n�r�, allez � :
 *         Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et
 *         commentaires
 */
public class ArcadSettings {

	private String serverName;
	private String userName;

	/**
	 * 
	 */
	public ArcadSettings(String serverName, String userName) {
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
	public void setServerName(String string) {
		serverName = string;
	}

	/**
	 * @param string
	 */
	public void setUserName(String string) {
		userName = string;
	}

}
