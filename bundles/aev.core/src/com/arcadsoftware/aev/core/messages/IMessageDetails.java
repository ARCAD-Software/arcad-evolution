package com.arcadsoftware.aev.core.messages;

import java.util.List;

public interface IMessageDetails {
	String getDescription();

	int getMaxDetailsType();

	/**
	 * @return Hierarchical list of Message Details (each detail entry can possible have embedded sub-detail
	 */
	List<? extends IMessageDetails> getMessageDetails();
}
