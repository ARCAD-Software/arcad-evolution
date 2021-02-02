package com.arcadsoftware.mmk.lists.metadata;

public enum ListColumnDataType {
	FLOAT("float"), INTEGER("integer"), STRING("string");

	String value;

	private ListColumnDataType(final String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
}
