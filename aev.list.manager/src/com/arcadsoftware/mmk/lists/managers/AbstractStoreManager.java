package com.arcadsoftware.mmk.lists.managers;

import com.arcadsoftware.ae.core.exceptions.ArcadException;
import com.arcadsoftware.mmk.lists.AbstractList;

public abstract class AbstractStoreManager {

	protected AbstractList list;
	protected AbstractFiller filler;
	
	public AbstractStoreManager(AbstractList list) {
		super();
		this.list = list;
	}
	
	protected boolean initialization()throws ArcadException{return true;};
	protected boolean finalization() throws ArcadException{return true;};
	

	
	public int store() throws ArcadException{
		if (filler!=null) {
			if (initialization()) {
				try {
					return filler.populate();
				} finally {
					finalization();	
				}
			}										
		}
		return -1;
	}	
	
	
	/**
	 * M‚thode permettant l'enregistrement d'une ligne de contenu.<br>
	 * @param item AbstractListItem : item … enregistrer. 
	 * @return boolean : Retourne <code><b>true</b></code> si l'op‚ration 
	 *                   s'est effectu‚e avec succŠs et 
	 *                   <code><b>false</b></code> sinon.
	 */
	public abstract boolean saveItem() throws ArcadException ;

	/**
	 * Renvoit 
	 * @return the filler AbstractPopulater : 
	 */
	public AbstractFiller getFiller() {
		return filler;
	}

	/**
	 * @param filler the filler to set
	 */
	public void setFiller(AbstractFiller populater) {
		this.filler = populater;
	}
	
	

	
	
	

	
	
}
