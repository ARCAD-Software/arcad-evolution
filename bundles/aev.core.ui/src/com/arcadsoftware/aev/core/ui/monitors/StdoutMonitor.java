package com.arcadsoftware.aev.core.ui.monitors;

import com.arcadsoftware.aev.core.model.IMonitor;
import com.arcadsoftware.aev.core.tools.StringTools;

/**
 * Monitor permettant de recevoir une notification de message à chaque changement d'étape d'une Action et de renvoyer
 * ces messages sur la sortie standard.
 *
 * @author dlelong
 */
public class StdoutMonitor implements IMonitor {

	private String actionDefinition = StringTools.EMPTY;
	protected boolean verbose = true;

	public StdoutMonitor(final String actionDefinition) {
		this(actionDefinition, true);
	}

	/**
	 * @param actionDefinition
	 *            : Texte de l'action
	 * @param verbose
	 *            : si 'true' écriture des messages de chaque étape, sinon écriture de début et de fin d'action
	 */
	public StdoutMonitor(final String actionDefinition, final boolean verbose) {
		super();
		this.actionDefinition = actionDefinition;
		this.verbose = verbose;
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

	@Override
	public void end(final String message) {
		write(message);
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
	 * Ecrit sur la sortie standrad la définition de l'action concaténée avec le message indiquant l'étape franchie
	 *
	 * @param message
	 */
	protected void write(final String message) {
		System.out.println(actionDefinition.concat(" : ").concat(message)); //$NON-NLS-1$
	}

}
