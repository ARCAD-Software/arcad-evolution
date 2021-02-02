package com.arcadsoftware.ae.core.logger.messages;

import java.util.ArrayList;

public class Messages {
	ArrayList<AbstractMessage> list = new ArrayList<>();

	public void add(final AbstractMessage message) {
		list.add(message);
	}

	public void clear() {
		list.clear();
	}

	public AbstractMessage messageAt(final int index) {
		if (index > -1 && index < messageCount()) {
			return list.get(index);
		}
		return null;
	}

	public int messageCount() {
		return list.size();
	}
}
