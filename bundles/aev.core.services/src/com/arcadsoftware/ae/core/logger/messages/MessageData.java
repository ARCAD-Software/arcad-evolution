package com.arcadsoftware.ae.core.logger.messages;

public class MessageData {

	private String data = null;
	private String key = null;

	public MessageData(final String key, final String data) {
		super();
		this.key = key;
		this.data = data;
	}

	public String getData() {
		return data;
	}

	public String getKey() {
		return key;
	}

	public void setData(final String data) {
		this.data = data;
	}

	public void setKey(final String key) {
		this.key = key;
	}
}
