/*
 * Cr‚‚ le 9 mars 2006
 *
 * Pour changer le modŠle de ce fichier g‚n‚r‚, allez … :
 * Fenˆtre&gt;Pr‚f‚rences&gt;Java&gt;G‚n‚ration de code&gt;Code et commentaires
 */
package com.arcadsoftware.ae.core.utils;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;


/**
 * Classe de gestion de l'internationalisation des libell‚s<br>
 * Les libell‚s sont stock‚s dans les fichiers labels_xx_XX.properties
 * du packages com.arcadsoftware.exec.config
 * Par d‚faut les libell‚s sont charg‚s en en_US mais l'utilisateur
 * peut changer la localisation en utilisant la m‚thode <code>setLocale(Locale l)</code>
 * qui permet le rechargement des libell‚s dans une langue diff‚rente.
 * @author MD
 *
 */
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

	
	/**
	 * Retourne le libell‚ internationalis‚ en fonction de la cl‚ pass‚e
	 * en paramŠtre. Si cette cl‚ ne correspond pas … une entr‚e dans les
	 * fichiers de libell‚s, c'est cette cl‚ qui est retourn‚e.
	 * @param key String : cl‚ d'entr‚e dans le fichier de traduction 
	 * @return Valeur internationalis‚e correspondant … la cl‚ pass‚e en param‚tre
	 */
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
	/**
	 * M‚thode permettant de changer la localisation des libell‚s.<br>
	 * Si l'objet de type Locale pass‚ en paramŠtre … des valeurs de langue 
	 * ou de pays diff‚rentes de celles utilis‚es, il y aura un rechargement
	 * du ressourceBundle.
	 * @param l : Locale : objet locale de param‚trage de la langue
	 */
	public void setLocal(Locale l) {
		if (!l.getCountry().equals(locale.getCountry()) ||
		   !l.getLanguage().equals(locale.getLanguage())) {			
			Labels.locale = l;
			//Rechargement du bundle
			resourceBundle = ResourceBundle.getBundle(BUNDLE,locale);
		}
	}
	
}
