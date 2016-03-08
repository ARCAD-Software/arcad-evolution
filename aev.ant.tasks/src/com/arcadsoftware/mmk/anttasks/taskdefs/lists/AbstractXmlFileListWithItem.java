package com.arcadsoftware.mmk.anttasks.taskdefs.lists;

import java.util.Vector;

import com.arcadsoftware.mmk.anttasks.taskdefs.lists.impl.item.Item;

public abstract class AbstractXmlFileListWithItem 
extends AbstractXmlFileListFromListTask {

	protected Vector<Item> items = new Vector<Item>(); 
	
	public Item createItem(){
		Item item = new Item();
		items.add(item);
        return item;
	}  

}
