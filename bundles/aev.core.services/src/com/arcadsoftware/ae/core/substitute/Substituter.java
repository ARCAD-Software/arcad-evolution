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
package com.arcadsoftware.ae.core.substitute;

import com.arcadsoftware.ae.core.utils.Utils;

public class Substituter {
	private static Substituter instance = new Substituter();

	public static Substituter getInstance() {
		return instance;
	}

	private Substituter() {
		final String arcadHome = Utils.getHomeDirectory();
		System.setProperty(IAEAConstants.AEA_ARCAD_HOME, arcadHome);
	}

	public String substitute(String value) {
		final String[] variables = Utils.toSubstitute(value);
		for (final String variable : variables) {
			value = substituteProperty(value, variable);
		}
		return value;
	}

	public String substituteProperty(String value, final String propertyName) {
		value = Utils.substituteProperty(value, propertyName);
		return value;
	}

}
