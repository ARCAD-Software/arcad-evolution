package com.arcadsoftware.mmk.anttasks.taskdefs.lists.impl.item;

import java.util.Vector;



public class Item {
	Vector<ItemValue> values = new Vector<ItemValue>(); 
	
	public Item(){
		super();
	}

	public ItemValue createItemValue(){
		ItemValue itemValue = new ItemValue();
		values.add(itemValue);
        return itemValue;
	}

	/**
	 * Renvoit 
	 * @return the values Vector<ItemValue> : 
	 */
	public Vector<ItemValue> getValues() {
		return values;
	}   
}
