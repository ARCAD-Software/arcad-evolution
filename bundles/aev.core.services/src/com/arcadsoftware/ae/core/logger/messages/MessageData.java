/*
 * Cr‚‚ le 13 mars 2007
 *
 */
package com.arcadsoftware.ae.core.logger.messages;

/**
 * @author MD
 *
 */
public class MessageData {

	private String key = null;
	private String data = null;
	
	public MessageData(String key,String data) {
		super();
		this.key = key;
		this.data = data;
	}
	
	/**
	 * @return Renvoie data.
	 */
	public String getData() {
		return data;
	}
	/**
	 * @param data data … d‚finir.
	 */
	public void setData(String data) {
		this.data = data;
	}
	/**
	 * @return Renvoie key.
	 */
	public String getKey() {
		return key;
	}
	/**
	 * @param key key … d‚finir.
	 */
	public void setKey(String key) {
		this.key = key;
	}
}
