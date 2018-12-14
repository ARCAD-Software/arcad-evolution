/*
 * Créé le 5 mai 04
 *
 */
package com.arcadsoftware.aev.core.messages;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.arcadsoftware.aev.core.tools.CoreLabels;

/**
 * Description d'un message à collectionner.
 * 
 * @author MD
 */
public class Message {

	// Formater utilisé pour les details datant les messages.
	private static final SimpleDateFormat dateForMessage = new SimpleDateFormat("HH:mm:ss (dd.MM.yyyy)"); //$NON-NLS-1$

	@SuppressWarnings("unchecked")
	private ArrayList details = new ArrayList();
	private String command;
	private int maxDetailsType;
	private int level;

	public Message(String command) {
		super();
		setCommand(command);
		level = MessageManager.LEVEL_DEVELOPMENT;
		maxDetailsType = MessageDetail.COMPLETION;
	}

	/**
	 * Création d'un message avec un détail (Type,description) et de niveau
	 * Développement.
	 * 
	 * @param command
	 *            le texte de la commande (titre) associé au message.
	 * @param type
	 *            le type du détail (Completion, diagnostic, warning, error).
	 * @param description
	 *            le contenu du détail.
	 */
	public Message(String command, int type, String description) {
		this(command);
		addDetail(type, description);
		maxDetailsType = type;
	}

	public Message(String command, int type, int level, String description) {
		this(command);
		addDetail(type, description);
		maxDetailsType = type;
		this.level = level;
	}

	public String getCommand() {
		return command;
	}

	public void setCommand(String string) {
		command = string;
	}

	@SuppressWarnings("unchecked")
	public ArrayList getDetails() {
		return details;
	}

	/**
	 * Retourne la liste des détails du message
	 * 
	 * @return MessageDetail[]
	 */
	@SuppressWarnings("unchecked")
	public MessageDetail[] toArray() {
		if (details == null) {
			return new MessageDetail[0];
		}
		return (MessageDetail[]) details.toArray(new MessageDetail[details.size()]);
	}

	@SuppressWarnings("unchecked")
	public void setDetails(ArrayList list) {
		details = list;
		maxDetailsType = 0;
		if (list != null)
			for (int i = 0; i < list.size(); i++) {
				if (((MessageDetail) list.get(i)).getType() > maxDetailsType)
					maxDetailsType = ((MessageDetail) list.get(i)).getType();
			}
		MessageManager.fireMessageChanged(this);
	}

	/**
	 * Ajoute un détail au message.<br/>
	 * Cette methode retourne le message lui-même, de telle sorte que les
	 * addDetails peuvent se succeder.
	 * 
	 * @param type
	 * @param description
	 */
	@SuppressWarnings("unchecked")
	public Message addDetail(int type, String description) {
		details.add(new MessageDetail(this, type, description));
		if (type > maxDetailsType)
			maxDetailsType = type;
		MessageManager.fireMessageChanged(this);
		return this;
	}

	/**
	 * Ajoute des détails relatifs à l'exception.
	 * 
	 * @param e
	 * @return Message
	 */
	public Message addException(Exception e) {
		return addThrowable(e);
	}

	/**
	 * Ajoute des détails relatifs à l'exception.
	 * 
	 * @param throwable
	 */
	public Message addThrowable(Throwable throwable) {
		String m = throwable.getLocalizedMessage();
		if (m == null)
			m = throwable.getMessage();
		if (m == null)
			m = CoreLabels.resString("MessageManager.ExceptionThrown"); //$NON-NLS-1$
		addDetail(MessageDetail.EXCEPTION, m);
		try {
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			throwable.printStackTrace(new PrintWriter(out, true));
			out.flush();
			String s = out.toString();
			out.close();
			if ((s != null) && (s.length() > 0)) {
				addDetail(MessageDetail.EXCEPTION, s);
			}
		} catch (IOException e1) {
			addDetail(MessageDetail.EXCEPTION, e1.getLocalizedMessage());
		}
		addDate();
		MessageManager.fireMessageChanged(this);
		return this;
	}

	/**
	 * Ajoute un détail indiquant la date courante.
	 * 
	 */
	public void addDate() {
		addDetail(MessageDetail.DIAGNOSTIC, dateForMessage.format(new Date()));
	}

	/**
	 * Supprime tous les détails.
	 * 
	 */
	public void clear() {
		details.clear();
		maxDetailsType = 0;
		MessageManager.fireMessageChanged(this);
	}

	public MessageDetail getDetailAt(int index) {
		return (MessageDetail) details.get(index);
	}

	public void removeDetailAt(int index) {
		details.remove(index);
		if (details != null)
			for (int i = 0; i < details.size(); i++) {
				if (((MessageDetail) details.get(i)).getType() > maxDetailsType)
					maxDetailsType = ((MessageDetail) details.get(i)).getType();
			}
		MessageManager.fireMessageChanged(this);
	}

	public int detailCount() {
		return details.size();
	}

	public void print() {
		System.err.println("COMMAND : " + getCommand());//$NON-NLS-1$ 
		for (int i = 0; i < detailCount(); i++) {
			getDetailAt(i).print();
		}
	}

	/**
	 * @return int
	 */
	public int getMaxDetailsType() {
		return maxDetailsType;
	}

	/**
	 * @param showParam
	 * @return boolean
	 */
	public boolean isVisibleTo(int showParam) {
		for (int j = 0; j < details.size(); j++) {
			if ((showParam & ((MessageDetail) details.get(j)).getType()) != 0) {
				return true;
			}
		}
		return false;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int i) {
		level = i;
	}
}
