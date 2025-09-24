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
package com.arcadsoftware.ae.core.translation;

public class Translator {
	private static Translator instance = new Translator();

	private static ITranslator translatorInstance = null;

	public static Translator getInstance() {
		return instance;
	}

	public static String resString(final String key) {
		if (translatorInstance != null) {
			return translatorInstance.resString(key);
		}
		return "";
	}

	public static String resString(final String key, final String[] substitionMessages) {
		String message = resString(key);
		if (!message.equals("")) {
			for (int i = 0; i < substitionMessages.length; i++) {
				message = message.replace("$" + i, substitionMessages[i]);
			}
		}
		return message;
	}

	private Translator() {
		super();
	}

	public static void setTranslator(final ITranslator t) {
		translatorInstance = t;
	}

}
