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
