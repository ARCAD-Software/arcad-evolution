package com.arcadsoftware.mmk.anttasks.taskdefs.lists.impl.generic;

import java.util.Vector;

import com.arcadsoftware.mmk.anttasks.taskdefs.lists.AbstractXmlFileListWithItem;
import com.arcadsoftware.mmk.anttasks.taskdefs.lists.impl.item.Item;
import com.arcadsoftware.mmk.anttasks.taskdefs.lists.impl.item.ItemValue;
import com.arcadsoftware.mmk.lists.impl.lists.GenericList;
import com.arcadsoftware.mmk.lists.metadata.ListColumnDef;
import com.arcadsoftware.mmk.lists.metadata.StoreItem;

public class ListAdditionTask extends AbstractXmlFileListWithItem {

	@Override
	public int processExecutionWithCount() {
		// Traitement de l'ajout à partir d'une liste
		int count = 0;
		if (fromListFileName != null && !fromListFileName.equals("")) {
			// Déclaration de la liste
			final GenericList fromlist = (GenericList) list.cloneList();
			fromlist.setXmlFileName(fromListFileName);
			count = list.addItems(fromlist, checkIfExists, replaceIfExists);
		}
		// Ajout des données placées par l'utilisateur
		list.load(false, true);
		// list.getCashManager().setFlushImmediat(false);
		int count2 = 0;
		for (final Item c : items) {
			final Vector<ItemValue> v = c.getValues();
			final StoreItem storeItem = new StoreItem();
			storeItem.setMetadatas(list.getMetadatas());
			boolean toadd = false;
			for (final ItemValue val : v) {
				if (list.getMetadatas() != null) {
					final ListColumnDef cd = list.getMetadatas().getColumnFromId(val.getId());
					if (cd != null) {
						storeItem.setValue(cd.getId(), val.getValue());
						toadd = true;
					}
				}
			}
			if (toadd) {
				final int i = list.addItems(storeItem, checkIfExists, replaceIfExists);
				count2 += i;
			}
		}
		// list.getCashManager().flush();
		return count + count2;
	}

}
