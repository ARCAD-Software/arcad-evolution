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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.MultiStatus;

/**
 * Multi-Message class that allows embedded Message detail (see {@link MultiStatus})
 *
 * @author ACL
 */
public class MultiMessage extends Message {

	private final List<IMessageDetails> detailList = new ArrayList<>();

	public MultiMessage(final String command) {
		super(command);
		style = STYLE_SHOW | STYLE_BLOCK;
	}

	@Override
	public Message addDetail(final int type, final String description) {
		// Get created MessageDetail (last MessageDetail in list) and add to hierarchical list as well
		final MessageDetail messageDetail = addMessageDetail(type, description);
		detailList.add(messageDetail);
		return this;
	}

	public void addMessage(final Message message) {
		detailList.add(message);

		// Keep "flattened" view of message details in synch with hierarchical detailList
		super.addDetail(message.getMaxDetailsType(), message.getDescription()); // Add message as message-detail entry
		final List<MessageDetail> messageDetails = super.getDetails();
		messageDetails.addAll(message.getDetails()); // Add all details
	}

	@Override
	public List<IMessageDetails> getMessageDetails() {
		return detailList;
	}
}
