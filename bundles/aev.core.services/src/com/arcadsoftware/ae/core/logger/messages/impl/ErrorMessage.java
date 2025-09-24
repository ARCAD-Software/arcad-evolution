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

package com.arcadsoftware.ae.core.logger.messages.impl;

public class ErrorMessage extends StatusMessage {
	/**
	 * Constructeur de classe.
	 *
	 * @param serviceName
	 *            String : Nom du service émetteur.
	 * @param message
	 *            : Texte principal du message.
	 */
	public ErrorMessage(final String serviceName, final String message) {
		super(serviceName, TYPE_MSG_ERROR, message, false);
	}

}
