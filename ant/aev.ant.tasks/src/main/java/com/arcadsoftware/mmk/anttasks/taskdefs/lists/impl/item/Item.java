package com.arcadsoftware.mmk.anttasks.taskdefs.lists.impl.item;

import java.util.Vector;

public class Item {
	Vector<ItemValue> values = new Vector<>();

	public Item() {
		super();
	}

	public ItemValue createItemValue() {
		final ItemValue itemValue = new ItemValue();
		values.add(itemValue);
		return itemValue;
	}

	/**
	 * Renvoit
	 * 
	 * @return the values Vector<ItemValue> :
	 */
	public Vector<ItemValue> getValues() {
		return values;
	}
}
