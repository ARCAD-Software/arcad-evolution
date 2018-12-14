/*
 * Cr‚‚ le 14 mars 2007
 *
 */
package com.arcadsoftware.ae.core.logger.messages.impl;

import org.dom4j.Element;

import com.arcadsoftware.ae.core.logger.messages.IMessageXmlPartProvider;
/**
 * @author MD
 *
 */
public class StatusMessage extends SimpleMessage 
implements IMessageXmlPartProvider{

	private boolean status = true;
	
	public StatusMessage(String serviceName, String typeInfo, String message) {
		super(serviceName, typeInfo, message);
	}

	public StatusMessage(String serviceName, String typeInfo, String message,boolean status) {
		super(serviceName, typeInfo, message);
		this.status = status;
	}	
	
	public StatusMessage(String serviceName,boolean status) {
		super(serviceName, TYPE_MSG_STATUS, status ? "SUCCEED": "FAILED");
		this.status = status;
	}

	/* (non-Javadoc)
	 * @see com.arcadsoftware.serviceprovider.message.IMessageXmlPartProvider#setXMLHeaderPart(org.dom4j.Element)
	 */
	public void setXMLHeaderPart(Element root) {
		root.addAttribute("status",String.valueOf(status));		
	}

	/* (non-Javadoc)
	 * @see com.arcadsoftware.serviceprovider.message.IMessageXmlPartProvider#setXMLPart(org.dom4j.Element)
	 */
	public void setXMLPart(Element root) {
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

}
