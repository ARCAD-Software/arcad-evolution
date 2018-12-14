/*
 * Cr‚‚ le 13 nov. 07
 *
 * TODO Pour changer le modŠle de ce fichier g‚n‚r‚, allez … :
 * Fenˆtre - Pr‚f‚rences - Java - Style de code - ModŠles de code
 */
package com.arcadsoftware.ae.core.substitute;



import com.arcadsoftware.ae.core.utils.Utils;

public class Substituter {
	private static Substituter instance = new Substituter();
	private Substituter(){
		String arcadHome = Utils.getHomeDirectory();
		System.setProperty(IAEAConstants.AEA_ARCAD_HOME, arcadHome);
	}
	
	public String substitute(String value){;		
		//Recherche d'une propri‚t‚		
		String[] variables = Utils.toSubstitute(value);
		for (int i=0;i<variables.length;i++){
			value = substituteProperty(value,variables[i]);
		}
		return value;		
	}
	
	public String substituteProperty(String value,String propertyName){
		value= Utils.substituteProperty(value,propertyName);
		return value;		
	}

	public static Substituter getInstance() {
		return instance;
	}	
	

}
