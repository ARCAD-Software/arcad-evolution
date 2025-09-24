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
package com.arcadsoftware.aev.core.ui.monitors;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

import com.arcadsoftware.aev.core.messages.MessageManager;
import com.arcadsoftware.aev.core.model.IMonitor;

/**
 * Monitor permettant de recevoir une notification de message � chaque changement d'�tape d'une Action et de renvoyer
 * ces messages sur un fichier de log dont le chemin d'acc�s est pass� en param�tre.
 *
 * @author dlelong
 */
public class FileMonitor implements IMonitor {

	protected String actionDefinition;
	protected boolean verbose = true;
	protected FileWriter writer;

	// Par d�faut le monitor fonctionne en mode verbose
	public FileMonitor(final String actionDefinition, final String filename) {
		this(actionDefinition, filename, true);
	}

	/**
	 * Cr�ation d'un nouveau FileMonitor
	 * 
	 * @param actionDefinition
	 *            : Texte court expliquant ce que fait l'action �cout�e
	 * @param filename
	 *            : Chemin d'acc�s au fichier dans lequel on va stocker les informations
	 * @param verbose
	 *            : si 'true' �criture des messages de chaque �tape, sinon �criture de d�but et de fin d'action
	 */
	public FileMonitor(final String actionDefinition, final String filename, final boolean verbose) {
		super();
		this.actionDefinition = actionDefinition;
		this.verbose = verbose;
		init(filename);
	}

	@Override
	public void afterExecute(final String message, final int numberOfStep) {
		if (verbose) {
			write(message);
		}
	}

	@Override
	public void beforeExecute(final String message, final int numberOfStep) {
		if (verbose) {
			write(message);
		}
	}

	@Override
	public void begin(final String message, final int totalNumberOfStep) {
		write(message);
	}

	/**
	 * Lib�ration des ressources
	 */
	protected void dispose() {
		try {
			if (writer != null) {
				writer.close();
			}
		} catch (final IOException e) {
			MessageManager.addException(e, MessageManager.SHOW_ERROR);
		}
	}

	@Override
	public void end(final String message) {
		write(message);
		dispose();
	}

	/**
	 * Initialisation du writer et cr�ation du fichier s'il n'existe pas
	 * 
	 * @param filename
	 */
	protected void init(final String filename) {
		try {
			final File f = new File(filename);
			if (!f.exists()) {
				if (!f.createNewFile()) {
					return;
				}
			}
			writer = new FileWriter(filename, true);
		} catch (final FileNotFoundException e) {
			MessageManager.addException(e, MessageManager.SHOW_ERROR);
			dispose();
		} catch (final IOException e) {
			MessageManager.addException(e, MessageManager.SHOW_ERROR);
			dispose();
		}
	}

	@Override
	public void initialize(final String message, final int numberOfStep) {
		if (verbose) {
			write(message);
		}
	}

	@Override
	public void progress(final String message, final int numberOfStep) {
		if (verbose) {
			write(message);
		}
	}

	/**
	 * Ajout � la fin du fichier de la d�finition de l'action suivi du message pass� en param�tre
	 * 
	 * @param message
	 */
	protected void write(final String message) {
		try {
			final String text = actionDefinition.concat(" : ").concat(message).concat("\n"); //$NON-NLS-1$ //$NON-NLS-2$
			if (writer != null) {
				writer.write(text, 0, text.length());
			}
		} catch (final IOException e) {
			MessageManager.addException(e, MessageManager.SHOW_ERROR);
			dispose();
		}
	}
}
