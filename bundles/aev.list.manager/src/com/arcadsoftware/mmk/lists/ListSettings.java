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

public class ListSettings {

	private static ListSettings instance = null;

	public static ListSettings getInstance() {
		if (instance == null) {
			instance = new ListSettings();
		}
		return instance;
	}

	public static void setInstance(final ListSettings instance) {
		ListSettings.instance = instance;
	}

	private boolean caseSensitive = false;

	/**
	 * 
	 */
	private ListSettings() {
		super();
	}

	/**
	 * Renvoit
	 * 
	 * @return the caseSensitive boolean :
	 */
	public boolean isCaseSensitive() {
		return caseSensitive;
	}

	/**
	 * @param caseSensitive
	 *            the caseSensitive to set
	 */
	public void setCaseSensitive(final boolean caseSensitive) {
		this.caseSensitive = caseSensitive;
	}

}
