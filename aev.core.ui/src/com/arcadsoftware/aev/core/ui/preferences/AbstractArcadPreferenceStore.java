/*
 * Créé le 25 avr. 2007
 *
 */
package com.arcadsoftware.aev.core.ui.preferences;

import org.eclipse.jface.preference.IPreferenceStore;

import com.arcadsoftware.aev.core.tools.StringTools;

/**
 * @author MD
 * 
 */
public abstract class AbstractArcadPreferenceStore {
	IPreferenceStore store;

	public IPreferenceStore getStore() {
		return store;
	}

	public void setStore(IPreferenceStore store) {
		this.store = store;
		if (store != null) {
			setDefault();
		}
	}

	public void setValue(String preferenceId, String value) {
		if (!preferenceId.equals(StringTools.EMPTY)) {
			if (store != null)
				store.setValue(preferenceId, value);
		}
	}

	public void setValue(String preferenceId, int value) {
		if (!preferenceId.equals(StringTools.EMPTY)) {
			if (store != null)
				store.setValue(preferenceId, value);
		}
	}

	public void setValue(String preferenceId, boolean value) {
		// IPreferenceStore store =
		// ArcadCorePlugin.getDefault().getPreferenceStore();
		if (!preferenceId.equals(StringTools.EMPTY)) {
			if (store != null)
				store.setValue(preferenceId, value);
		}
	}

	public boolean getBoolean(String preferenceId) {
		if (getStore() != null)
			return store.getBoolean(preferenceId);
		return false;
	}

	public String getString(String preferenceId) {
		if (store != null)
			return store.getString(preferenceId);
		return StringTools.EMPTY;
	}

	public int getInt(String preferenceId) {
		if (store != null)
			return store.getInt(preferenceId);
		return -1;
	}

	public abstract void setDefault();

}
