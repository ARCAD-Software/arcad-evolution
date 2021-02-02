package com.arcadsoftware.mmk.lists;

public class ArcadListException extends Exception {
	private static final long serialVersionUID = -8193901682692437711L;

	public ArcadListException(final String message) {
		super(message);
	}

	public ArcadListException(final String message, final Exception cause) {
		super(message, cause);
	}
}
