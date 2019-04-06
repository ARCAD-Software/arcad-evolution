package com.arcadsoftware.aev.core.messages;

import java.util.List;

public interface IMessageDetails {
	
	/*TODO: For Java 1.8?
	public List<? extends IMessageDetails> getMessageDetails();
	*/
	/**
	 * @return Hierarchical list of Message Details (each detail entry can possible have embedded sub-detail 
	 */
	public List getMessageDetails();
	public int getMaxDetailsType();
	public String getDescription();
}
