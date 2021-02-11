/*
 * Cr�� le 5 mai 04
 *
 * Pour changer le mod�le de ce fichier g�n�r�, allez � :
 * Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
 */
package com.arcadsoftware.aev.core.messages;

import java.util.ArrayList;
import java.util.List;

/**
 * @author MD
 */
public class MessageDetail implements IMessageDetails {

	/**
	 * Message indiquant qu'une op�ration c'est bien termin�e (ou commence...)
	 */
	public static final int COMPLETION = 1;

	/**
	 * Message de type diagnostic, message d'information.
	 */
	public static final int DIAGNOSTIC = 2;

	/**
	 * Message de type Erreur
	 */
	public static final int ERROR = 8;

	/**
	 * Message de type Exception.
	 */
	public static final int EXCEPTION = 16;

	private static final List<? extends IMessageDetails> messageDetails = new ArrayList<MessageDetail>(0);

	/**
	 * Message de type Avertissement
	 */
	public static final int WARNING = 4;

	private String description;
	private Message parentMessage;
	private int type;

	/**
	 * Cr�ation du d�tail du message � partir d'une exception.
	 * 
	 * @param e
	 *            exception originelle.
	 */
	public MessageDetail(final Exception e) {
		super();
		setType(EXCEPTION);
		setDescription(e.getMessage());
	}

	public MessageDetail(final Message parent, final int type, final String description) {
		super();
		parentMessage = parent;
		setType(type);
		setDescription(description);
	}

	@Override
	public String getDescription() {
		return description;
	}

	@Override
	public int getMaxDetailsType() {
		return getType();
	}

	public Message getMessage() {
		return parentMessage;
	}

	@Override
	public List<? extends IMessageDetails> getMessageDetails() {
		return messageDetails;
	}

	public int getType() {
		return type;
	}

	public String getTypeString() {
		switch (getType()) {
		case EXCEPTION:
			return "Exception";
		case ERROR:
			return "Error";
		case WARNING:
			return "Warning";
		case COMPLETION:
			return "Completion";
		default:
			return "Diagnostic";
		}
	}

	public void print() {
		MessageManager.logError("Detail : " + toString());//$NON-NLS-1$
	}

	public void setDescription(final String string) {
		description = string;
	}

	public void setType(final int data) {
		type = data;
	}

	@Override
	public String toString() {
		return String.format("[%1$s] %2$s", getTypeString(), getDescription()); //$NON-NLS-1$
	}
}
