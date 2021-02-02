package com.arcadsoftware.aev.core.ui.monitors;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

import com.arcadsoftware.aev.core.messages.MessageManager;
import com.arcadsoftware.aev.core.model.IMonitor;

/**
 * Monitor permettant de recevoir une notification de message à chaque changement d'étape d'une Action et de renvoyer
 * ces messages sur un fichier de log dont le chemin d'accés est passé en paramètre.
 *
 * @author dlelong
 */
public class FileMonitor implements IMonitor {

	protected String actionDefinition;
	protected boolean verbose = true;
	protected FileWriter writer;

	// Par défaut le monitor fonctionne en mode verbose
	public FileMonitor(final String actionDefinition, final String filename) {
		this(actionDefinition, filename, true);
	}

	/**
	 * Création d'un nouveau FileMonitor
	 * 
	 * @param actionDefinition
	 *            : Texte court expliquant ce que fait l'action écoutée
	 * @param filename
	 *            : Chemin d'accés au fichier dans lequel on va stocker les informations
	 * @param verbose
	 *            : si 'true' écriture des messages de chaque étape, sinon écriture de début et de fin d'action
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
	 * Libération des ressources
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
	 * Initialisation du writer et création du fichier s'il n'existe pas
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
	 * Ajout à la fin du fichier de la définition de l'action suivi du message passé en paramètre
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
