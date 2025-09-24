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
package com.arcadsoftware.aev.core.messages;

/**
 * @author mlafon
 * @version 1.1.0
 */
public interface IMessagesListener {

	/**
	 * Le message a �t� modifi�.
	 * 
	 * @param message
	 *            le message modifi�.
	 */
	void messageChanged(Message message);

	/**
	 * Un ou plusieurs messages ont �t� supprim�s (message = null dans le cas d'un clear).
	 */
	void messageDeleted(Message message);

	void newMessageAdded(Message message);

	/**
	 * A new messages has been added to ARCAD Messages list.
	 * 
	 * @param message
	 * @param e
	 *            and exception, can be null.
	 */
	void newMessageAdded(Message message, Throwable e);
}
