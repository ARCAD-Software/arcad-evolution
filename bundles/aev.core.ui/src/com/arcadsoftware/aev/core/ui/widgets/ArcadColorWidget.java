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
package com.arcadsoftware.aev.core.ui.widgets;

import com.arcadsoftware.aev.core.model.ArcadListenerList;
import com.arcadsoftware.aev.core.ui.listeners.IModifyListener;
import com.arcadsoftware.aev.core.ui.tools.ArcadColorUI;

public class ArcadColorWidget {

	protected ArcadColorUI colorUI;
	protected ArcadListenerList modifyListener = new ArcadListenerList();

	public ArcadColorWidget() {
		super();
	}

	public void addModifyListener(final IModifyListener listener) {
		modifyListener.add(listener);
	}

	public void fireModifyListener() {
		final Object[] listeners = modifyListener.getListeners();
		for (final Object listener : listeners) {
			final IModifyListener l = (IModifyListener) listener;
			try {
				l.changed();
			} catch (final RuntimeException e1) {
				removeModifyListener(l);
			}
		}
	}

	public ArcadColorUI getColorUI() {
		return colorUI;
	}

	public void removeModifyListener(final IModifyListener listener) {
		modifyListener.remove(listener);
	}

	public void setColorUI(final ArcadColorUI colorUI) {
		this.colorUI = colorUI;
		fireModifyListener();
	}
}
