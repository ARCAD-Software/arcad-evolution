package com.arcadsoftware.mmk.anttasks.taskdefs.rollback.settings;

public class RollbackSettings {
	private static RollbackSettings instance = null;

	public static RollbackSettings getInstance() {
		if (instance == null) {
			instance = new RollbackSettings();
		}
		return instance;
	}

	public static void setInstance(final RollbackSettings instance) {
		RollbackSettings.instance = instance;
	}

	private String backupEnvironment = null;

	/**
	 * 
	 */
	private RollbackSettings() {
		super();
	}

	/**
	 * Renvoit
	 * 
	 * @return the backupEnvironment String :
	 */
	public String getBackupEnvironment() {
		return backupEnvironment;
	}

	/**
	 * @param backupEnvironment
	 *            the backupEnvironment to set
	 */
	public void setBackupEnvironment(final String backupEnvironment) {
		this.backupEnvironment = backupEnvironment;
	}

}
