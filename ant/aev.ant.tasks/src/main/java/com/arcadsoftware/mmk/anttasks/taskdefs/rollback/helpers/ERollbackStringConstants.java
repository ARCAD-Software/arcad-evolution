package com.arcadsoftware.mmk.anttasks.taskdefs.rollback.helpers;

public enum ERollbackStringConstants {
	// Gestion des codes actions
	RB_ACTIONCODE_COPY("copy"), RB_ACTIONCODE_DELETE("delete"), RB_ACTIONCODE_TRANSACTION("trasaction"),
	// Gestion des propriétés globale
	RB_PROP_DIR("arcad.rollback.dir"), RB_PROP_ID("arcad.rollback.id"),
	// Gestion du nom des fichiers de configuration
	RB_SETTING_FILENAME("rollback-settings.xml"), RB_TAG_ACTION("action"), RB_TAG_CODE("code"), RB_TAG_HEADER(
			"header"), RB_TAG_ROOT("rollback.settings"), RB_TAG_VERSION("version");

	private String value = null;

	private ERollbackStringConstants(final String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

}
