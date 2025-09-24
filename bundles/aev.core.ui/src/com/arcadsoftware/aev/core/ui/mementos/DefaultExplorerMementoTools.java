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
package com.arcadsoftware.aev.core.ui.mementos;

import org.eclipse.ui.XMLMemento;

import com.arcadsoftware.aev.core.tools.StringTools;

public class DefaultExplorerMementoTools extends ExplorerMementoTools {

	public DefaultExplorerMementoTools(final String viewId) {
		super(viewId);
	}

	@Override
	protected ExplorerSettings createNewExplorerSettings() {
		return null;
	}

	@Override
	protected void deleteOldValues() {
		// Do nothing
	}

	@Override
	protected String getFilename() {
		return StringTools.EMPTY;
	}

	@Override
	protected boolean isGoodExplorerSettings(final ExplorerSettings explorerSettings) {
		return false;
	}

	@Override
	protected void readAll(final XMLMemento root) {
		// Do nothing
	}

	@Override
	protected void saveExplorerSettings(final XMLMemento root, final ExplorerSettings es) {
		// Do nothing
	}
}
