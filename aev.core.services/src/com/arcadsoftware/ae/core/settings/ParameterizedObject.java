package com.arcadsoftware.ae.core.settings;


public abstract class ParameterizedObject {
	protected Setting setting = null;
	
	public boolean initialize(){
		String settingFilename = getSettingFilename();
		if (settingFilename!=null) {
			setting = SettingManager.loadSettings(settingFilename);
			if (setting!=null) {
				applySetting();
				return true;
			} else {
				return false;	
			}		
		} else {
			return true;
		}
	}
	
	private void applySetting(){
		for(Category category : setting.getCategories()){
			for (Form form : category.getList()){
				for (ConsoleField field : form.getFields()) {
					if (field instanceof ConsoleProperty) {
						ConsoleProperty property = (ConsoleProperty)field;
						String id = property.getId();
						String value = property.getValue();
						SettingManager.setBeanStringValue(this, id, value);
					}
				}
			}
		}
	}
	
	public String getSettingFilename(){
		return null;
	}

}
