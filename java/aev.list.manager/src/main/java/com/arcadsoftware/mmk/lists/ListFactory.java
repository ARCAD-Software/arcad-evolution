package com.arcadsoftware.mmk.lists;


import java.io.File;

import com.arcadsoftware.ae.core.utils.AbstractFactory;



/**
 * Classe de gestion des chargements via Spring.<br>
 * Cette classe implémente les mécanismes nécessaires à la prise
 * en charge du chargement des classes via le framework Spring.<br>
 * Cette classe est un singleton.
 * @author MD
 *
 */
public class ListFactory extends AbstractFactory {

	private static ListFactory instance = new ListFactory();
	
	@Override
	protected String getConfigurationFile() {				
		if (arcadHomeFolder.exists())
			return new File(arcadHomeFolder,"lists-settings.xml").getAbsolutePath();
		return null;
	}

	@Override
	protected String getExtensionFolder() {
		return new File(arcadHomeFolder,"lists").getAbsolutePath();
	}

	/* (non-Javadoc)
	 * @see com.arcadsoftware.ae.core.utils.AbstractFactory#initialize()
	 */
	@Override
	protected void initialize() {
		super.initialize();
		ListSettings.setInstance((ListSettings)getBean("list-settings"));		
	}

	/**
	 * Renvoit 
	 * @return the instance ListFactory : 
	 */
	public static ListFactory getInstance() {
		return instance;
	}



}
