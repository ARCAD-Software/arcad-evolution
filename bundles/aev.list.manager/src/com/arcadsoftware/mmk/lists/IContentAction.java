package com.arcadsoftware.mmk.lists;

import java.util.Hashtable;

import com.arcadsoftware.ae.core.exceptions.ArcadException;
import com.arcadsoftware.mmk.lists.managers.AbstractFiller;
import com.arcadsoftware.mmk.lists.metadata.StoreItem;

public interface IContentAction {

	/**
	 * Fonctions de gestion de contenu
	 * 
	 * @param items
	 * @param checkIfExists
	 * @param replaceIfExists
	 * @return
	 */
	int addItems(AbstractFiller filler, boolean checkIfExists, boolean replaceIfExists) throws ArcadException;

	int addItems(AbstractArcadList list, boolean checkIfExists, boolean replaceIfExists) throws ArcadException;

	int addItems(StoreItem item, boolean checkIfExists, boolean replaceIfExists) throws ArcadException;

	int addItems(StoreItem[] items, boolean checkIfExists, boolean replaceIfExists) throws ArcadException;

	/**
	 * Fonctions de parcours et de recherche
	 */
	int browse() throws ArcadException;

	int browse(String subquery) throws ArcadException;

	int clearItems() throws ArcadException;

	void compare(AbstractArcadList opList,
			AbstractArcadList addedList, boolean adCheckIfExists, boolean adReplaceIfExists,
			AbstractArcadList commonList, boolean cmCheckIfExists, boolean cmReplaceIfExists,
			AbstractArcadList deletedList, boolean deCheckIfExists, boolean deReplaceIfExists)
			throws ArcadException;

	int count(String query) throws ArcadException;

	void duplicate(AbstractArcadList toList) throws ArcadException;

	boolean exists(StoreItem item) throws ArcadException;

	/**
	 * Ne conserve dans la liste que les données correspondantes à la requète d'extraction.
	 * 
	 * @param extractQuery
	 * @return
	 */
	int extractItems(String extractQuery) throws ArcadException;

	/**
	 * Extrait les élements de la liste en fonction de la requète d'extration et les ajoute dans la liste cible.
	 * 
	 * @param extractQuery
	 * @param targetList
	 * @param clearListBeforeAdding
	 * @param checkIfExists
	 * @param replaceIfExists
	 * @return
	 */
	int extractItems(String extractQuery,
			AbstractArcadList targetList,
			boolean clearListBeforeAdding,
			boolean checkIfExists,
			boolean replaceIfExists) throws ArcadException;

	int intersect(AbstractArcadList opList, AbstractArcadList resList,
			boolean checkIfExists, boolean replaceIfExists) throws ArcadException;

	int intersect(AbstractArcadList opList, AbstractArcadList resList,
			boolean checkIfExists, boolean replaceIfExists, Hashtable<String, String> extendedQuery)
			throws ArcadException;

	void load(boolean retrieveProcessInfo, boolean metadataOnly) throws ArcadException;

	int merge(AbstractArcadList opList, AbstractArcadList resList,
			boolean checkIfExists, boolean replaceIfExists) throws ArcadException;

	int reinitializeValue(String id, String value) throws ArcadException;

	int removeDuplicate(String orderQuery) throws ArcadException;

	int removeItems(AbstractArcadList fromList) throws ArcadException;

	int removeItems(StoreItem item) throws ArcadException;

	int removeItems(StoreItem[] items) throws ArcadException;

	int removeItems(String removeQuery) throws ArcadException;

	int substract(AbstractArcadList opList, AbstractArcadList resList,
			boolean checkIfExists, boolean replaceIfExists) throws ArcadException;

	int updateItems(AbstractArcadList fromList) throws ArcadException;

	int updateItems(StoreItem item) throws ArcadException;

	int updateItems(StoreItem[] items) throws ArcadException;

}
