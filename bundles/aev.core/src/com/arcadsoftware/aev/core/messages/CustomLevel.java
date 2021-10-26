package com.arcadsoftware.aev.core.messages;

import java.util.logging.Level;

public class CustomLevel extends Level {
	private static final long serialVersionUID = -368271218488446920L;

	public CustomLevel(String name, int value) {
		super(name, value);
	}

	public CustomLevel(String name, int value, String resourceBundleName) {
		super(name, value, resourceBundleName);
	}

}
