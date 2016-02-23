/*
 * Créé le 4 déc. 2006
 */
package com.arcadsoftware.aev.core.ui.mementos;

import java.util.HashMap;
import java.util.Map;

public class UserPrefsSettings extends ArcadSettings {
	
	private String elementId;
	private Map<String, String> preferences = new HashMap<String, String>();
	
	/**
	 * @param serverName
	 * @param userName
	 */
	public UserPrefsSettings(String serverName, String userName) {
		super(serverName, userName);
	}
	
	public UserPrefsSettings(String serverName, String userName,String elementId) {
		super(serverName, userName);
		this.elementId = elementId;
	}
	
	public UserPrefsSettings(String elementId, Map<String, String> preferences) {
		this("*ALL", "*ALL", elementId, preferences); //$NON-NLS-1$ //$NON-NLS-2$
	}		
	
	/**
	 * @param serverName
	 * @param userName
	 * @param viewerId
	 * @param cols
	 */
	public UserPrefsSettings(String serverName, String userName,
			String viewerId, Map<String, String> preferences) {
		this(serverName, userName,viewerId);
		this.preferences = preferences;
	}
		
	public String getPrefValue(String key){
		return this.preferences.get(key);
	}

	
	public void setPrefValue(String key, String value){
		this.preferences.put(key, value);
	}
	
	/**
	 * @return elementId (any UI element that uses User preferences).
	 */
	public String getElementId() {
		return elementId;
	}
	
	public Map<String, String> getPreferences(){
		return preferences;
	}
}
