/*******************************************************************************
 * Copyright (c) 2025 ARCAD Software.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     ARCAD Software - initial API and implementation
 *******************************************************************************/
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
	ArrayList<ExplorerSettings> list = new ArrayList<>();

	public ExplorerMementoTools(final String viewId) {
		super();
		setViewId(viewId);
	}

	/**
	 * Construction d'un ExplorerSettings en fonction des param�tres choisis
	 *
	 * @return
	 */
	protected abstract ExplorerSettings createNewExplorerSettings();

	/**
	 * M�thode permettant de supprimer les anciennes valeurs avant sauvegarde des nouvelles
	 */
	protected abstract void deleteOldValues();

	protected abstract String getFilename();

	/**
	 * M�thode permettant de comparer l'explorerSettings aux param�tres locaux
	 *
	 * @param explorerSettings
	 * @return
	 */
	protected abstract boolean isGoodExplorerSettings(ExplorerSettings explorerSettings);

	public void readAll() {
		try {
			list.clear();
			final XMLMemento x = XMLMemento.createReadRoot(new FileReader(getFilename()));
			readAll(x);
		} catch (final WorkbenchException e) {
			MessageManager.addException(e, MessageManager.LEVEL_PRODUCTION).addDetail(MessageDetail.ERROR,
					"XMLFilters.loadFilteredElement");//$NON-NLS-1$
		} catch (final FileNotFoundException e) {
			MessageManager.addException(e, MessageManager.LEVEL_PRODUCTION).addDetail(MessageDetail.ERROR,
					"File : " + getFilename());//$NON-NLS-1$
		}
	}

	/**
	 * M�thode permettant de relire les donn�es stock�es � partir de l'�l�ment root du fichier XML
	 *
	 * @param root
	 *            : racine du document XML
	 */
	protected abstract void readAll(XMLMemento root);

	public void restore() {
		readAll();
		for (int i = list.size() - 1; i >= 0; i--) {
			final ExplorerSettings es = list.get(i);
			if (isGoodExplorerSettings(es)) {
				setKeyValue(es.getKeyValue());
				return;
			}
		}
	}

	public void save() {
		readAll();
		// Supression des anciennes valeurs
		deleteOldValues();
		// R�introduction des valeurs
		list.add(createNewExplorerSettings());
		// Enregistrement du fichier
		final XMLMemento x = XMLMemento.createWriteRoot("element"); //$NON-NLS-1$
		for (final ExplorerSettings element : list) {
			final ExplorerSettings es = element;
			saveExplorerSettings(x, es);
		}
		try {
			x.save(new FileWriter(getFilename()));
		} catch (final IOException e) {
			MessageManager.addException(e, MessageManager.LEVEL_PRODUCTION).addDetail(MessageDetail.ERROR,
					"File : " + getFilename());//$NON-NLS-1$
		}
	}

	/**
	 * M�thode de sauvegarde d'un ExplorerSettings � la racine du document XML
	 *
	 * @param root
	 *            : racine du document XML
	 * @param es
	 *            : l'ExplorerSettings � sauvegarder
	 */
	protected abstract void saveExplorerSettings(XMLMemento root, ExplorerSettings es);
}
