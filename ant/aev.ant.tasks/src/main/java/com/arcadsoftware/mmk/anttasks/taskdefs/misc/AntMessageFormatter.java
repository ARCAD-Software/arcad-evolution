package com.arcadsoftware.mmk.anttasks.taskdefs.misc;

import com.arcadsoftware.ae.core.logger.formatter.AbstractMessageFormatter;
import com.arcadsoftware.ae.core.logger.messages.Messages;

public class AntMessageFormatter implements AbstractMessageFormatter {

	@Override
	public String format(final Messages messages) {
		final StringBuilder sb = new StringBuilder();
		for (int i = 0; i < messages.messageCount(); i++) {
			sb.append("[").append(messages.messageAt(i).getServiceName()).append("]");
			sb.append("[").append(messages.messageAt(i).getMessageType()).append("]");
			sb.append(messages.messageAt(i).getMessageText());
		}
		return sb.toString();
	}

}
