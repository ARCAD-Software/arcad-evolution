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
package com.arcadsoftware.ae.core.utils;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class Labels {
	private static final String BUNDLE = "com.arcadsoftware.exec.config.labels";

	private static Labels instance = new Labels();

	private static Locale locale = new Locale("en", "EN");

	static ResourceBundle resourceBundle = null;

	public static Labels getInstance() {
		return instance;
	}

	public static String resString(final String key) {
		resourceBundle = ResourceBundle.getBundle(BUNDLE, locale);
		if (resourceBundle != null) {
			try {
				return resourceBundle.getString(key);
			} catch (final MissingResourceException e) {
				return key;
			}

		}
		return key;
	}

	boolean loaded = false;

	private Labels() {
		super();
	}

	public Locale getLocal() {
		return locale;
	}

	public void setLocal(final Locale l) {
		if (!l.getCountry().equals(locale.getCountry()) ||
				!l.getLanguage().equals(locale.getLanguage())) {
			Labels.locale = l;
			// Rechargement du bundle
			resourceBundle = ResourceBundle.getBundle(BUNDLE, locale);
		}
	}

}
