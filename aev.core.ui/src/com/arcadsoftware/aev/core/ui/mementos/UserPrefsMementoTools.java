
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

	private String filterElementId = StringTools.EMPTY;
	private static UserPrefsMementoTools instance;
	private String filename = null;	

	public static final String DIRECTORYNAME = File.separator + "defs"; //$NON-NLS-1$ 
	public static final String FILENAME = DIRECTORYNAME + File.separator + "userPrefs.xml"; //$NON-NLS-1$

	private UserPrefsMementoTools() {
		super();
	}

	public static UserPrefsMementoTools getInstance() {
		if (instance == null) {
			instance = new UserPrefsMementoTools();
			instance.load();
		}
		return instance;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.arcadsoftware.aev.core.ui.mementos.RootAndUserMementoTools#getFileName
	 * ()
	 */
	@Override
	protected String getFileName() {
		if (this.filename == null)
			filename = EvolutionCoreUIPlugin.getDefault().getStateLocation().toString()
					+ FILENAME;
		File directory = new File(EvolutionCoreUIPlugin.getDefault().getStateLocation().toString()
				+ DIRECTORYNAME);
		if (!directory.exists())
			directory.mkdir();
		File file = new File(filename);
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				MessageManager.addException(e, MessageManager.LEVEL_PRODUCTION).addDetail(MessageDetail.ERROR,
						"File : " + this.filename);//$NON-NLS-1$
			}
		}
		return this.filename;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.arcadsoftware.aev.core.ui.mementos.RootAndUserMementoTools#readKeys
	 * (java.lang.String, java.lang.String, org.eclipse.ui.IMemento)
	 */
	@Override
	public ArcadSettings readKeys(String serverName, String userName, IMemento user) {
		
		IMemento mementoPref = user.getChild("userPrefs");//$NON-NLS-1$
		String prefId = mementoPref.getString("id");//$NON-NLS-1$
		IMemento[] prefs = mementoPref.getChildren("pref");//$NON-NLS-1$
		
		Map<String, String> settings = new HashMap<String, String>();
		for (int i = 0; i < prefs.length; i++) {
			settings.put(prefs[i].getString("key"), prefs[i].getString("value"));
		}
		return new UserPrefsSettings(serverName, userName, prefId, settings);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.arcadsoftware.aev.core.ui.mementos.RootAndUserMementoTools#saveKeys
	 * (org.eclipse.ui.IMemento,
	 * com.arcadsoftware.aev.core.ui.mementos.ArcadSettings)
	 */
	@Override
	public void saveKeys(IMemento user, ArcadSettings s) {
		if (s instanceof UserPrefsSettings) {
			UserPrefsSettings settings = (UserPrefsSettings) s;
			IMemento mementoPref = user.createChild("userPrefs");//$NON-NLS-1$
			mementoPref.putString("id", settings.getElementId());//$NON-NLS-1$
			
			Set<String> keys = settings.getPreferences().keySet();
			for (String key : keys) {
				IMemento item = mementoPref.createChild("pref");//$NON-NLS-1$
				item.putString("key", key);//$NON-NLS-1$
				item.putString("value", settings.getPreferences().get(key));//$NON-NLS-1$
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.arcadsoftware.aev.core.ui.mementos.RootAndUserMementoTools#keyEquals
	 * (com.arcadsoftware.aev.core.ui.mementos.ArcadSettings,
	 * com.arcadsoftware.aev.core.ui.mementos.ArcadSettings)
	 */
	@Override
	public boolean keyEquals(ArcadSettings ref, ArcadSettings toCompare) {
		if ((ref instanceof UserPrefsSettings) && (toCompare instanceof UserPrefsSettings)) {
			UserPrefsSettings r = (UserPrefsSettings) ref;
			UserPrefsSettings c = (UserPrefsSettings) toCompare;
			return r.getServerName().equalsIgnoreCase(c.getServerName())
					&& r.getUserName().equalsIgnoreCase(c.getUserName())
					&& r.getElementId().equalsIgnoreCase(c.getElementId());
		}
		return false;
	}

	public UserPrefsSettings getPrefsSetting(String identifier) {
		UserPrefsSettings filter = new UserPrefsSettings("*ALL", "*ALL", identifier); //$NON-NLS-1$//$NON-NLS-2$
		return (UserPrefsSettings) getCurrentSettings(filter);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.arcadsoftware.aev.core.ui.mementos.RootAndUserMementoTools#keep(com
	 * .arcadsoftware.aev.core.ui.mementos.ArcadSettings)
	 */
	@Override
	protected boolean keep(ArcadSettings s) {
		return true;
	}

	/**
	 * @return filterElementId.
	 */
	public String getFilterElementId() {
		return filterElementId;
	}

	/**
	 * @param filterElementId
	 *            filterElementId to define
	 */
	public void setFilterElementId(String filterElementId) {
		this.filterElementId = filterElementId;
	}
}
