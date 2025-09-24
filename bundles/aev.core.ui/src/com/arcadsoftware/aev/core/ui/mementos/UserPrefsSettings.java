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
 * Cr�� le 4 d�c. 2006
 */
package com.arcadsoftware.aev.core.ui.mementos;

import java.util.HashMap;
import java.util.Map;

public class UserPrefsSettings extends ArcadSettings {

	private String elementId;
	private Map<String, String> preferences = new HashMap<>();

	public UserPrefsSettings(final String elementId, final Map<String, String> preferences) {
		this("*ALL", "*ALL", elementId, preferences); //$NON-NLS-1$ //$NON-NLS-2$
	}

	/**
	 * @param serverName
	 * @param userName
	 */
	public UserPrefsSettings(final String serverName, final String userName) {
		super(serverName, userName);
	}

	public UserPrefsSettings(final String serverName, final String userName, final String elementId) {
		super(serverName, userName);
		this.elementId = elementId;
	}

	/**
	 * @param serverName
	 * @param userName
	 * @param viewerId
	 * @param cols
	 */
	public UserPrefsSettings(final String serverName, final String userName,
			final String viewerId, final Map<String, String> preferences) {
		this(serverName, userName, viewerId);
		this.preferences = preferences;
	}

	/**
	 * @return elementId (any UI element that uses User preferences).
	 */
	public String getElementId() {
		return elementId;
	}

	public Map<String, String> getPreferences() {
		return preferences;
	}

	public String getPrefValue(final String key) {
		return preferences.get(key);
	}

	public void setPrefValue(final String key, final String value) {
		preferences.put(key, value);
	}
}
