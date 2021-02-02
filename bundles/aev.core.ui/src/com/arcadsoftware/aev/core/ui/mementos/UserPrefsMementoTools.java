
package com.arcadsoftware.aev.core.ui.mementos;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.eclipse.ui.IMemento;

import com.arcadsoftware.aev.core.messages.MessageDetail;
import com.arcadsoftware.aev.core.messages.MessageManager;
import com.arcadsoftware.aev.core.tools.StringTools;
import com.arcadsoftware.aev.core.ui.EvolutionCoreUIPlugin;

public class UserPrefsMementoTools extends RootAndUserMementoTools {

	public static final String DIRECTORYNAME = File.separator + "defs"; //$NON-NLS-1$
	public static final String FILENAME = DIRECTORYNAME + File.separator + "userPrefs.xml"; //$NON-NLS-1$
	private static UserPrefsMementoTools instance;

	public static UserPrefsMementoTools getInstance() {
		if (instance == null) {
			instance = new UserPrefsMementoTools();
			instance.load();
		}
		return instance;
	}

	private String filename = null;

	private String filterElementId = StringTools.EMPTY;

	private UserPrefsMementoTools() {
		super();
	}

	/*
	 * (non-Javadoc)
	 * @see com.arcadsoftware.aev.core.ui.mementos.RootAndUserMementoTools#getFileName ()
	 */
	@Override
	protected String getFileName() {
		if (filename == null) {
			filename = EvolutionCoreUIPlugin.getDefault().getStateLocation().toString()
					+ FILENAME;
		}
		final File directory = new File(EvolutionCoreUIPlugin.getDefault().getStateLocation().toString()
				+ DIRECTORYNAME);
		if (!directory.exists()) {
			directory.mkdir();
		}
		final File file = new File(filename);
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (final IOException e) {
				MessageManager.addException(e, MessageManager.LEVEL_PRODUCTION).addDetail(MessageDetail.ERROR,
						"File : " + filename);//$NON-NLS-1$
			}
		}
		return filename;
	}

	/**
	 * @return filterElementId.
	 */
	public String getFilterElementId() {
		return filterElementId;
	}

	public UserPrefsSettings getPrefsSetting(final String identifier) {
		final UserPrefsSettings filter = new UserPrefsSettings("*ALL", "*ALL", identifier); //$NON-NLS-1$//$NON-NLS-2$
		return (UserPrefsSettings) getCurrentSettings(filter);
	}

	/*
	 * (non-Javadoc)
	 * @see com.arcadsoftware.aev.core.ui.mementos.RootAndUserMementoTools#keep(com
	 * .arcadsoftware.aev.core.ui.mementos.ArcadSettings)
	 */
	@Override
	protected boolean keep(final ArcadSettings s) {
		return true;
	}

	/*
	 * (non-Javadoc)
	 * @see com.arcadsoftware.aev.core.ui.mementos.RootAndUserMementoTools#keyEquals
	 * (com.arcadsoftware.aev.core.ui.mementos.ArcadSettings, com.arcadsoftware.aev.core.ui.mementos.ArcadSettings)
	 */
	@Override
	public boolean keyEquals(final ArcadSettings ref, final ArcadSettings toCompare) {
		if (ref instanceof UserPrefsSettings && toCompare instanceof UserPrefsSettings) {
			final UserPrefsSettings r = (UserPrefsSettings) ref;
			final UserPrefsSettings c = (UserPrefsSettings) toCompare;
			return r.getServerName().equalsIgnoreCase(c.getServerName())
					&& r.getUserName().equalsIgnoreCase(c.getUserName())
					&& r.getElementId().equalsIgnoreCase(c.getElementId());
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * @see com.arcadsoftware.aev.core.ui.mementos.RootAndUserMementoTools#readKeys (java.lang.String, java.lang.String,
	 * org.eclipse.ui.IMemento)
	 */
	@Override
	public ArcadSettings readKeys(final String serverName, final String userName, final IMemento user) {

		final IMemento mementoPref = user.getChild("userPrefs");//$NON-NLS-1$
		final String prefId = mementoPref.getString("id");//$NON-NLS-1$
		final IMemento[] prefs = mementoPref.getChildren("pref");//$NON-NLS-1$

		final Map<String, String> settings = new HashMap<>();
		for (final IMemento pref : prefs) {
			settings.put(pref.getString("key"), pref.getString("value"));
		}
		return new UserPrefsSettings(serverName, userName, prefId, settings);
	}

	/*
	 * (non-Javadoc)
	 * @see com.arcadsoftware.aev.core.ui.mementos.RootAndUserMementoTools#saveKeys (org.eclipse.ui.IMemento,
	 * com.arcadsoftware.aev.core.ui.mementos.ArcadSettings)
	 */
	@Override
	public void saveKeys(final IMemento user, final ArcadSettings s) {
		if (s instanceof UserPrefsSettings) {
			final UserPrefsSettings settings = (UserPrefsSettings) s;
			final IMemento mementoPref = user.createChild("userPrefs");//$NON-NLS-1$
			mementoPref.putString("id", settings.getElementId());//$NON-NLS-1$

			final Set<String> keys = settings.getPreferences().keySet();
			for (final String key : keys) {
				final IMemento item = mementoPref.createChild("pref");//$NON-NLS-1$
				item.putString("key", key);//$NON-NLS-1$
				item.putString("value", settings.getPreferences().get(key));//$NON-NLS-1$
			}
		}
	}

	/**
	 * @param filterElementId
	 *            filterElementId to define
	 */
	public void setFilterElementId(final String filterElementId) {
		this.filterElementId = filterElementId;
	}
}
