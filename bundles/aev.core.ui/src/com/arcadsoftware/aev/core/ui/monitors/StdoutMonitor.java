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

import com.arcadsoftware.aev.core.model.IMonitor;
import com.arcadsoftware.aev.core.tools.StringTools;

/**
 * Monitor permettant de recevoir une notification de message � chaque changement d'�tape d'une Action et de renvoyer
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
	 *            : si 'true' �criture des messages de chaque �tape, sinon �criture de d�but et de fin d'action
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
	 * Ecrit sur la sortie standrad la d�finition de l'action concat�n�e avec le message indiquant l'�tape franchie
	 *
	 * @param message
	 */
	protected void write(final String message) {
		System.out.println(actionDefinition.concat(" : ").concat(message)); //$NON-NLS-1$
	}

}
