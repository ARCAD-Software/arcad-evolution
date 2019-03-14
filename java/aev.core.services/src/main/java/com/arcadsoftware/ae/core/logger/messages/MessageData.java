package com.arcadsoftware.ae.core.logger.messages;

public class MessageData {

	private String key = null;
	private String data = null;
	
	public MessageData(String key,String data) {
		super();
		this.key = key;
		this.data = data;
	}
	
	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}
}
