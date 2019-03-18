package com.arcadsoftware.ae.core.utils;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class Labels {
	private static Labels instance = new Labels();
	
	private static final String BUNDLE = "com.arcadsoftware.exec.config.labels";
	
	private static Locale locale = new Locale("en","EN");
	
	boolean loaded = false;
	static ResourceBundle resourceBundle = null;
	
	
	private Labels(){
		super();
	}


	public static Labels getInstance() {
		return instance;
	}

	public Locale getLocal() {
		return locale;
	}

	public static String resString(String key) {
		resourceBundle = ResourceBundle.getBundle(BUNDLE,locale);
		if (resourceBundle!=null){
			try{
				return resourceBundle.getString(key);
			} catch (MissingResourceException e){
				return key;
			}
			
		}			
		return key;
	}	

	public void setLocal(Locale l) {
		if (!l.getCountry().equals(locale.getCountry()) ||
		   !l.getLanguage().equals(locale.getLanguage())) {			
			Labels.locale = l;
			//Rechargement du bundle
			resourceBundle = ResourceBundle.getBundle(BUNDLE,locale);
		}
	}
	
}
