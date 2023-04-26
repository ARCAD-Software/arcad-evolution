package com.arcadsoftware.aev.core.ui.monitors;

import com.arcadsoftware.aev.core.model.IMonitor;
import com.arcadsoftware.aev.core.tools.StringTools;

/**
 * Monitor permettant de recevoir une notification de message � chaque
 * changement d'�tape d'une Action et de renvoyer ces messages sur la sortie
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
	 *            : si 'true' �criture des messages de chaque �tape, sinon
	 *            �criture de d�but et de fin d'action
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
	 * Ecrit sur la sortie standrad la d�finition de l'action concat�n�e avec le
	 * message indiquant l'�tape franchie
	 * 
	 * @param message
	 */
	protected void write(String message) {
		System.out.println(actionDefinition.concat(" : ").concat(message)); //$NON-NLS-1$
	}

}
