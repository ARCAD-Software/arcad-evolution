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
package com.arcadsoftware.aev.core.ui.actions;

import org.eclipse.jface.action.Separator;

/**
 * Dummy class to mimic a {@link Separator}
 *
 * @author ARCAD Software
 */
public class ArcadSeparator extends ArcadAction {
	@Override
	protected boolean canExecute() {
		return false;
	}

	@Override
	public boolean isNotSeparator() {
		return false;
	}
}
