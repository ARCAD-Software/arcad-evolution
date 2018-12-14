/*
 * Cr‚‚ le 16 nov. 07
 *
 * TODO Pour changer le modŠle de ce fichier g‚n‚r‚, allez … :
 * Fenˆtre - Pr‚f‚rences - Java - Style de code - ModŠles de code
 */
package com.arcadsoftware.mmk.lists.arcad.storeitem;

import com.arcadsoftware.mmk.lists.AbstractList;
import com.arcadsoftware.mmk.lists.metadata.AbstractStoreItemManager;
import com.arcadsoftware.mmk.lists.metadata.ListMetaDatas;
import com.arcadsoftware.mmk.lists.metadata.StoreItem;

public class ArcadStoreItemManager extends AbstractStoreItemManager {
	private static final String ID = "com.arcadsoftware.lists.Arcadlist";
	private static final String VERSION = "1.0.0.0";	
	
	public ArcadStoreItemManager(AbstractList list) {
		super(list);
	}
	@Override
	public void createMetadata(ListMetaDatas metadatas) {
		// TODO Raccord de m‚thode auto-g‚n‚r‚

	}

	@Override
	public StoreItem toStoreItem(Object object) {
		// TODO Raccord de m‚thode auto-g‚n‚r‚
		return null;
	}

}
