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
package com.arcadsoftware.ae.core.logger;

public interface IMessageLogger {
	int LOGLVL_FATAL = 3;
	int LOGLVL_INFO = 1;
	int LOGLVL_VERBOSE = 0;
	int LOGLVL_WARNING = 2;

	void logMessage(String message, int logLevel);
}
