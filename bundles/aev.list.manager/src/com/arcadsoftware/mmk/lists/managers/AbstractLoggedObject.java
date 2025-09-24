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
package com.arcadsoftware.mmk.lists.managers;

import com.arcadsoftware.ae.core.logger.IMessageLogger;
import com.arcadsoftware.ae.core.utils.Utils;

public abstract class AbstractLoggedObject {
	protected IMessageLogger logger = null;

	public IMessageLogger getLogger() {
		return logger;
	}

	public void logError(final String moduleName, final String message) {
		if (logger != null) {
			logger.logMessage(message, IMessageLogger.LOGLVL_FATAL);
		}
	}

	public void logError(final String moduleName, final Throwable e) {
		logError(moduleName, e.getLocalizedMessage() + "\n" + Utils.stackTrace(e));
	}

	public void logInfo(final String moduleName, final String message) {
		if (logger != null) {
			logger.logMessage(message, IMessageLogger.LOGLVL_INFO);
		}
	}

	public void logVerbose(final String moduleName, final String message) {
		if (logger != null) {
			logger.logMessage(message, IMessageLogger.LOGLVL_VERBOSE);
		}
	}

	public void logWarning(final String moduleName, final String message) {
		if (logger != null) {
			logger.logMessage(message, IMessageLogger.LOGLVL_WARNING);
		}
	}

	public void setLogger(final IMessageLogger logger) {
		this.logger = logger;
	}
}
