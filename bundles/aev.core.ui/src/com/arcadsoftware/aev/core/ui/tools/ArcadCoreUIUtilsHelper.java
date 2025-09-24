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
package com.arcadsoftware.aev.core.ui.tools;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import com.arcadsoftware.aev.core.tools.ArcadCoreUtilsHelper;
import com.arcadsoftware.aev.core.ui.EvolutionCoreUIPlugin;

public class ArcadCoreUIUtilsHelper extends ArcadCoreUtilsHelper {
	private static Cursor cursor;

	@Override
	public void beginAction() {
		final Shell shell = EvolutionCoreUIPlugin.getDefault().getPluginShell();
		if (shell != null) {
			if (shell.getDisplay() != null) {
				final Display display = shell.getDisplay();
				cursor = new Cursor(display, SWT.CURSOR_WAIT);
				shell.setCursor(cursor);
			}
		}
	}

	@Override
	public void endAction() {
		final Shell shell = EvolutionCoreUIPlugin.getDefault().getPluginShell();
		if (shell != null) {
			shell.setCursor(null);
			cursor.dispose();
		}
	}
}
