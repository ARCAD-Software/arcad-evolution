/*
 * Created on Jan 23, 2006
 *
 */
package com.arcadsoftware.aev.core.ui.tools;

import org.eclipse.core.runtime.Preferences;
import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;

import com.arcadsoftware.aev.core.messages.MessageManager;
import com.arcadsoftware.aev.core.ui.EvolutionCoreUIPlugin;

/**
 * @author administrateur
 * 
 * TODO: Delete class entirely if not used?
 */
@Deprecated
public class CorePreferenceInitializer extends AbstractPreferenceInitializer {

	/**
	 * 
	 */
	public CorePreferenceInitializer() {
		super();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.core.runtime.preferences.AbstractPreferenceInitializer#
	 * initializeDefaultPreferences()
	 */
	@Override
	public void initializeDefaultPreferences() {
		Preferences store = EvolutionCoreUIPlugin.getDefault().getPluginPreferences();

		EvolutionCoreUIPlugin.getDefault().getPluginPreferences().setDefault(EvolutionCoreUIPlugin.PREF_MESSAGES_LEVEL,
				MessageManager.LEVEL_PRODUCTION);

		// /////////////////////////////////////////////////////////////////////////////////
		// ////// Modifier le niveau des messages de l'application avant de
		// passer ////////
		// ////// en Beta-Test et en Production. ////////
		EvolutionCoreUIPlugin.getDefault().setMessagesLevel(MessageManager.LEVEL_PRODUCTION);
		// /////////////////////////////////////////////////////////////////////////////////
	}

}
