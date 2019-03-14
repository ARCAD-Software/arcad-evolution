package com.arcadsoftware.mmk.lists.ext;

import java.io.File;

import com.arcadsoftware.mmk.lists.metadata.AbstractStoreItemManager;
import com.arcadsoftware.mmk.lists.metadata.ListColumnDataType;
import com.arcadsoftware.mmk.lists.metadata.ListMetaDatas;
import com.arcadsoftware.mmk.lists.metadata.StoreItem;

public class TestStoreItemManager extends AbstractStoreItemManager {

	@Override
	public void createMetadata(ListMetaDatas metadatas) {
		metadatas.setId("");
		metadatas.setVersion("");		
		metadatas.addColumnDef("value","arcad.list.text",ListColumnDataType.STRING,true);		
	}

	@Override
	public StoreItem toStoreItem(Object object) {
		if (object instanceof File) {
			StoreItem item = new StoreItem(list.getMetadatas()); 
			item.setUserValue(
				new String[]{((File)object).getAbsolutePath()}
			);	
			return item;
		}	
		return null;
	}

}
