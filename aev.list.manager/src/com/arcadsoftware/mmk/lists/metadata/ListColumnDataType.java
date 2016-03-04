package com.arcadsoftware.mmk.lists.metadata;

public enum ListColumnDataType {
	STRING("string"),
	INTEGER("integer"),		
	FLOAT("float");
	
	String value;
	private ListColumnDataType(String value) {
		this.value = value;
	}
	
	public String getValue() {
		return value;
	}
}
