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
package com.arcadsoftware.ae.core.settings;

import java.util.ArrayList;

public class Setting {
	private final ArrayList<Category> categories;

	public Setting() {
		categories = new ArrayList<>();
	}

	public void addCategory(final Category c) {
		categories.add(c);
	}

	public ArrayList<Category> getCategories() {
		return categories;
	}

	public Category getCategoryByName(final String category) {
		for (final Category c : categories) {
			if (c.getLabel().equalsIgnoreCase(category)) {
				return c;
			}
		}
		return null;
	}

}
