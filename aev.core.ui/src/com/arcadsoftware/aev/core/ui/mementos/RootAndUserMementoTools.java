/*
 * Créé le 2 févr. 05
 *
 * Pour changer le modèle de ce fichier généré, allez à :
 * Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
package com.arcadsoftware.aev.core.ui.mementos;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import org.eclipse.ui.IMemento;
import org.eclipse.ui.WorkbenchException;
import org.eclipse.ui.XMLMemento;

import com.arcadsoftware.aev.core.messages.MessageDetail;
import com.arcadsoftware.aev.core.messages.MessageManager;

/**
 * @author MD
 * 
 *         Pour changer le modèle de ce commentaire de type généré, allez à :
 *         Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et
 *         commentaires
 */
public abstract class RootAndUserMementoTools {
	@SuppressWarnings("unchecked")
	protected ArrayList list = new ArrayList();

	protected abstract String getFileName();

	public abstract ArcadSettings readKeys(String serverName, String userName, IMemento user);

	public abstract void saveKeys(IMemento user, ArcadSettings s);

	public abstract boolean keyEquals(ArcadSettings ref, ArcadSettings toCompare);

	/**
	 * @param s
	 */
	protected boolean keep(ArcadSettings s) {
		return true;
	}

	/**
	 * @param serverName
	 * @param userName
	 */
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

	@SuppressWarnings("unchecked")
	public ArrayList getSettings() {
		ArrayList l = new ArrayList();
		for (int i = 0; i < list.size(); i++) {
			ArcadSettings s = (ArcadSettings) list.get(i);
			if (keep(s)) {
				l.add(s);
			}
		}
		return l;
	}

	@SuppressWarnings("unchecked")
	public void setSettings(ArrayList settings) {
		// On supprime de la liste tous les settings correspondant
		// au filtre
		for (int i = list.size() - 1; i >= 0; i--) {
			ArcadSettings s = (ArcadSettings) list.get(i);
			if (keep(s)) {
				list.remove(i);
			}
		}
		// On ajoute à la liste les settings passés en paramétre
		for (int i = 0; i < settings.size(); i++) {
			ArcadSettings s = (ArcadSettings) settings.get(i);
			if (keep(s)) {
				list.add(s);
			}
		}
		save();
	}

	/**
	 * @param settings
	 */
	protected void doBeforeAdding(ArcadSettings settings) {
		// Do nothing
	}

	@SuppressWarnings("unchecked")
	public void setCurrentSettings(ArcadSettings settings) {
		for (int i = 0; i < list.size(); i++) {
			ArcadSettings s = (ArcadSettings) list.get(i);
			if ((s.getServerName().equalsIgnoreCase(settings.getServerName()))
					&& (s.getUserName().equalsIgnoreCase(settings.getUserName())) && keyEquals(settings, s)) {
				list.remove(i);
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

	@SuppressWarnings("unchecked")
	public void load() {
		try {
			list.clear();
			XMLMemento x = XMLMemento.createReadRoot(new FileReader(getFileName()));
			// Récupération des serveurs
			IMemento[] servers = x.getChildren("server"); //$NON-NLS-1$
			for (int i = 0; i < servers.length; i++) {
				String server = servers[i].getString("name"); //$NON-NLS-1$				
				IMemento[] users = servers[i].getChildren("user"); //$NON-NLS-1$
				for (int j = 0; j < users.length; j++) {
					String user = users[j].getString("name"); //$NON-NLS-1$
					list.add(readKeys(server, user, users[j]));
				}
			}
		} catch (WorkbenchException e) {
			MessageManager.addException(e, MessageManager.LEVEL_PRODUCTION).addDetail(MessageDetail.ERROR,
					"XMLFilters.loadFilteredElement");//$NON-NLS-1$
		} catch (FileNotFoundException e) {
			MessageManager.addException(e, MessageManager.LEVEL_BETATESTING).addDetail(MessageDetail.ERROR,
					"File : " + getFileName());//$NON-NLS-1$
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

	/**
	 * @return Returns the list.
	 */
	@SuppressWarnings("unchecked")
	public ArrayList getList() {
		return list;
	}
}
