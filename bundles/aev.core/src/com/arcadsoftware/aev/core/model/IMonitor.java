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
package com.arcadsoftware.aev.core.model;

/**
 * Interface � impl�menter pour toute classe voulant �tre g�r�e par le MonitorManager Elle reprend les diff�rentes
 * �tapes d'une ArcadAction et permet d'ajouter un message � chaque �tape et d'informer de l'avancement avec le nombre
 * d'�tape franchie
 * 
 * @author dlelong
 */
public interface IMonitor {

	void afterExecute(String message, int numberOfStep);

	void beforeExecute(String message, int numberOfStep);

	void begin(String message, int totalNumberOfStep);

	void end(String message);

	void initialize(String message, int numberOfStep);

	void progress(String message, int numberOfStep);
}
