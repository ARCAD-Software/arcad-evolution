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
package com.arcadsoftware.ae.core.logger.formatter.impl;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.arcadsoftware.ae.core.logger.formatter.AbstractMessageFormatter;
import com.arcadsoftware.ae.core.logger.messages.AbstractMessage;
import com.arcadsoftware.ae.core.logger.messages.MessageData;
import com.arcadsoftware.ae.core.logger.messages.Messages;

public class BasicMessageFormatter implements AbstractMessageFormatter {

	SimpleDateFormat sp = new SimpleDateFormat("yyyyMMdd-hh:mm:ss:SSS");

	private String createHeader(final AbstractMessage message) {
		final StringBuilder sb = new StringBuilder();
		sb.append('[').append(sp.format(new Date())).append(']')
				.append('[').append(message.getServiceName()).append(']')
				.append('[').append(message.getMessageType()).append(']')
				.append(message.getMessageText());
		return sb.toString();
	}

	public String format(final AbstractMessage message) {
		final StringBuilder sb = new StringBuilder(createHeader(message));
		if (!message.getDatas().isEmpty()) {
			sb.append('(');
			for (final MessageData element : message.getDatas()) {
				final MessageData md = element;
				final String key = md.getKey();
				final String value = md.getData();
				sb.append(key).append('=').append(value).append(';');
			}
			sb.append(')');
		}
		return sb.toString();
	}

	@Override
	public String format(final Messages messages) {
		final StringBuilder sb = new StringBuilder();
		final int count = messages.messageCount();
		for (int i = 0; i < count; i++) {
			final AbstractMessage message = messages.messageAt(i);
			sb.append(format(message)).append('\n');
		}
		return sb.toString();
	}

}
