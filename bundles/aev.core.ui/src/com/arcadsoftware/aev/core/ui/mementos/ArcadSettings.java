/*
 * Créé le 18 nov. 04
 *
 * Pour changer le modèle de ce fichier généré, allez à :
 * Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
package com.arcadsoftware.aev.core.ui.mementos;

/**
 * @author MD
 * 
 *         Pour changer le modèle de ce commentaire de type généré, allez à :
 *         Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et
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
