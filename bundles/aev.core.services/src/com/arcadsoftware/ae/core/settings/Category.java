package com.arcadsoftware.ae.core.settings;

import java.util.ArrayList;

public class Category {

	String label;
	ArrayList<Form> list = new ArrayList<>();

	public String getLabel() {
		return label;
	}

	public ArrayList<Form> getList() {
		return list;
	}

	public void setLabel(final String label) {
		this.label = label;
	}

	public void setList(final ArrayList<Form> list) {
		this.list = list;
	}

}
