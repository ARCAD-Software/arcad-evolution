package com.arcadsoftware.aev.core.ui.mementos;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import org.eclipse.ui.IMemento;
import org.eclipse.ui.XMLMemento;

import com.arcadsoftware.aev.core.messages.MessageDetail;
import com.arcadsoftware.aev.core.messages.MessageManager;

/**
 * @author MD
 * 
 */
public abstract class RootAndUserMementoTools {
	protected ArrayList<ArcadSettings> list = new ArrayList<>();

	protected abstract String getFileName();

	public abstract ArcadSettings readKeys(String serverName, String userName, IMemento user);

	public abstract void saveKeys(IMemento user, ArcadSettings s);

	public abstract boolean keyEquals(ArcadSettings ref, ArcadSettings toCompare);

	protected boolean keep(ArcadSettings s) {
		return true;
	}

	public RootAndUserMementoTools() {
		super();
	}

	public ArcadSettings getCurrentSettings(ArcadSettings ref) {
		for (int i = 0; i < list.size(); i++) {
			ArcadSettings s = (ArcadSettings) list.get(i);
			if ((s.getServerName().equalsIgnoreCase(ref.getServerName()))
					&& (s.getUserName().equalsIgnoreCase(ref.getUserName())) && keyEquals(ref, s)) {
				return s;
			}
		}
		return null;
	}

	public ArrayList<ArcadSettings> getSettings() {
		ArrayList<ArcadSettings> l = new ArrayList<>();
		for (int i = 0; i < list.size(); i++) {
			ArcadSettings s = (ArcadSettings) list.get(i);
			if (keep(s)) {
				l.add(s);
			}
		}
		return l;
	}

	public void setSettings(ArrayList<ArcadSettings> settings) {
		for (int i = list.size() - 1; i >= 0; i--) {
			ArcadSettings s = (ArcadSettings) list.get(i);
			if (keep(s)) {
				list.remove(i);
			}
		}

		for (int i = 0; i < settings.size(); i++) {
			ArcadSettings s = (ArcadSettings) settings.get(i);
			if (keep(s)) {
				list.add(s);
			}
		}
		save();
	}

	protected void doBeforeAdding(ArcadSettings settings) {
		// Do nothing
	}

	public void setCurrentSettings(ArcadSettings settings) {
		Iterator<ArcadSettings> settingsList = list.iterator();
		while(settingsList.hasNext()) {
			ArcadSettings s = settingsList.next();
			if ((s.getServerName().equalsIgnoreCase(settings.getServerName()))
					&& (s.getUserName().equalsIgnoreCase(settings.getUserName())) && keyEquals(settings, s)) {
				list.remove(s);
				break;
			}
		}
		doBeforeAdding(settings);
		list.add(settings);
		save();
	}

	public void add(ArcadSettings settings) {
		setCurrentSettings(settings);
	}

	public void removeCurrentSettings(ArcadSettings settings) {
		for (int i = 0; i < list.size(); i++) {
			ArcadSettings s = (ArcadSettings) list.get(i);
			if ((s.getServerName().equalsIgnoreCase(settings.getServerName()))
					&& (s.getUserName().equalsIgnoreCase(settings.getUserName())) && keyEquals(settings, s)) {
				list.remove(i);
				break;
			}
		}
		save();
	}

	public void load() {
		File xmlFile = new File(getFileName());
		if(xmlFile.exists() && xmlFile.length() > 0){
			try (FileReader reader = new FileReader(xmlFile)){
				list.clear();
				XMLMemento x = XMLMemento.createReadRoot(reader);
				IMemento[] servers = x.getChildren("server"); //$NON-NLS-1$
				for (int i = 0; i < servers.length; i++) {
					String server = servers[i].getString("name"); //$NON-NLS-1$				
					IMemento[] users = servers[i].getChildren("user"); //$NON-NLS-1$
					for (int j = 0; j < users.length; j++) {
						String user = users[j].getString("name"); //$NON-NLS-1$
						list.add(readKeys(server, user, users[j]));
					}
				}
			}
			catch (Exception e) {
				MessageManager.addException(e, MessageManager.LEVEL_PRODUCTION).addDetail(MessageDetail.ERROR,
						"XMLFilters.loadFilteredElement");//$NON-NLS-1$
			}
		}
	}

	public void save() {
		// Enregistrement du fichier
		XMLMemento x = XMLMemento.createWriteRoot("element"); //$NON-NLS-1$
		for (int i = 0; i < list.size(); i++) {
			ArcadSettings es = (ArcadSettings) list.get(i);
			String serverName = es.getServerName();
			String userName = es.getUserName();
			IMemento server = x.createChild("server"); //$NON-NLS-1$
			server.putString("name", serverName); //$NON-NLS-1$
			IMemento user = server.createChild("user"); //$NON-NLS-1$
			user.putString("name", userName); //$NON-NLS-1$
			saveKeys(user, es);
		}
		try {
			x.save(new FileWriter(getFileName()));
		} catch (IOException e) {
			MessageManager.addException(e, MessageManager.LEVEL_PRODUCTION).addDetail(MessageDetail.ERROR,
					"File : " + getFileName());//$NON-NLS-1$
		}
	}

	public ArrayList<ArcadSettings> getList() {
		return list;
	}
}
