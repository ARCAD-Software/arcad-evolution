/*******************************************************************************
 * Copyright (c) 2025 ARCAD Software.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     ARCAD Software - initial API and implementation
 *******************************************************************************/
package com.arcadsoftware.mmk.lists;

import java.util.Date;
import java.util.Hashtable;

import com.arcadsoftware.ae.core.exceptions.ArcadException;
import com.arcadsoftware.ae.core.utils.ListenerList;
import com.arcadsoftware.mmk.lists.managers.AbstractContentManager;
import com.arcadsoftware.mmk.lists.managers.AbstractFiller;
import com.arcadsoftware.mmk.lists.managers.AbstractLoggedObject;
import com.arcadsoftware.mmk.lists.managers.AbstractStoreManager;
import com.arcadsoftware.mmk.lists.metadata.AbstractStoreItemManager;
import com.arcadsoftware.mmk.lists.metadata.ListMetaDatas;
import com.arcadsoftware.mmk.lists.metadata.StoreItem;

public abstract class AbstractArcadList extends AbstractLoggedObject
		implements IContentAction {

	public static final String MODULE_NAME = "LM";

	private int browsedElementCount = 0;

	ListenerList browseListener;
	private AbstractContentManager contentManager = null;
	private int elementCount = 0;
	private int failedElementCount = 0;

	private AbstractFiller filler = null;
	private boolean followBrowsing = true;

	private ListHeader header = new ListHeader();
	private ListMetaDatas metadatas;

	private StoreItem storeItem = new StoreItem();
	private boolean storeItemInitialized = false;
	private AbstractStoreItemManager storeItemTransformer = null;

	private AbstractStoreManager storeManager = null;
	private int succeedElementCount = 0;

	public AbstractArcadList() {
		super();
		createMetaDatas();
		initialize();
		browseListener = new ListenerList(3);
	}

	/*-----------------------------------------------------------------
	 *                  Gestion du parcours de la liste
	 ------------------------------------------------------------------*/
	public void addBrowseListener(final IListBrowseListener listener) {
		browseListener.add(listener);
	}

	/**
	 * Ajout d'élément à partir d'un objet de remplissage.<br>
	 * Cette méthode utilise le parcours d'éléments spécifiques fournit par l'objet filler passé en paramètre.
	 * 
	 * @param filler
	 *            AbstractFiller :
	 * @param checkIfExists
	 *            boolean :
	 * @param replaceIfExists
	 *            boolean :
	 */
	@Override
	public int addItems(final AbstractFiller filler,
			final boolean checkIfExists, final boolean replaceIfExists) {
		contentManager = getContentManager();
		if (contentManager != null) {
			try {
				return contentManager.addItems(filler, checkIfExists, replaceIfExists);
			} catch (final ArcadException e) {
				logError(MODULE_NAME, e);
			}
		}
		return -1;
	}

	@Override
	public int addItems(final AbstractArcadList list, final boolean checkIfExists, final boolean replaceIfExists) {
		contentManager = getContentManager();
		if (contentManager != null) {
			if (list != null) {
				try {
					return contentManager.addItems(list, checkIfExists, replaceIfExists);
				} catch (final ArcadException e) {
					logError(MODULE_NAME, e);
				}
			} else {
				logError(MODULE_NAME, "List must not be null");
			}
		}
		return -1;
	}

	@Override
	public int addItems(final StoreItem item, final boolean checkIfExists, final boolean replaceIfExists) {
		contentManager = getContentManager();
		if (contentManager != null) {
			try {
				return contentManager.addItems(item, checkIfExists, replaceIfExists);
			} catch (final ArcadException e) {
				logError(MODULE_NAME, e);
			}
		}
		return -1;
	}

	@Override
	public int addItems(final StoreItem[] items, final boolean checkIfExists, final boolean replaceIfExists) {
		contentManager = getContentManager();
		if (contentManager != null) {
			try {
				return contentManager.addItems(items, checkIfExists, replaceIfExists);
			} catch (final ArcadException e) {
				logError(MODULE_NAME, e);
			}
		}
		return -1;
	}

	@Override
	public int browse() {
		return browse(null);
	}

	@Override
	public int browse(final String subquery) {
		contentManager = getContentManager();
		if (contentManager != null) {
			browsedElementCount = 0;
			followBrowsing = true;
			try {
				return contentManager.browse(subquery);
			} catch (final ArcadException e) {
				logError(MODULE_NAME, e);
			}
		}
		return -1;
	}

	@Override
	public int clearItems() {
		contentManager = getContentManager();
		if (contentManager != null) {
			try {
				return contentManager.clearItems();
			} catch (final ArcadException e) {
				logError(MODULE_NAME, e);
			}
		}
		return -1;
	}

	public AbstractArcadList cloneList() {
		final AbstractArcadList l = createCloneList();
		l.setMetadatas(getMetadatas().duplicate());
		l.initStoreItem();
		return l;
	}

	@Override
	public void compare(final AbstractArcadList opList,
			final AbstractArcadList addedList, final boolean adCheckIfExists, final boolean adReplaceIfExists,
			final AbstractArcadList commonList, final boolean cmCheckIfExists, final boolean cmReplaceIfExists,
			final AbstractArcadList deletedList, final boolean deCheckIfExists, final boolean deReplaceIfExists) {
		contentManager = getContentManager();
		if (contentManager != null) {
			if (opList == null) {
				logError(MODULE_NAME, "Operand List must not be null");
			} else {
				try {
					contentManager.compare(opList,
							addedList, adCheckIfExists, adReplaceIfExists,
							commonList, cmCheckIfExists, cmReplaceIfExists,
							deletedList, deCheckIfExists, deReplaceIfExists);
				} catch (final ArcadException e) {
					logError(MODULE_NAME, e);
				}
			}
		}
	}

	@Override
	public int count(final String query) {
		contentManager = getContentManager();
		if (contentManager != null) {
			try {
				return contentManager.count(query);
			} catch (final ArcadException e) {
				logError(MODULE_NAME, e);
			}
		}
		return -1;
	}

	public abstract AbstractArcadList createCloneList();

	/**
	 * Cette méthode permet de définir le StoreManager à utiliser par la liste pour enregistrer les données en
	 * provenance d'un objet filler.
	 * 
	 * @return AbstractStoreManager : StoreManager à utiliser
	 */
	public abstract AbstractContentManager createContentManager();

	/**
	 * Cette méthode permet de définir la description des données de la liste.<br>
	 * C'est dans cette méthode que vous définirez
	 */
	protected void createMetaDatas() {
		metadatas = new ListMetaDatas();
		if (getStoreItemManager() != null) {
			getStoreItemManager().createMetadata(metadatas);
		}
	}

	/**
	 * Cette méthode permet de définir le StoreManager à utiliser par la liste pour enregistrer les données en
	 * provenance d'un objet filler.
	 * 
	 * @return AbstractStoreManager : StoreManager à utiliser
	 */
	public abstract AbstractStoreItemManager createStoreItemManager(AbstractArcadList list);

	/**
	 * Cette méthode permet de définir le StoreManager à utiliser par la liste pour enregistrer les données en
	 * provenance d'un objet filler.
	 * 
	 * @return AbstractStoreManager : StoreManager à utiliser
	 */
	public abstract AbstractStoreManager createStoreManager();

	/*
	 * (non-Javadoc)
	 * @see com.arcadsoftware.mmk.lists.impl.xml.IContentAction#duplicate(com.arcadsoftware.mmk.lists.AbstractList)
	 */
	@Override
	public void duplicate(final AbstractArcadList toList) {
		contentManager = getContentManager();
		if (contentManager != null) {
			if (toList != null) {
				try {
					contentManager.duplicate(toList);
				} catch (final ArcadException e) {
					logError(MODULE_NAME, e);
				}
			} else {
				logError(MODULE_NAME, "Result List must not be null");
			}
		}
	}

	@Override
	public boolean exists(final StoreItem item) {
		contentManager = getContentManager();
		if (contentManager != null) {
			try {
				return contentManager.exists(item);
			} catch (final ArcadException e) {
				logError(MODULE_NAME, e);
			}
		}
		return false;
	}

	@Override
	public int extractItems(final String extractQuery) {
		contentManager = getContentManager();
		if (contentManager != null) {
			try {
				return contentManager.extractItems(extractQuery);
			} catch (final ArcadException e) {
				logError(MODULE_NAME, e);
			}
		}
		return -1;
	}

	@Override
	public int extractItems(final String extractQuery,
			final AbstractArcadList targetList,
			final boolean clearListBeforeAdding,
			final boolean checkIfExists,
			final boolean replaceIfExists) {
		contentManager = getContentManager();
		if (contentManager != null) {
			if (targetList != null) {
				try {
					return contentManager.extractItems(extractQuery,
							targetList,
							clearListBeforeAdding,
							checkIfExists,
							replaceIfExists);
				} catch (final ArcadException e) {
					logError(MODULE_NAME, e);
				}
			} else {
				logError(MODULE_NAME, "Result List must not be null");
			}
		}
		return -1;
	}

	public void fireBrowseEvent() {
		browsedElementCount++;
		final int count = browseListener.size();
		for (int i = 0; i < count; i++) {
			final IListBrowseListener listener = (IListBrowseListener) browseListener.getListeners()[i];
			try {
				listener.elementBrowsed(storeItem);
			} catch (final Exception e) {
				removeBrowseListener(listener);
			}
		}
	}

	/**
	 * @return the browsedElementCount int :
	 */
	public int getBrowsedElementCount() {
		return browsedElementCount;
	}

	public AbstractContentManager getContentManager() {
		if (contentManager == null) {
			contentManager = createContentManager();
		}
		return contentManager;
	}

	/**
	 * Renvoit
	 * 
	 * @return the elementCount int :
	 */
	public int getElementCount() {
		return elementCount;
	}

	/**
	 * Renvoit
	 * 
	 * @return the failedElementCount int :
	 */
	public int getFailedElementCount() {
		return failedElementCount;
	}

	/**
	 * Renvoit
	 * 
	 * @return the filler AbstractFiller :
	 */
	public AbstractFiller getFiller() {
		return filler;
	}

	/**
	 * @return the header ListHeader :
	 */
	public ListHeader getHeader() {
		return header;
	}

	/**
	 * Renvoit
	 * 
	 * @return the metadatas ListMetaDatas :
	 */
	public ListMetaDatas getMetadatas() {
		return metadatas;
	}

	/**
	 * Renvoit
	 * 
	 * @return the processedElementCount int :
	 */
	public int getProcessedElementCount() {
		return failedElementCount + succeedElementCount;
	}

	/**
	 * Renvoit
	 * 
	 * @return the storeItem StoreItem :
	 */
	public StoreItem getStoreItem() {
		return storeItem;
	}

	/*-----------------------------------------------------------------
	 *                    Gestion des metadatas
	 ------------------------------------------------------------------*/
	public AbstractStoreItemManager getStoreItemManager() {
		if (storeItemTransformer == null) {
			storeItemTransformer = createStoreItemManager(this);
		}
		return storeItemTransformer;
	}

	private AbstractStoreManager getStoreManager() {
		if (storeManager == null) {
			storeManager = createStoreManager();
		}
		return storeManager;
	}

	/**
	 * Renvoit
	 * 
	 * @return the succeedElementCount int :
	 */
	public int getSucceedElementCount() {
		return succeedElementCount;
	}

	public void initialize() {
		final Date now = new Date();
		getHeader().setCreatedThe(now);
		getHeader().setCreatedBy(System.getProperty("user.name"));
		getHeader().setLastModifiedThe(now);
		getHeader().setLastModifiedBy(System.getProperty("user.name"));
	}

	public void initStoreItem() {
		storeItem.setMetadatas(metadatas);
		storeItemInitialized = true;
	}

	@Override
	public int intersect(final AbstractArcadList opList, final AbstractArcadList resList,
			final boolean checkIfExists, final boolean replaceIfExists) {
		return this.intersect(opList, resList, checkIfExists, replaceIfExists, null);
	}

	@Override
	public int intersect(final AbstractArcadList opList, final AbstractArcadList resList,
			final boolean checkIfExists, final boolean replaceIfExists, final Hashtable<String, String> extendedQuery) {
		contentManager = getContentManager();
		if (contentManager != null) {
			if (opList == null) {
				logError(MODULE_NAME, "Operand List must not be null");
			} else if (resList == null) {
				logError(MODULE_NAME, "Result List must not be null");
			} else {
				try {
					return contentManager.intersect(opList, resList, checkIfExists,
							replaceIfExists, extendedQuery);
				} catch (final ArcadException e) {
					logError(MODULE_NAME, e);
				}
			}
		}
		return -1;
	}

	/**
	 * Renvoit
	 * 
	 * @return the followBrowsing boolean :
	 */
	public boolean isFollowBrowsing() {
		return followBrowsing;
	}

	/**
	 * Renvoit
	 * 
	 * @return the storeItemInitialized boolean :
	 */
	public boolean isStoreItemInitialized() {
		return storeItemInitialized;
	}

	public void load(final boolean retrieveProcessInfo) {
		load(retrieveProcessInfo, false);
	}

	@Override
	public void load(final boolean retrieveProcessInfo, final boolean metadataOnly) {
		contentManager = getContentManager();
		if (contentManager != null) {
			try {
				contentManager.load(retrieveProcessInfo, metadataOnly);
			} catch (final ArcadException e) {
				logError(MODULE_NAME, e.getCause());
			}
		}
	}

	@Override
	public int merge(final AbstractArcadList opList, final AbstractArcadList resList,
			final boolean checkIfExists, final boolean replaceIfExists) {
		contentManager = getContentManager();
		if (contentManager != null) {
			if (opList == null) {
				logError(MODULE_NAME, "Operand List must not be null");
			} else if (resList == null) {
				logError(MODULE_NAME, "Result List must not be null");
			} else {
				try {
					return contentManager.merge(opList, resList, checkIfExists, replaceIfExists);
				} catch (final ArcadException e) {
					logError(MODULE_NAME, e);
				}
			}
		}
		return -1;
	}

	public int populate() {
		storeManager = getStoreManager();
		if (storeManager != null) {
			storeManager.setFiller(getFiller());
			try {
				return storeManager.store();
			} catch (final ArcadException e) {
				logError(MODULE_NAME, e);
			}
		}
		return -1;
	}

	@Override
	public int reinitializeValue(final String id, final String value) {
		contentManager = getContentManager();
		if (contentManager != null) {
			try {
				return contentManager.reinitializeValue(id, value);
			} catch (final ArcadException e) {
				logError(MODULE_NAME, e);
			}
		}
		return -1;
	}

	public void removeBrowseListener(final IListBrowseListener listener) {
		browseListener.remove(listener);
	}

	public int removeDuplicate() {
		return removeDuplicate("");
	}

	@Override
	public int removeDuplicate(final String orderQuery) {
		contentManager = getContentManager();
		if (contentManager != null) {
			try {
				return contentManager.removeDuplicate(orderQuery);
			} catch (final ArcadException e) {
				logError(MODULE_NAME, e);
			}
		}
		return -1;
	}

	@Override
	public int removeItems(final AbstractArcadList fromList) {
		contentManager = getContentManager();
		if (contentManager != null) {
			if (fromList != null) {
				try {
					return contentManager.removeItems(fromList);
				} catch (final ArcadException e) {
					logError(MODULE_NAME, e);
				}
			} else {
				logError(MODULE_NAME, "List must not be null");
			}

		}
		return -1;
	}

	@Override
	public int removeItems(final StoreItem item) {
		contentManager = getContentManager();
		if (contentManager != null) {
			try {
				return contentManager.removeItems(item);
			} catch (final ArcadException e) {
				logError(MODULE_NAME, e);
			}
		}
		return -1;
	}

	@Override
	public int removeItems(final StoreItem[] items) {
		contentManager = getContentManager();
		if (contentManager != null) {
			try {
				return contentManager.removeItems(items);
			} catch (final ArcadException e) {
				logError(MODULE_NAME, e);
			}
		}
		return -1;
	}

	@Override
	public int removeItems(final String removeQuery) {
		contentManager = getContentManager();
		if (contentManager != null) {
			try {
				return contentManager.removeItems(removeQuery);
			} catch (final ArcadException e) {
				logError(MODULE_NAME, e);
			}
		}
		return -1;
	}

	/*-----------------------------------------------------------------
	 *                    Gestion du remplissage de la liste
	 ------------------------------------------------------------------*/
	/**
	 * Cette méthode permet l'ajout d'un objet quelconque comme item de la liste.<br>
	 * La conversion est réaliser à l'aide de la méthode {@link #toListItem(Object) toListItem(Object)}
	 * 
	 * @param Object
	 *            element : Elément à ajouter dans la liste.
	 */
	public void saveItem() {
		try {
			getStoreManager().saveItem();
		} catch (final ArcadException e) {
			logError(MODULE_NAME, e);
		}
	}

	/**
	 * @param elementCount
	 *            the elementCount to set
	 */
	public void setElementCount(final int elementCount) {
		this.elementCount = elementCount;
	}

	/**
	 * @param failedElementCount
	 *            the failedElementCount to set
	 */
	public void setFailedElementCount(final int failedElementCount) {
		this.failedElementCount = failedElementCount;
	}

	/**
	 * @param filler
	 *            the filler to set
	 */
	public void setFiller(final AbstractFiller filler) {
		this.filler = filler;
	}

	/**
	 * @param followBrowsing
	 *            the followBrowsing to set
	 */
	public void setFollowBrowsing(final boolean followBrowsing) {
		this.followBrowsing = followBrowsing;
	}

	/**
	 * @param header
	 *            the header to set
	 */
	public void setHeader(final ListHeader header) {
		this.header = header;
	}

	/**
	 * @param metadatas
	 *            the metadatas to set
	 */
	public void setMetadatas(final ListMetaDatas metadatas) {
		this.metadatas = metadatas;
	}

	/**
	 * @param storeItem
	 *            the storeItem to set
	 */
	public void setStoreItem(final StoreItem storeItem) {
		this.storeItem = storeItem;
	}

	/**
	 * @param storeItemInitialized
	 *            the storeItemInitialized to set
	 */
	public void setStoreItemInitialized(final boolean storeItemInitialized) {
		this.storeItemInitialized = storeItemInitialized;
	}

	/**
	 * @param succeedElementCount
	 *            the succeedElementCount to set
	 */
	public void setSucceedElementCount(final int succeedElementCount) {
		this.succeedElementCount = succeedElementCount;
	}

	@Override
	public int substract(final AbstractArcadList opList,
			final AbstractArcadList resList,
			final boolean checkIfExists, final boolean replaceIfExists) {
		contentManager = getContentManager();
		if (contentManager != null) {
			if (opList == null) {
				logError(MODULE_NAME, "Operand List must not be null");
			} else if (resList == null) {
				logError(MODULE_NAME, "Result List must not be null");
			} else {
				try {
					return contentManager.substract(opList, resList, checkIfExists, replaceIfExists);
				} catch (final ArcadException e) {
					logError(MODULE_NAME, e);
				}
			}
		}
		return -1;
	}

	/**
	 * Cette méthode permet de définir la conversion de données entre l'objet <code>element</code> passé en paramètre et
	 * l'objet StoreItem chargé du stockage temporaire.<br>
	 * 
	 * @param element
	 *            : Objet à convertir.
	 */
	public StoreItem toStoreItem(final Object element) {
		if (getStoreItemManager() != null) {
			return getStoreItemManager().toStoreItem(element);
		}
		return null;
	}

	@Override
	public int updateItems(final AbstractArcadList fromList) {
		contentManager = getContentManager();
		if (contentManager != null) {
			if (fromList != null) {
				try {
					return contentManager.updateItems(fromList);
				} catch (final ArcadException e) {
					logError(MODULE_NAME, e);
				}
			} else {
				logError(MODULE_NAME, "List must not be null");
			}
		}
		return -1;
	}

	@Override
	public int updateItems(final StoreItem item) {
		contentManager = getContentManager();
		if (contentManager != null) {
			try {
				return contentManager.updateItems(item);
			} catch (final ArcadException e) {
				logError(MODULE_NAME, e);
			}
		}
		return -1;
	}

	@Override
	public int updateItems(final StoreItem[] items) {
		contentManager = getContentManager();
		if (contentManager != null) {
			try {
				return contentManager.updateItems(items);
			} catch (final ArcadException e) {
				logError(MODULE_NAME, e);
			}
		}
		return -1;
	}
}
