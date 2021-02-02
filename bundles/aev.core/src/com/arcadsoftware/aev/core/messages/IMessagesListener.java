/*
 * Cr�� le 10 juin 2004
 * Projet : ARCAD Plugin Core
 * <i> Copyright 2004, Arcad-Software.</i>
 *
 */
package com.arcadsoftware.aev.core.messages;

/**
 * @author mlafon
 * @version 1.1.0
 */
public interface IMessagesListener {

	/**
	 * Le message a �t� modifi�.
	 * 
	 * @param message
	 *            le message modifi�.
	 */
	void messageChanged(Message message);

	/**
	 * Un ou plusieurs messages ont �t� supprim�s (message = null dans le cas d'un clear).
	 */
	void messageDeleted(Message message);

	void newMessageAdded(Message message);

	/**
	 * A new messages has been added to ARCAD Messages list.
	 * 
	 * @param message
	 * @param e
	 *            and exception, can be null.
	 */
	void newMessageAdded(Message message, Throwable e);
}
