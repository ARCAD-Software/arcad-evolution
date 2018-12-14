/*
 * Créé le 18 nov. 04
 *
 * Pour changer le modèle de ce fichier généré, allez à :
 * Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
package com.arcadsoftware.aev.core.ui.mementos;

import com.arcadsoftware.aev.core.tools.StringTools;

/**
 * @author MD
 * 
 *         Pour changer le modèle de ce commentaire de type généré, allez à :
 *         Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et
 *         commentaires
 */
public class ExplorerSettings {
	private String viewId;
	private String keyValue;

	public ExplorerSettings() {
		super();
	}

	public ExplorerSettings(String viewId, String keyValue) {
		this();
		this.keyValue = keyValue;
		this.viewId = (viewId != null ? viewId : StringTools.EMPTY);
	}

	public String getKeyValue() {
		return keyValue;
	}

	public void setKeyValue(String string) {
		keyValue = string;
	}

	public String getViewId() {
		return viewId;
	}

	public void setViewId(String viewId) {
		this.viewId = viewId;
	}
}