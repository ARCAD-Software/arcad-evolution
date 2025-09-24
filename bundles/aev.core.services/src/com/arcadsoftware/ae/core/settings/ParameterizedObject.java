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

public abstract class ParameterizedObject {
	protected Setting setting = null;

	private void applySetting() {
		for (final Category category : setting.getCategories()) {
			for (final Form form : category.getList()) {
				for (final ConsoleField field : form.getFields()) {
					if (field instanceof ConsoleProperty) {
						final ConsoleProperty property = (ConsoleProperty) field;
						final String id = property.getId();
						final String value = property.getValue();
						SettingManager.setBeanStringValue(this, id, value);
					}
				}
			}
		}
	}

	public String getSettingFilename() {
		return null;
	}

	public boolean initialize() {
		final String settingFilename = getSettingFilename();
		if (settingFilename != null) {
			setting = SettingManager.loadSettings(settingFilename);
			if (setting != null) {
				applySetting();
				return true;
			} else {
				return false;
			}
		} else {
			return true;
		}
	}

}
