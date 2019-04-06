package com.arcadsoftware.aev.core.messages;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.MultiStatus;

/**
 * Multi-Message class that allows embedded Message detail (see {@link MultiStatus})
 * 
 * @author ACL
 *
 */
public class MultiMessage extends Message {

	private List<IMessageDetails> detailList = new ArrayList<IMessageDetails>();

	
	public MultiMessage(String command) {
		super(command);
	}

	public MultiMessage(String command, int type, String description) {
		super(command, type, description);
	}

	public MultiMessage(String command, int type, int level, String description) {
		super(command, type, level, description);
	}

	public MultiMessage(String command, int type, int level,
			String description, boolean fixedType) {
		super(command, type, level, description, fixedType);
	}

	@Override
	public Message addDetail(int type, String description) {
		super.addDetail(type, description);
		// Get created MessageDetail (last MessageDetail in list) and add to hierarchical list as well
		List<MessageDetail> messageDetails = super.getDetails();
		detailList.add(
				messageDetails.get(messageDetails.size()-1));
		return this;
	}
	
	public void addMessage(Message message) {
		detailList.add(message);
		
		// Keep "flattened" view of message details in synch with hierarchical detailList
		super.addDetail(message.getMaxDetailsType(), message.getDescription()); // Add message as message-detail entry
		List<MessageDetail> messageDetails = super.getDetails();
		messageDetails.addAll(message.getDetails()); // Add all details
	}
	
	@Override
	public List<IMessageDetails> getMessageDetails() {
		return detailList;
	}
}
