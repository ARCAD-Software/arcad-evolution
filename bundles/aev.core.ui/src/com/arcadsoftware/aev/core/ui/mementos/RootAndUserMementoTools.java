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
 */
public abstract class RootAndUserMementoTools {
	protected ArrayList<ArcadSettings> list = new ArrayList<>();

	public RootAndUserMementoTools() {
		super();
	}

	public void add(final ArcadSettings settings) {
		setCurrentSettings(settings);
	}

	protected void doBeforeAdding(final ArcadSettings settings) {
		// Do nothing
	}

	public ArcadSettings getCurrentSettings(final ArcadSettings ref) {
		for (final ArcadSettings element : list) {
			final ArcadSettings s = element;
			if (s.getServerName().equalsIgnoreCase(ref.getServerName())
					&& s.getUserName().equalsIgnoreCase(ref.getUserName()) && keyEquals(ref, s)) {
				return s;
			}
		}
		return null;
	}

	protected abstract String getFileName();

	public ArrayList<ArcadSettings> getList() {
		return list;
	}

	public ArrayList<ArcadSettings> getSettings() {
		final ArrayList<ArcadSettings> l = new ArrayList<>();
		for (int i = 0; i < list.size(); i++) {
			final ArcadSettings s = list.get(i);
			if (keep(s)) {
				l.add(s);
			}
		}
		return l;
	}

	protected boolean keep(final ArcadSettings s) {
		return true;
	}

	public abstract boolean keyEquals(ArcadSettings ref, ArcadSettings toCompare);

	public void load() {
		final File xmlFile = new File(getFileName());
		if (xmlFile.exists() && xmlFile.length() > 0) {
			try (FileReader reader = new FileReader(xmlFile)) {
				list.clear();
				final XMLMemento x = XMLMemento.createReadRoot(reader);
				final IMemento[] servers = x.getChildren("server"); //$NON-NLS-1$
				for (final IMemento server2 : servers) {
					final String server = server2.getString("name"); //$NON-NLS-1$
					final IMemento[] users = server2.getChildren("user"); //$NON-NLS-1$
					for (final IMemento user2 : users) {
						final String user = user2.getString("name"); //$NON-NLS-1$
						list.add(readKeys(server, user, user2));
					}
				}
			} catch (final Exception e) {
				MessageManager.addException(e, MessageManager.LEVEL_PRODUCTION).addDetail(MessageDetail.ERROR,
						"XMLFilters.loadFilteredElement");//$NON-NLS-1$
			}
		}
	}

	public abstract ArcadSettings readKeys(String serverName, String userName, IMemento user);

	public void removeCurrentSettings(final ArcadSettings settings) {
		for (int i = 0; i < list.size(); i++) {
			final ArcadSettings s = list.get(i);
			if (s.getServerName().equalsIgnoreCase(settings.getServerName())
					&& s.getUserName().equalsIgnoreCase(settings.getUserName()) && keyEquals(settings, s)) {
				list.remove(i);
				break;
			}
		}
		save();
	}

	public void save() {
		// Enregistrement du fichier
		final XMLMemento x = XMLMemento.createWriteRoot("element"); //$NON-NLS-1$
		for (final ArcadSettings element : list) {
			final ArcadSettings es = element;
			final String serverName = es.getServerName();
			final String userName = es.getUserName();
			final IMemento server = x.createChild("server"); //$NON-NLS-1$
			server.putString("name", serverName); //$NON-NLS-1$
			final IMemento user = server.createChild("user"); //$NON-NLS-1$
			user.putString("name", userName); //$NON-NLS-1$
			saveKeys(user, es);
		}
		try {
			x.save(new FileWriter(getFileName()));
		} catch (final IOException e) {
			MessageManager.addException(e, MessageManager.LEVEL_PRODUCTION).addDetail(MessageDetail.ERROR,
					"File : " + getFileName());//$NON-NLS-1$
		}
	}

	public abstract void saveKeys(IMemento user, ArcadSettings s);

	public void setCurrentSettings(final ArcadSettings settings) {
		final Iterator<ArcadSettings> settingsList = list.iterator();
		while (settingsList.hasNext()) {
			final ArcadSettings s = settingsList.next();
			if (s.getServerName().equalsIgnoreCase(settings.getServerName())
					&& s.getUserName().equalsIgnoreCase(settings.getUserName()) && keyEquals(settings, s)) {
				list.remove(s);
				break;
			}
		}
		doBeforeAdding(settings);
		list.add(settings);
		save();
	}

	public void setSettings(final ArrayList<ArcadSettings> settings) {
		for (int i = list.size() - 1; i >= 0; i--) {
			final ArcadSettings s = list.get(i);
			if (keep(s)) {
				list.remove(i);
			}
		}

		for (int i = 0; i < settings.size(); i++) {
			final ArcadSettings s = settings.get(i);
			if (keep(s)) {
				list.add(s);
			}
		}
		save();
	}
}
