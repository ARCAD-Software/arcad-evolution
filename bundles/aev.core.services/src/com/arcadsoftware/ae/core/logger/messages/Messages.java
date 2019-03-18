package com.arcadsoftware.ae.core.logger.messages;

import java.util.ArrayList;

public class Messages {
	ArrayList<AbstractMessage> list = new ArrayList<AbstractMessage>();
	
	
	public void add(AbstractMessage message) {
		list.add(message);
	}

	public void clear() {
		list.clear();
	}	
		
	public int messageCount() {
		return list.size();
	}	
	
	public AbstractMessage messageAt(int index) {
		if ((index>-1) && (index<messageCount())) {
			return list.get(index);
		}
		return null;
	}			
}
