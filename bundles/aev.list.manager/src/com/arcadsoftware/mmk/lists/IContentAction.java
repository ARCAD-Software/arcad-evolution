package com.arcadsoftware.mmk.lists;


import java.util.Hashtable;

import com.arcadsoftware.ae.core.exceptions.ArcadException;
import com.arcadsoftware.mmk.lists.managers.AbstractFiller;
import com.arcadsoftware.mmk.lists.metadata.StoreItem;

public interface IContentAction {
	
	/**
	 * Fonctions de parcours et de recherche
	 */
	public int  browse() throws ArcadException;
	public int browse(String subquery) throws ArcadException;
	
	public boolean exists(StoreItem item) throws ArcadException;	
	
	/**
	 * Fonctions de gestion de contenu 
	 * @param items
	 * @param checkIfExists
	 * @param replaceIfExists
	 * @return
	 */
	public int addItems(AbstractFiller filler, boolean checkIfExists, boolean replaceIfExists) throws ArcadException; 
	public int addItems(StoreItem[] items, boolean checkIfExists, boolean replaceIfExists) throws ArcadException;	
	public int addItems(StoreItem item, boolean checkIfExists, boolean replaceIfExists) throws ArcadException;
	public int addItems(AbstractList list, boolean checkIfExists, boolean replaceIfExists) throws ArcadException;	
	
	public int updateItems(StoreItem[] items) throws ArcadException;
	public int updateItems(StoreItem item) throws ArcadException;
	public int updateItems(AbstractList fromList) throws ArcadException;	
	public int reinitializeValue(String id,String value) throws ArcadException;	
	
	public int removeItems(StoreItem[] items) throws ArcadException;
	public int removeItems(StoreItem item) throws ArcadException;	
	public int removeItems(AbstractList fromList) throws ArcadException;	
	public int removeItems(String removeQuery) throws ArcadException;	
	public int clearItems() throws ArcadException;	
	
	/**
	 * Extrait les ‚lements de la liste en fonction de la requˆte d'extration
	 * et les ajoute dans la liste cible. 
	 * @param extractQuery
	 * @param targetList
	 * @param clearListBeforeAdding
	 * @param checkIfExists
	 * @param replaceIfExists
	 * @return
	 */
	public int extractItems(String extractQuery,
			                    AbstractList targetList,
			                    boolean clearListBeforeAdding,
			                    boolean checkIfExists,
			                    boolean replaceIfExists)  throws ArcadException;
	
	/**
	 * Ne conserve dans la liste que les donn‚es correspondantes … la
	 * requˆte d'extraction.
	 * @param extractQuery
	 * @return
	 */
	public int extractItems(String extractQuery)  throws ArcadException;	
	
	
	public int removeDuplicate(String orderQuery) throws ArcadException;
	
	public void duplicate(AbstractList toList) throws ArcadException;
		
	public int merge(AbstractList opList,AbstractList resList,
			         boolean checkIfExists, boolean replaceIfExists)  throws ArcadException;	
	public int substract(AbstractList opList,AbstractList resList,
			         boolean checkIfExists, boolean replaceIfExists)  throws ArcadException;
	public int intersect(AbstractList opList,AbstractList resList,
	         boolean checkIfExists, boolean replaceIfExists) throws ArcadException ;
	public int intersect(AbstractList opList,AbstractList resList,
	         boolean checkIfExists, boolean replaceIfExists,Hashtable<String,String> extendedQuery) 
	throws ArcadException ;
	public void load(boolean retrieveProcessInfo,boolean metadataOnly) throws ArcadException ;
	
	public int count(String query) throws ArcadException ;
	
	public void compare(AbstractList opList,
	           AbstractList addedList,boolean adCheckIfExists, boolean adReplaceIfExists,
	           AbstractList commonList,boolean cmCheckIfExists, boolean cmReplaceIfExists,
	           AbstractList deletedList,boolean deCheckIfExists, boolean deReplaceIfExists)
			   throws ArcadException ;

	
}
