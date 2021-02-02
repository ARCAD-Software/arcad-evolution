package com.arcadsoftware.ae.core.settings;

import java.util.ArrayList;

public class Setting {
	private final ArrayList<Category> categories;

	public Setting() {
		categories = new ArrayList<>();
	}

	public void addCategory(final Category c) {
		categories.add(c);
	}

	public ArrayList<Category> getCategories() {
		return categories;
	}

	public Category getCategoryByName(final String category) {
		for (final Category c : categories) {
			if (c.getLabel().equalsIgnoreCase(category)) {
				return c;
			}
		}
		return null;
	}

}
