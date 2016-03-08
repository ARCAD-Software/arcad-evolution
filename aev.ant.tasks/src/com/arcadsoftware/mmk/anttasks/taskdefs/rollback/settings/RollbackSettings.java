package com.arcadsoftware.mmk.anttasks.taskdefs.rollback.settings;






public class RollbackSettings {
	private static RollbackSettings instance = null;			
    /**
     * 
     */
    private RollbackSettings() {
        super();
    }	
	private String backupEnvironment = null;

	public static RollbackSettings getInstance(){
		if (instance==null) {
			instance = new RollbackSettings();
		}
		return instance;
	}	
	
    public static void setInstance(RollbackSettings instance) {
    	RollbackSettings.instance = instance;
    }		
	
	/**
	 * Renvoit 
	 * @return the backupEnvironment String : 
	 */
	public String getBackupEnvironment() {
		return backupEnvironment;
	}

	/**
	 * @param backupEnvironment the backupEnvironment to set
	 */
	public void setBackupEnvironment(String backupEnvironment) {
		this.backupEnvironment = backupEnvironment;
	}
	
}
