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
package com.arcadsoftware.aev.core.messages;

import java.util.logging.Level;

public class CustomLevel extends Level {
	private static final long serialVersionUID = -368271218488446920L;

	public CustomLevel(String name, int value) {
		super(name, value);
	}

	public CustomLevel(String name, int value, String resourceBundleName) {
		super(name, value, resourceBundleName);
	}

}
