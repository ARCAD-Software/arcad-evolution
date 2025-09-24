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
package com.arcadsoftware.ae.core.logger.messages.impl;

import org.w3c.dom.Element;

import com.arcadsoftware.ae.core.logger.messages.IMessageXmlPartProvider;

public class StatusMessage extends SimpleMessage
		implements IMessageXmlPartProvider {

	private boolean status = true;

	public StatusMessage(final String serviceName, final boolean status) {
		super(serviceName, TYPE_MSG_STATUS, status ? "SUCCEED" : "FAILED");
		this.status = status;
	}

	public StatusMessage(final String serviceName, final String typeInfo, final String message) {
		super(serviceName, typeInfo, message);
	}

	public StatusMessage(final String serviceName, final String typeInfo, final String message, final boolean status) {
		super(serviceName, typeInfo, message);
		this.status = status;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(final boolean status) {
		this.status = status;
	}

	@Override
	public void setXMLHeaderPart(final Element root) {
		root.setAttribute("status", String.valueOf(status));
	}

	@Override
	public void setXMLPart(final Element root) {
		//nothing to set
	}

}
