package com.arcadsoftware.mmk.lists;

public class ListSettings {

	private static ListSettings instance = null;

	public static ListSettings getInstance() {
		if (instance == null) {
			instance = new ListSettings();
		}
		return instance;
	}

	public static void setInstance(final ListSettings instance) {
		ListSettings.instance = instance;
	}

	private boolean caseSensitive = false;

	/**
	 * 
	 */
	private ListSettings() {
		super();
	}

	/**
	 * Renvoit
	 * 
	 * @return the caseSensitive boolean :
	 */
	public boolean isCaseSensitive() {
		return caseSensitive;
	}

	/**
	 * @param caseSensitive
	 *            the caseSensitive to set
	 */
	public void setCaseSensitive(final boolean caseSensitive) {
		this.caseSensitive = caseSensitive;
	}

}
