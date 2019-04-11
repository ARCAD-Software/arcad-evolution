/*
 * Créé le 10 juin 2004
 * Projet : ARCAD Plugin Core
 * <i> Copyright 2004, Arcad-Software.</i>
 *  
 */
package com.arcadsoftware.aev.core.messages;

/**
 * 
 * @author mlafon
 * @version 1.1.0
 */
public interface IMessagesListener {

	/**
	 * A new messages has been added to ARCAD Messages list.
	 * @param message
	 * @param e and exception, can be null.
	 */
	public void newMessageAdded(Message message, Throwable e);
	public void newMessageAdded(Message message);


	/**
	 * Un ou plusieurs messages ont été supprimés
	 * (message = null dans le cas d'un clear). 
	 */
	public void messageDeleted(Message message);
	
	/**
	 * Le message a été modifié.
	 * @param message le message modifié.
	 */
	public void messageChanged(Message message);
}
