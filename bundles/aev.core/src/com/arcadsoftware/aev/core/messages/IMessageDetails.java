package com.arcadsoftware.aev.core.messages;

import java.util.List;

public interface IMessageDetails {
	/**
	 * @return Hierarchical list of Message Details (each detail entry can possible have embedded sub-detail 
	 */
	public List<? extends IMessageDetails> getMessageDetails();
	public int getMaxDetailsType();
	public String getDescription();
}
