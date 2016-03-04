package com.arcadsoftware.mmk.lists;



public class ListSettings {

	private boolean caseSensitive = false;	
	
	private static ListSettings instance = null;			
    /**
     * 
     */
    private ListSettings() {
        super();
    }	


	public static ListSettings getInstance(){
		if (instance==null) {
			instance = new ListSettings();
		}
		return instance;
	}	
	
    public static void setInstance(ListSettings instance) {
    	ListSettings.instance = instance;
    }		
	


	/**
	 * Renvoit 
	 * @return the caseSensitive boolean : 
	 */
	public boolean isCaseSensitive() {
		return caseSensitive;
	}

	/**
	 * @param caseSensitive the caseSensitive to set
	 */
	public void setCaseSensitive(boolean caseSensitive) {
		this.caseSensitive = caseSensitive;
	}
	
	
	
}
