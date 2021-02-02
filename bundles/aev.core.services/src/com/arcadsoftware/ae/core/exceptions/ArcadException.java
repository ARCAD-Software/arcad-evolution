package com.arcadsoftware.ae.core.exceptions;

public class ArcadException extends Exception {
	private static final long serialVersionUID = 1L;
	private final Exception cause;

	/**
	 * Creates a RexecJException object.
	 *
	 * @param message
	 *            a String containing the message for this exception.
	 */

	public ArcadException(final String message) {
		this(message, new Exception(message));
	}

	/**
	 * Creates a ArcadException object.
	 *
	 * @param message
	 *            a String containing the message for this exception.
	 * @param cause
	 *            an Exception object that is the root cause of this exception.
	 */

	public ArcadException(final String message, final Exception cause) {
		super(message);
		this.cause = cause;
	}

	/**
	 * Retrieves the root cause for this ArcadException.
	 *
	 * @return a Throwable object containing the root cause for this RexecJException.
	 */

	@Override
	public synchronized Throwable getCause() {
		return cause;
	}
}
