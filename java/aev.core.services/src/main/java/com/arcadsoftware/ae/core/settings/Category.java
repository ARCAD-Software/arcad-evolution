package com.arcadsoftware.ae.core.settings;

import java.util.ArrayList;

public class Category  {

	String label;
	ArrayList<Form> list = new ArrayList<Form>();
	
	
	public String getLabel() {
		return label;
	}
	
	public void setLabel(String label) {
		this.label = label;
	}
	
	public ArrayList<Form> getList() {
		return list;
	}
	
	public void setList(ArrayList<Form> list) {
		this.list = list;
	}
	

	

}
