package com.arcadsoftware.aev.core.ui.mementos;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import org.eclipse.ui.WorkbenchException;
import org.eclipse.ui.XMLMemento;

import com.arcadsoftware.aev.core.messages.MessageDetail;
import com.arcadsoftware.aev.core.messages.MessageManager;

public abstract class ExplorerMementoTools extends ExplorerSettings {
	ArrayList<ExplorerSettings> list = new ArrayList<ExplorerSettings>();

	public ExplorerMementoTools(String viewId) {
		super();
		setViewId(viewId);
	}

	protected abstract String getFilename();

	public void readAll() {
		try {
			list.clear();
			XMLMemento x = XMLMemento.createReadRoot(new FileReader(getFilename()));
			readAll(x);
		} catch (WorkbenchException e) {
			MessageManager.addException(e, MessageManager.LEVEL_PRODUCTION).addDetail(MessageDetail.ERROR,
					"XMLFilters.loadFilteredElement");//$NON-NLS-1$
		} catch (FileNotFoundException e) {
			MessageManager.addException(e, MessageManager.LEVEL_PRODUCTION).addDetail(MessageDetail.ERROR,
					"File : " + getFilename());//$NON-NLS-1$
		}
	}

	public void save() {
		readAll();
		// Supression des anciennes valeurs
		deleteOldValues();
		// Réintroduction des valeurs
		list.add(createNewExplorerSettings());
		// Enregistrement du fichier
		XMLMemento x = XMLMemento.createWriteRoot("element"); //$NON-NLS-1$
		for (int i = 0; i < list.size(); i++) {
			ExplorerSettings es = (ExplorerSettings) list.get(i);
			saveExplorerSettings(x, es);
		}
		try {
			x.save(new FileWriter(getFilename()));
		} catch (IOException e) {
			MessageManager.addException(e, MessageManager.LEVEL_PRODUCTION).addDetail(MessageDetail.ERROR,
					"File : " + getFilename());//$NON-NLS-1$
		}
	}

	public void restore() {
		readAll();
		for (int i = list.size() - 1; i >= 0; i--) {
			ExplorerSettings es = list.get(i);
			if (isGoodExplorerSettings(es)) {
				setKeyValue(es.getKeyValue());
				return;
			}
		}
	}

	/**
	 * Méthode permettant de comparer l'explorerSettings aux paramètres locaux
	 * 
	 * @param explorerSettings
	 * @return
	 */
	protected abstract boolean isGoodExplorerSettings(ExplorerSettings explorerSettings);

	/**
	 * Méthode permettant de relire les données stockées à partir de l'élément
	 * root du fichier XML
	 * 
	 * @param root
	 *            : racine du document XML
	 */
	protected abstract void readAll(XMLMemento root);

	/**
	 * Méthode permettant de supprimer les anciennes valeurs avant sauvegarde
	 * des nouvelles
	 */
	protected abstract void deleteOldValues();

	/**
	 * Construction d'un ExplorerSettings en fonction des paramètres choisis
	 * 
	 * @return
	 */
	protected abstract ExplorerSettings createNewExplorerSettings();

	/**
	 * Méthode de sauvegarde d'un ExplorerSettings à la racine du document XML
	 * 
	 * @param root
	 *            : racine du document XML
	 * @param es
	 *            : l'ExplorerSettings à sauvegarder
	 */
	protected abstract void saveExplorerSettings(XMLMemento root, ExplorerSettings es);
}
