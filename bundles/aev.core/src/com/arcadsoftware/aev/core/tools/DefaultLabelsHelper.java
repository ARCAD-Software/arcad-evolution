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
 * Created on Sep 21, 2006
 */
package com.arcadsoftware.aev.core.tools;

/**
 * @author MD
 */
public class DefaultLabelsHelper implements IHelperLabel {

	public DefaultLabelsHelper() {
		super();
	}

	@Override
	public String resString(final String res, final Object... params) {
		try {
			return String.format(res, params);
		} catch (final Exception e) {
			return res;
		}
	}
}
