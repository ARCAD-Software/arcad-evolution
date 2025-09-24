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

import com.arcadsoftware.aev.core.tools.StringTools;

/**
 * @author MD Pour changer le mod�le de ce commentaire de type g�n�r�, allez � :
 *         Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
 */
public class ExplorerSettings {
	private String keyValue;
	private String viewId;

	public ExplorerSettings() {
		super();
	}

	public ExplorerSettings(final String viewId, final String keyValue) {
		this();
		this.keyValue = keyValue;
		this.viewId = viewId != null ? viewId : StringTools.EMPTY;
	}

	public String getKeyValue() {
		return keyValue;
	}

	public String getViewId() {
		return viewId;
	}

	public void setKeyValue(final String string) {
		keyValue = string;
	}

	public void setViewId(final String viewId) {
		this.viewId = viewId;
	}
}