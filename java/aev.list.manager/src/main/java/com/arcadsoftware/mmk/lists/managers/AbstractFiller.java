package com.arcadsoftware.mmk.lists.managers;

import com.arcadsoftware.mmk.lists.AbstractList;
import com.arcadsoftware.mmk.lists.metadata.ListMetaDatas;
import com.arcadsoftware.mmk.lists.metadata.StoreItem;


/**
 * Classe abstraite de gestion du remplissage de liste.<br>
 * <div>
 * <div class="intro">Généralités</div>
 * Cette classe permet de définir une méthode de remplissage d'une 
 * liste par implémentation la méthode {@link #fill() fill()}.<br>
 * L'ajout d'un élément dans la liste s'effctue en deux temps : <br>
 * - L'affectation des valeurs à l'objet <code>storeItem</code> 
 * de la liste,<br>
 * - L'appel à la méthode <code>addItem</code>.<br>
 * Pour affecter des valeurs à l'objet <code>storeItem</code>, vous
 * pouvez utiliser deux méthodes : <br>
 * - L'utilisation de la méthode {@link AbstractList#toStoreItem(Object)() toStoreItem()}
 * <code>toStoreItem(Object element)</code> qui permet de transférer 
 * les données d'un objet quelconque vers un objet de type <code>storeItem</code>,
 * - L'affectation directe via {@link AbstractList#getStoreItem() AbstractList#getStoreItem()}
 * 
 * @author MD
 *
 */
public abstract class AbstractFiller  extends AbstractLoggedObject{
	
	private boolean fillMode = true;
	private ListMetaDatas metadatas;
	private AbstractList list;
	boolean checkIfExists;
	boolean replaceIfExists;
	
	
	
	public AbstractFiller() {
		super();
	}		
	
	public AbstractFiller(AbstractList list) {
		setList(list);
	}

	/**
	 * Renvoit la liste à remplir par le filler.
	 * @return the list AbstractList : 
	 */
	public AbstractList getList() {
		return list;
	}

	/**
	 * @param list the list to set
	 */
	public void setList(AbstractList list) {
		this.list = list;
		setLogger(list.getLogger());
		if (list.getStoreItem().getMetadatas()==null)
			list.initStoreItem();		
	}
	
	public void toogleToAddMode( boolean checkIfExists, boolean replaceIfExists) {
		fillMode = false;
		this.checkIfExists = checkIfExists;
		this.replaceIfExists = replaceIfExists;
	}
	public void toogleToFillMode() {
		fillMode = true;
	}
	
	
	public int saveItem(StoreItem item) {
		int count = 0;
		if (getList()!=null) {
			
			if (fillMode) {
				getList().setStoreItem(item);
				getList().saveItem();
				count = 1;
			} else {
				count = getList().addItems(item,checkIfExists,replaceIfExists);
				getList().getContentManager().getCashManager().setFlushImmediat(true);			
			}
		}
		return count;
	}
	
	public int populate() {
		if (getList()!=null) {
			if (!fillMode) {
				getList().getContentManager().getCashManager().setFlushImmediat(false);			
			}
			int result = fill();
			if (!fillMode) {
				getList().getContentManager().getCashManager().setFlushImmediat(true);			
				getList().getContentManager().getCashManager().flushRequest();
			}		
			return result;
		}
		return -1;
	}
	
	
	public abstract int fill();	
	
	
}
