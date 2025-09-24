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
/*
 * Created on Sep 12, 2006
 */
package com.arcadsoftware.aev.core.tools;

/**
 * @author MD
 */
public class CoreLabels {
	private static CoreLabels instance;

	public static CoreLabels getInstance() {
		if (instance == null) {
			instance = new CoreLabels();
		}
		return instance;
	}

	public static String resString(final String res, final Object... params) {
		return getInstance().getHelper().resString(res, params);
	}

	private IHelperLabel helperLabel = null;

	public CoreLabels() {
		super();
	}

	public IHelperLabel getHelper() {
		if (helperLabel == null) {
			helperLabel = new DefaultLabelsHelper();
		}
		return helperLabel;
	}

	public void setLabelHelper(final IHelperLabel helperLabel) {
		this.helperLabel = helperLabel;
	}
}
