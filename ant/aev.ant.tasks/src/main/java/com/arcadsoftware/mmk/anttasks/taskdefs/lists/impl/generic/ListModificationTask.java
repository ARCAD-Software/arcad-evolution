package com.arcadsoftware.mmk.anttasks.taskdefs.lists.impl.generic;

import java.util.ArrayList;
import java.util.Vector;

import com.arcadsoftware.mmk.anttasks.taskdefs.lists.AbstractXmlFileListWithItem;
import com.arcadsoftware.mmk.anttasks.taskdefs.lists.impl.item.Item;
import com.arcadsoftware.mmk.anttasks.taskdefs.lists.impl.item.ItemValue;
import com.arcadsoftware.mmk.lists.impl.lists.GenericList;
import com.arcadsoftware.mmk.lists.metadata.ListColumnDef;
import com.arcadsoftware.mmk.lists.metadata.StoreItem;

public class ListModificationTask extends AbstractXmlFileListWithItem {

	@Override
	public int processExecutionWithCount() {
		// Suppression à partir d'une liste
		int count1 = 0;
		if (fromListFileName != null && !fromListFileName.equals("")) {
			// Déclaration de la liste
			final GenericList fromlist = (GenericList) list.cloneList();
			fromlist.setXmlFileName(fromListFileName);
			count1 = list.updateItems(fromlist);
		}
		// mise à jour des données placées par l'utilisateur
		int count2 = 0;
		final ArrayList<StoreItem> l = new ArrayList<>();
		for (final Item c : items) {
			final Vector<ItemValue> v = c.getValues();
			for (final ItemValue val : v) {
				if (list.getMetadatas() != null) {
					final ListColumnDef cd = list.getMetadatas().getColumnFromId(val.getId());
					if (cd != null) {
						final StoreItem storeItem = new StoreItem();
						storeItem.setMetadatas(list.getMetadatas());
						storeItem.setValue(cd.getId(), val.getValue());
						l.add(storeItem);
					}
				}
			}
		}
		final StoreItem[] items = new StoreItem[l.size()];
		for (int i = 0; i < l.size(); i++) {
			items[i] = l.get(i);
		}
		count2 = list.updateItems(items);

		return count1 + count2;
	}

}
