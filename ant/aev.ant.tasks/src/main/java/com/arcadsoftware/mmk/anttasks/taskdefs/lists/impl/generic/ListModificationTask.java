package com.arcadsoftware.mmk.anttasks.taskdefs.lists.impl.generic;

import java.util.ArrayList;
import java.util.Iterator;
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
		//Suppression à partir d'une liste
		int count1 = 0;
		if ((fromListFileName!=null) && !fromListFileName.equals("")){
			//Déclaration de la liste        
			GenericList fromlist = (GenericList)list.cloneList();
			fromlist.setXmlFileName(fromListFileName);
			count1 = list.updateItems(fromlist);
		}
		//mise à jour des données placées par l'utilisateur
		int count2 =0;
		ArrayList<StoreItem> l = new ArrayList<StoreItem>();
        for (Iterator<Item> it=items.iterator(); it.hasNext(); ) {
        	Item c = it.next();
        	Vector<ItemValue> v = c.getValues();
            for (Iterator<ItemValue> it2=v.iterator(); it2.hasNext(); ) {            	
            	ItemValue val = it2.next();
            	if (list.getMetadatas()!=null) {
	            	ListColumnDef cd = list.getMetadatas().getColumnFromId(val.getId());
	            	if (cd!=null) {
	                	StoreItem storeItem = new StoreItem();
	                	storeItem.setMetadatas(list.getMetadatas());	            		
	            		storeItem.setValue(cd.getId(),val.getValue());
	            		l.add(storeItem);
	            	}   
            	}
            } 
        }			
        StoreItem[] items = new StoreItem[l.size()];
        for (int i = 0;i<l.size();i++) {
        	items[i] = l.get(i);
        }
        count2 = list.updateItems(items);
        

		
        
		return count1+count2;
	}



	
}
