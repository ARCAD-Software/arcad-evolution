package com.arcadsoftware.aev.core.ui.monitors;

import com.arcadsoftware.aev.core.model.IMonitor;
import com.arcadsoftware.aev.core.tools.StringTools;

/**
 * Monitor permettant de recevoir une notification de message à chaque
 * changement d'étape d'une Action et de renvoyer ces messages sur la sortie
 * standard.
 * 
 * @author dlelong
 * 
 */
public class StdoutMonitor implements IMonitor {

	private String actionDefinition = StringTools.EMPTY;
	protected boolean verbose = true;

	/**
	 * @param actionDefinition
	 *            : Texte de l'action
	 * @param verbose
	 *            : si 'true' écriture des messages de chaque étape, sinon
	 *            écriture de début et de fin d'action
	 */
	public StdoutMonitor(String actionDefinition, boolean verbose) {
		super();
		this.actionDefinition = actionDefinition;
		this.verbose = verbose;
	}

	public StdoutMonitor(String actionDefinition) {
		this(actionDefinition, true);
	}

	public void afterExecute(String message, int numberOfStep) {
		if (verbose)
			write(message);
	}

	public void beforeExecute(String message, int numberOfStep) {
		if (verbose)
			write(message);
	}

	public void begin(String message, int totalNumberOfStep) {
		write(message);
	}

	public void end(String message) {
		write(message);
	}

	public void initialize(String message, int numberOfStep) {
		if (verbose)
			write(message);
	}

	public void progress(String message, int numberOfStep) {
		if (verbose)
			write(message);
	}

	/**
	 * Ecrit sur la sortie standrad la définition de l'action concaténée avec le
	 * message indiquant l'étape franchie
	 * 
	 * @param message
	 */
	protected void write(String message) {
		System.out.println(actionDefinition.concat(" : ").concat(message)); //$NON-NLS-1$
	}

}
