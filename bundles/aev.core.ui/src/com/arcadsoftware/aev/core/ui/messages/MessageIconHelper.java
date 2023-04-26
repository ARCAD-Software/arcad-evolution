package com.arcadsoftware.aev.core.ui.messages;

import com.arcadsoftware.aev.core.messages.Message;
import com.arcadsoftware.aev.core.messages.MessageDetail;
import com.arcadsoftware.aev.icons.Icon;

public class MessageIconHelper {
	public static Icon getMessageDetailIcon(final MessageDetail messageDetail) {
		switch (messageDetail.getType()) {
		case MessageDetail.EXCEPTION:
			return Icon.MESSAGE_CRITICAL;
		case MessageDetail.ERROR:
			return Icon.MESSAGE_ERROR;
		case MessageDetail.WARNING:
			return Icon.MESSAGE_WARNING;
		case MessageDetail.COMPLETION:
			return Icon.MESSAGE_INFO;
		default:
			return Icon.MESSAGE_BUG;
		}
	}

	public static Icon getMessageIcon(final Message message) {
		switch (message.getMaxDetailsType()) {
		case MessageDetail.EXCEPTION:
			return Icon.CRITICAL;
		case MessageDetail.ERROR:
			return Icon.ERROR;
		case MessageDetail.WARNING:
			return Icon.WARNING;
		case MessageDetail.COMPLETION:
			return Icon.INFO;
		default:
			return Icon.BUG;
		}
	}

	private MessageIconHelper() {
	}
}
