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
package com.arcadsoftware.ae.core.logger.messages;

public class MessageData {

	private String data = null;
	private String key = null;

	public MessageData(final String key, final String data) {
		super();
		this.key = key;
		this.data = data;
	}

	public String getData() {
		return data;
	}

	public String getKey() {
		return key;
	}

	public void setData(final String data) {
		this.data = data;
	}

	public void setKey(final String key) {
		this.key = key;
	}
}
