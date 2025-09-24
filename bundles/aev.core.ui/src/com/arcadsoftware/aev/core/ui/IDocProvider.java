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
package com.arcadsoftware.aev.core.ui;

/**
 * Classe centralisant les contextId permettant d'identifier une page d'aide et de la lier � un �l�ment graphique
 *
 * @author dlelong
 */
public class IDocProvider {
	public static final String ID = "com.arcadsoftware.aev.core.ui"; //$NON-NLS-1$
	public static final String MESSAGE_LOG_VIEW = "messageLogView"; //$NON-NLS-1$
	
	public static final String HLP_MESSAGESDETAIL = ID.concat(".messagedetails"); //$NON-NLS-1$
	public static final String HLP_MESSAGESLOG = ID.concat(".messagelog"); //$NON-NLS-1$
	
}
