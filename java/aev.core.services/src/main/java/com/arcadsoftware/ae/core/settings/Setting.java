package com.arcadsoftware.ae.core.settings;

import java.util.ArrayList;

public class Setting {
	private ArrayList<Category> categories;
	
	public Setting(){
		categories = new ArrayList<Category>();
	}
	
	public void addCategory(Category c){
		categories.add(c);		
	}
	
	public Category getCategoryByName(String category){
		for(Category c:categories) {
			if (c.getLabel().equalsIgnoreCase(category)) {
				return c;
			}
		}
		return null;
	}
	
	public ArrayList<Category> getCategories(){
		return categories;
	}
	
}
