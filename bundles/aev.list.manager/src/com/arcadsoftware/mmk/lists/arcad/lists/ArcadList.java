/*
 * Cr‚‚ le 16 nov. 07
 *
 * TODO Pour changer le modŠle de ce fichier g‚n‚r‚, allez … :
 * Fenˆtre - Pr‚f‚rences - Java - Style de code - ModŠles de code
 */
package com.arcadsoftware.mmk.lists.arcad.lists;

import com.arcadsoftware.mmk.lists.AbstractList;
import com.arcadsoftware.mmk.lists.AbstractXmlList;
import com.arcadsoftware.mmk.lists.arcad.storeitem.ArcadStoreItemManager;
import com.arcadsoftware.mmk.lists.metadata.AbstractStoreItemManager;

public class ArcadList extends AbstractXmlList {

	@Override
	public AbstractList createCloneList() {
		return new ArcadList();
	}

	@Override
	public AbstractStoreItemManager createStoreItemManager(AbstractList list) {
		return new ArcadStoreItemManager(list);
	}

}
