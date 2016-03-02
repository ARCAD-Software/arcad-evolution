/*
 * Cr‚‚ le 15 mars 2007
 *
 */
package com.arcadsoftware.ae.core.logger.messages;

import java.util.ArrayList;


/**
 * @author MD
 *
 */
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
