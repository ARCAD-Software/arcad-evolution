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




public abstract class AbstractList  extends AbstractLoggedObject
implements IContentAction{
	
	public static final String MODULE_NAME = "LM";

	private ListHeader header = new ListHeader();

	private ListMetaDatas metadatas;
	private StoreItem storeItem = new StoreItem(); 			
	private AbstractFiller filler = null;	
	private AbstractStoreManager storeManager = null;
		
	private AbstractContentManager contentManager = null;
	private AbstractStoreItemManager storeItemTransformer = null;
	
	
	
	ListenerList browseListener;
	private int browsedElementCount = 0;
	
	private int elementCount = 0;
	private int succeedElementCount = 0;	
	private int failedElementCount = 0;		
		
	private boolean followBrowsing = true;
	private boolean storeItemInitialized = false;

	
	public AbstractList(){
		super();	
		createMetaDatas();
		initialize();		
		browseListener = new ListenerList(3);		
	}
	
	public void initialize(){
		Date now = new Date();
		this.getHeader().setCreatedThe(now);
		this.getHeader().setCreatedBy(System.getProperty("user.name"));
		this.getHeader().setLastModifiedThe(now);		
		this.getHeader().setLastModifiedBy(System.getProperty("user.name"));		
	}
	

	/**
	 * Renvoit 
	 * @return the filler AbstractFiller : 
	 */
	public AbstractFiller getFiller() {
		return filler;
	}

	/**
	 * @param filler the filler to set
	 */
	public void setFiller(AbstractFiller filler) {
		this.filler = filler;
	}
	
	/**
	 * @return the browsedElementCount int : 
	 */
	public int getBrowsedElementCount() {
		return browsedElementCount;
	}	
	/**
	 * @return the header ListHeader : 
	 */
	public ListHeader getHeader() {
		return header;
	}

	/**
	 * @param header the header to set
	 */
	public void setHeader(ListHeader header) {
		this.header = header;
	}
	
	/**
	 * Renvoit 
	 * @return the elementCount int : 
	 */
	public int getElementCount() {
		return elementCount;
	}

	/**
	 * @param elementCount the elementCount to set
	 */
	public void setElementCount(int elementCount) {
		this.elementCount = elementCount;
	}

	/**
	 * Renvoit 
	 * @return the failedElementCount int : 
	 */
	public int getFailedElementCount() {
		return failedElementCount;
	}

	/**
	 * @param failedElementCount the failedElementCount to set
	 */
	public void setFailedElementCount(int failedElementCount) {
		this.failedElementCount = failedElementCount;
	}

	/**
	 * Renvoit 
	 * @return the processedElementCount int : 
	 */
	public int getProcessedElementCount() {
		return failedElementCount+succeedElementCount;
	}

	/**
	 * Renvoit 
	 * @return the succeedElementCount int : 
	 */
	public int getSucceedElementCount() {
		return succeedElementCount;
	}

	/**
	 * @param succeedElementCount the succeedElementCount to set
	 */
	public void setSucceedElementCount(int succeedElementCount) {
		this.succeedElementCount = succeedElementCount;
	}	
	
	/**
	 * Renvoit 
	 * @return the followBrowsing boolean : 
	 */
	public boolean isFollowBrowsing() {
		return followBrowsing;
	}

	/**
	 * @param followBrowsing the followBrowsing to set
	 */
	public void setFollowBrowsing(boolean followBrowsing) {
		this.followBrowsing = followBrowsing;
	}	
	/*-----------------------------------------------------------------
	 *                    Gestion du remplissage de la liste
	 ------------------------------------------------------------------*/
	/**
	 * Cette méthode permet l'ajout d'un objet quelconque comme item
	 * de la liste.<br> 
	 * La conversion est réaliser à l'aide de la méthode {@link #toListItem(Object)
	 * toListItem(Object)} 
	 * @param Object element : Elément à ajouter dans la liste.
	 */
	public void saveItem(){
		try {
			getStoreManager().saveItem();
		} catch (ArcadException e) {
			logError(MODULE_NAME,e);
		}
	}	
	
	private AbstractStoreManager getStoreManager(){
		if (storeManager==null) {
			storeManager = createStoreManager();
		}		
		return storeManager;
	}
	
	public int populate(){
		storeManager = getStoreManager();
		if (storeManager!=null) {
			storeManager.setFiller(getFiller());
			try {
				return storeManager.store();
			} catch (ArcadException e) {
				logError(MODULE_NAME,e);
			}			
		}
		return -1;
	}
	/*-----------------------------------------------------------------
	 *                    Gestion des metadatas
	 ------------------------------------------------------------------*/
	public AbstractStoreItemManager getStoreItemManager() {
		if (storeItemTransformer==null) {
			storeItemTransformer=createStoreItemManager(this);
		}
		return storeItemTransformer;
	}
	
	
	/**
	 * Cette méthode permet de définir la description des données
	 * de la liste.<br>
	 * C'est dans cette méthode que vous définirez 
	 *
	 */		
	protected void createMetaDatas() {
		metadatas = new ListMetaDatas();
		if (getStoreItemManager()!=null) {
			getStoreItemManager().createMetadata(metadatas);
		}
	}
	
	/**
	 * Renvoit 
	 * @return the metadatas ListMetaDatas : 
	 */
	public ListMetaDatas getMetadatas() {
		return metadatas;
	}

	/**
	 * @param metadatas the metadatas to set
	 */
	public void setMetadatas(ListMetaDatas metadatas) {
		this.metadatas = metadatas;
	}	
	/*-----------------------------------------------------------------
	 *                  Gestion du parcours de la liste
	 ------------------------------------------------------------------*/
	public void addBrowseListener(IListBrowseListener listener) {
		browseListener.add(listener);
	}
	public void removeBrowseListener(IListBrowseListener listener) {
		browseListener.remove(listener);
	}	
	
	public void fireBrowseEvent(){
		browsedElementCount++;
		int count = browseListener.size();
		for (int i=0;i<count;i++) {
			IListBrowseListener listener = 
				(IListBrowseListener)this.browseListener.getListeners()[i];						
    		try {
    			listener.elementBrowsed(storeItem);
    		} catch(Exception e) {
    			removeBrowseListener(listener);
    		}
		}	
	}

	/**
	 * Renvoit 
	 * @return the storeItem StoreItem : 
	 */
	public StoreItem getStoreItem() {
		return storeItem;
	}

	public void initStoreItem() {
		storeItem.setMetadatas(metadatas);
		storeItemInitialized = true;
	}
	
	/**
	 * @param storeItem the storeItem to set
	 */
	public void setStoreItem(StoreItem storeItem) {
		this.storeItem = storeItem;
	}	
	
	/**
	 * Cette méthode permet de définir la conversion de données
	 * entre l'objet <code>element</code> passé en paramètre
	 * et l'objet StoreItem chargé du stockage temporaire.<br> 
	 * @param element : Objet à convertir.
	 */
	public StoreItem toStoreItem(Object element) {
		if (getStoreItemManager()!=null)
			return getStoreItemManager().toStoreItem(element);
		return null;		
	}
	
	public AbstractContentManager getContentManager(){
		if (contentManager==null) {
			contentManager = createContentManager();
		}
		return contentManager;
	}

	
	public int browse(String subquery) {
		contentManager = getContentManager();
		if (contentManager!=null) {
			browsedElementCount=0;
			followBrowsing=true;
			try {
				return contentManager.browse(subquery);
			} catch (ArcadException e) {
				logError(MODULE_NAME,e);
			}
		}		
		return -1;
	}	
	
	public int browse() {
		return browse(null);		
	}
	
	public boolean exists(StoreItem item) {
		contentManager = getContentManager();
		if (contentManager!=null) {
			try {
				return contentManager.exists(item);
			} catch (ArcadException e) {
				logError(MODULE_NAME,e);
			}
		}
		return false;
	}
		
	/**
	 * Ajout d'élément à partir d'un objet de remplissage.<br>
	 * Cette méthode utilise le parcours d'éléments spécifiques fournit
	 * par l'objet filler passé en paramètre.
	 * @param filler AbstractFiller :	 
	 * @param checkIfExists boolean : 
	 * @param replaceIfExists boolean :	  
	 */
	public int addItems(AbstractFiller filler,
			            boolean checkIfExists, boolean replaceIfExists) {
		contentManager = getContentManager();
		if (contentManager!=null) {
			try {
				return contentManager.addItems(filler,checkIfExists,replaceIfExists);
			} catch (ArcadException e) {
				logError(MODULE_NAME,e);
			}
		}
		return -1;		
	}

	public int addItems(StoreItem[] items,boolean checkIfExists, boolean replaceIfExists) {
		contentManager = getContentManager();
		if (contentManager!=null) {
			try {
				return contentManager.addItems(items,checkIfExists,replaceIfExists);
			} catch (ArcadException e) {
				logError(MODULE_NAME,e);
			}
		}
		return -1;		
	}

	public int addItems(StoreItem item, boolean checkIfExists, boolean replaceIfExists) {
		contentManager = getContentManager();
		if (contentManager!=null) {
			try {
				return contentManager.addItems(item,checkIfExists,replaceIfExists);
			} catch (ArcadException e) {
				logError(MODULE_NAME,e);
			}
		}
		return -1;		
	}		

	public int addItems(AbstractList list, boolean checkIfExists, boolean replaceIfExists) {
		contentManager = getContentManager();
		if (contentManager!=null) {
			if (list!=null) {
				try {
					return contentManager.addItems(list,checkIfExists,replaceIfExists);
				} catch (ArcadException e) {
					logError(MODULE_NAME,e);
				}
			} else 
				logError(MODULE_NAME,"List must not be null");
		}
		return -1;	
	}

	public int removeItems(StoreItem[] items) {
		contentManager = getContentManager();
		if (contentManager!=null) {
			try {
				return contentManager.removeItems(items);
			} catch (ArcadException e) {
				logError(MODULE_NAME,e);
			}
		}
		return -1;		
	}

	public int removeItems(StoreItem item) {
		contentManager = getContentManager();
		if (contentManager!=null) {
			try {
				return contentManager.removeItems(item);
			} catch (ArcadException e) {
				logError(MODULE_NAME,e);
			}
		}
		return -1;		
	}

	public int removeItems(String removeQuery) {
		contentManager = getContentManager();
		if (contentManager!=null) {
			try {
				return contentManager.removeItems(removeQuery);
			} catch (ArcadException e) {
				logError(MODULE_NAME,e);
			}
		}
		return -1;		
	}
	
	public int removeItems(AbstractList fromList) {
		contentManager = getContentManager();
		if (contentManager!=null) {
			if (fromList!=null) {
				try {
					return contentManager.removeItems(fromList);
				} catch (ArcadException e) {
					logError(MODULE_NAME,e);
				}
			} else 
				logError(MODULE_NAME,"List must not be null");
			
		}
		return -1;
	}
	
	public int clearItems() {
		contentManager = getContentManager();
		if (contentManager!=null) {
			try {
				return contentManager.clearItems();
			} catch (ArcadException e) {
				logError(MODULE_NAME,e);
			}
		}
		return -1;		
	}	

	public int updateItems(StoreItem[] items) {
		contentManager = getContentManager();
		if (contentManager!=null) {
			try {
				return contentManager.updateItems(items);
			} catch (ArcadException e) {
				logError(MODULE_NAME,e);
			}
		}
		return -1;		
	}

	public int updateItems(StoreItem item) {
		contentManager = getContentManager();
		if (contentManager!=null) {
			try {
				return contentManager.updateItems(item);
			} catch (ArcadException e) {
				logError(MODULE_NAME,e);
			}
		}
		return -1;		
	}

	public int updateItems(AbstractList fromList) {
		contentManager = getContentManager();
		if (contentManager!=null) {
			if (fromList!=null) {
				try {
					return contentManager.updateItems(fromList);
				} catch (ArcadException e) {
					logError(MODULE_NAME,e);
				}
			} else 
				logError(MODULE_NAME,"List must not be null");
		}
		return -1;
	}	
	
	public int extractItems(String extractQuery) {
		contentManager = getContentManager();
		if (contentManager!=null) {
			try {
				return contentManager.extractItems(extractQuery);
			} catch (ArcadException e) {
				logError(MODULE_NAME,e);
			}
		}
		return -1;
	}

	public int extractItems(String extractQuery, 
			                    AbstractList targetList, 
			                    boolean clearListBeforeAdding, 
			                    boolean checkIfExists, 
			                    boolean replaceIfExists) {
		contentManager = getContentManager();
		if (contentManager!=null) {
			if (targetList!=null) {
				try {
					return contentManager.extractItems(extractQuery,
							                           targetList,
							                           clearListBeforeAdding,
							                           checkIfExists,
							                           replaceIfExists);
				} catch (ArcadException e) {
					logError(MODULE_NAME,e);
				}
			} else 
				logError(MODULE_NAME,"Result List must not be null");
		}
		return -1;
	}

	public int removeDuplicate() {
		return removeDuplicate("");
	}
	
	public int removeDuplicate(String orderQuery) {
		contentManager = getContentManager();
		if (contentManager!=null) {
			try {
				return contentManager.removeDuplicate(orderQuery);
			} catch (ArcadException e) {
				logError(MODULE_NAME,e);
			}
		}
		return -1;
	}

	public int reinitializeValue(String id,String value) {
		contentManager = getContentManager();
		if (contentManager!=null) {
			try {
				return contentManager.reinitializeValue(id,value);
			} catch (ArcadException e) {
				logError(MODULE_NAME,e);
			}
		}
		return -1;
	}	
	
	/* (non-Javadoc)
	 * @see com.arcadsoftware.mmk.lists.impl.xml.IContentAction#duplicate(com.arcadsoftware.mmk.lists.AbstractList)
	 */
	public void duplicate(AbstractList toList) {
		contentManager = getContentManager();
		if (contentManager!=null) {
			if (toList!=null) {
				try {
					contentManager.duplicate(toList);
				} catch (ArcadException e) {
					logError(MODULE_NAME,e);
				}
			} else 
				logError(MODULE_NAME,"Result List must not be null");			
		}
	}

	public void load(boolean retrieveProcessInfo){
		load(retrieveProcessInfo,false);
	}	
	
	public void load(boolean retrieveProcessInfo,boolean metadataOnly){
		contentManager = getContentManager();
		if (contentManager!=null) {
			try {
				contentManager.load(retrieveProcessInfo,metadataOnly);
			} catch (ArcadException e) {				
				logError(MODULE_NAME,e.getCause());
			}
		}		
	}	
	
	public int count(String query) {
		contentManager = getContentManager();
		if (contentManager!=null) {
			try {
				return contentManager.count(query);
			} catch (ArcadException e) {
				logError(MODULE_NAME,e);
			}
		}	
		return -1;
	}		
	
	public int merge(AbstractList opList, AbstractList resList, 
			         boolean checkIfExists, boolean replaceIfExists) {
		contentManager = getContentManager();
		if (contentManager!=null) {
			if (opList == null)
				logError(MODULE_NAME,"Operand List must not be null");	
			else if (resList == null)
				logError(MODULE_NAME,"Result List must not be null");
			else {
				try {
					return contentManager.merge(opList,resList,checkIfExists,replaceIfExists);
				} catch (ArcadException e) {
					logError(MODULE_NAME,e);
				}
			}
		}
		return -1;
	}

	public int substract(AbstractList opList, 
			             AbstractList resList, 
			             boolean checkIfExists, boolean replaceIfExists) {
		contentManager = getContentManager();
		if (contentManager!=null) {
			if (opList == null)
				logError(MODULE_NAME,"Operand List must not be null");	
			else if (resList == null)
				logError(MODULE_NAME,"Result List must not be null");
			else {
				try {
					return contentManager.substract(opList,resList,checkIfExists,replaceIfExists);
				} catch (ArcadException e) {
					logError(MODULE_NAME,e);
				}
			}
		}
		return -1;
	}

	public int intersect(AbstractList opList,AbstractList resList,
			         boolean checkIfExists, boolean replaceIfExists,Hashtable<String,String> extendedQuery) {
		contentManager = getContentManager();
		if (contentManager!=null) {
			if (opList == null)
				logError(MODULE_NAME,"Operand List must not be null");	
			else if (resList == null)
				logError(MODULE_NAME,"Result List must not be null");
			else {			
				try {
					return contentManager.intersect(opList,resList,checkIfExists,
							                        replaceIfExists,extendedQuery);
				} catch (ArcadException e) {
					logError(MODULE_NAME,e);
				}
			}
		}
		return -1;
	}		
	
	
	public int intersect(AbstractList opList,AbstractList resList,
			         boolean checkIfExists, boolean replaceIfExists) {
		return this.intersect(opList,resList,checkIfExists,replaceIfExists,null);
	}		

	public void compare(AbstractList opList,
	           AbstractList addedList,boolean adCheckIfExists, boolean adReplaceIfExists,
	           AbstractList commonList,boolean cmCheckIfExists, boolean cmReplaceIfExists,
	           AbstractList deletedList,boolean deCheckIfExists, boolean deReplaceIfExists){
		contentManager = getContentManager();
		if (contentManager!=null) {
			if (opList == null)
				logError(MODULE_NAME,"Operand List must not be null");	
			else {			
				try {
					contentManager.compare(opList,
							               addedList,adCheckIfExists,adReplaceIfExists,
							               commonList,cmCheckIfExists,cmReplaceIfExists,
							               deletedList,deCheckIfExists,deReplaceIfExists);
				} catch (ArcadException e) {
					logError(MODULE_NAME,e);
				}
			}
		}
	}	

	
	public AbstractList cloneList(){		
		AbstractList l =createCloneList();
		l.setMetadatas(getMetadatas().duplicate());
		l.initStoreItem();
		return l;
	}	
	
	/**
	 * Cette méthode permet de définir le StoreManager à utiliser
	 * par la liste pour enregistrer les données en provenance
	 * d'un objet filler.
	 * @return AbstractStoreManager : StoreManager à utiliser
	 */
	public abstract AbstractStoreManager createStoreManager();	
	/**
	 * Cette méthode permet de définir le StoreManager à utiliser
	 * par la liste pour enregistrer les données en provenance
	 * d'un objet filler.
	 * @return AbstractStoreManager : StoreManager à utiliser
	 */
	public abstract AbstractContentManager createContentManager();		
	
	/**
	 * Cette méthode permet de définir le StoreManager à utiliser
	 * par la liste pour enregistrer les données en provenance
	 * d'un objet filler.
	 * @return AbstractStoreManager : StoreManager à utiliser
	 */
	public abstract AbstractStoreItemManager createStoreItemManager(AbstractList list);		
	

	public abstract AbstractList createCloneList();

	/**
	 * Renvoit 
	 * @return the storeItemInitialized boolean : 
	 */
	public boolean isStoreItemInitialized() {
		return storeItemInitialized;
	}

	/**
	 * @param storeItemInitialized the storeItemInitialized to set
	 */
	public void setStoreItemInitialized(boolean storeItemInitialized) {
		this.storeItemInitialized = storeItemInitialized;
	}
}

