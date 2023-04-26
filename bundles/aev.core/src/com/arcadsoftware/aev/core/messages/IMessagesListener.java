/*
 * Cr�� le 10 juin 2004
 * Projet : ARCAD Plugin Core
 * <i> Copyright 2004, Arcad-Software.</i>
 *  
 */
package com.arcadsoftware.aev.core.messages;

/**
 * 
 * @author mlafon
 * @version 1.0.0
 */
public interface IMessagesListener {

	/**
	 * Un nouveau message a �t� ajout�.
	 * @param message
	 */
	public void newMessageAdded(Message message);
	public void newMessageAdded(Message message, Throwable e);


	/**
	 * Un ou plusieurs messages ont �t� supprim�s
	 * (message = null dans le cas d'un clear). 
	 */
	public void messageDeleted(Message message);
	
	/**
	 * Le message a �t� modifi�.
	 * @param message le message modifi�.
	 */
	public void messageChanged(Message message);
}
