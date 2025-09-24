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
package com.arcadsoftware.mmk.lists;

public class ArcadListException extends Exception {
	private static final long serialVersionUID = -8193901682692437711L;

	public ArcadListException(final String message) {
		super(message);
	}

	public ArcadListException(final String message, final Exception cause) {
		super(message, cause);
	}
}
