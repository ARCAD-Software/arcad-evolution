/*
 * Créé le 25 avr. 2007
 *
 */
package com.arcadsoftware.aev.core.ui.preferences;

import org.eclipse.jface.preference.IPreferenceStore;

import com.arcadsoftware.aev.core.tools.StringTools;

/**
 * @author MD
 */
public abstract class AbstractArcadPreferenceStore {
	IPreferenceStore store;

	public boolean getBoolean(final String preferenceId) {
		if (getStore() != null) {
			return store.getBoolean(preferenceId);
		}
		return false;
	}

	public int getInt(final String preferenceId) {
		if (store != null) {
			return store.getInt(preferenceId);
		}
		return -1;
	}

	public IPreferenceStore getStore() {
		return store;
	}

	public String getString(final String preferenceId) {
		if (store != null) {
			return store.getString(preferenceId);
		}
		return StringTools.EMPTY;
	}

	public abstract void setDefault();

	public void setStore(final IPreferenceStore store) {
		this.store = store;
		if (store != null) {
			setDefault();
		}
	}

	public void setValue(final String preferenceId, final boolean value) {
		// IPreferenceStore store =
		// ArcadCorePlugin.getDefault().getPreferenceStore();
		if (!preferenceId.equals(StringTools.EMPTY)) {
			if (store != null) {
				store.setValue(preferenceId, value);
			}
		}
	}

	public void setValue(final String preferenceId, final int value) {
		if (!preferenceId.equals(StringTools.EMPTY)) {
			if (store != null) {
				store.setValue(preferenceId, value);
			}
		}
	}

	public void setValue(final String preferenceId, final String value) {
		if (!preferenceId.equals(StringTools.EMPTY)) {
			if (store != null) {
				store.setValue(preferenceId, value);
			}
		}
	}

}
