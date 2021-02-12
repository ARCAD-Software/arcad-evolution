/*
 * Cr�� le 5 mai 04
 *
 */
package com.arcadsoftware.aev.core.messages;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.time.FastDateFormat;

import com.arcadsoftware.aev.core.tools.CoreLabels;
import com.arcadsoftware.aev.core.tools.StringTools;

/**
 * Description d'un message � collectionner.
 *
 * @author MD
 */
public class Message implements IMessageDetails {

	// Formater utilisé pour les details datant les messages.
	private static final FastDateFormat dateForMessage = FastDateFormat.getInstance("HH:mm:ss (dd.MM.yyyy)"); //$NON-NLS-1$
	public static final int STYLE_BLOCK = 0x04;
	public static final int STYLE_LOG = 0x01;
	public static final int STYLE_NONE = 0;

	public static final int STYLE_SHOW = 0x02;

	private String command;
	protected Date creationDate = new Date();
	private List<MessageDetail> details = new ArrayList<>();
	protected boolean isFixedType = false;
	private int level;
	private int maxDetailsType;
	protected int style = STYLE_NONE;

	/**
	 *
	 */
	public Message(final String command) {
		super();
		setCommand(command);
		level = MessageManager.LEVEL_DEVELOPMENT;
		maxDetailsType = MessageDetail.COMPLETION;
	}

	public Message(final String command, final int type, final int level, final String description) {
		this(command, type, level, description, false);
	}

	public Message(final String command, final int type, final int level, final String description,
			final boolean fixedType) {
		this(command, type, level, description, fixedType, STYLE_NONE);
	}

	public Message(final String command, final int type, final int level, final String description,
			final boolean fixedType, final int style) {
		this(command);
		if (!StringTools.isEmpty(description)) {
			addDetail(type, description);
		}
		maxDetailsType = type;
		this.level = level;
		isFixedType = fixedType;
		this.style = style;
	}

	/**
	 * Cr�ation d'un message avec un d�tail (Type,description) et de niveau D�veloppement.
	 *
	 * @param command
	 *            le texte de la commande (titre) associ� au message.
	 * @param type
	 *            le type du d�tail (Completion, diagnostic, warning, error).
	 * @param description
	 *            le contenu du d�tail.
	 */
	public Message(final String command, final int type, final String description) {
		this(command);
		addDetail(type, description);
		maxDetailsType = type;
	}

	/**
	 * Ajoute un d�tail indiquant la date courante.
	 */
	public void addDate() {
		addDetail(MessageDetail.DIAGNOSTIC, dateForMessage.format(new Date()));
	}

	/**
	 * Ajoute un d�tail au message.<br/>
	 * Cette methode retourne le message lui-m�me, de telle sorte que les addDetails peuvent se succeder.
	 *
	 * @param type
	 * @param description
	 */
	public Message addDetail(final int type, final String description) {
		addMessageDetail(type, description);
		return this;
	}

	/**
	 * Ajoute des d�tails relatifs � l'exception.
	 *
	 * @param e
	 * @return
	 */
	public Message addException(final Exception e) {
		return addThrowable(e);
	}

	protected MessageDetail addMessageDetail(final int type, final String description) {
		final MessageDetail messageDetail = new MessageDetail(this, type, description);
		details.add(messageDetail);
		if (!isFixedType && type > maxDetailsType) {
			maxDetailsType = type;
		}
		MessageManager.fireMessageChanged(this);
		return messageDetail;
	}

	/**
	 * Ajoute des d�tails relatifs � l'exception.
	 *
	 * @param throwable
	 */
	public Message addThrowable(final Throwable throwable) {
		String m = throwable.getLocalizedMessage();
		if (m == null) {
			m = throwable.getMessage();
		}
		if (m == null) {
			m = CoreLabels.resString("MessageManager.ExceptionThrown"); //$NON-NLS-1$
		}
		addDetail(MessageDetail.EXCEPTION, m);
		final String s = MessageManager.printStackTrace(throwable);
		if (s != null && s.length() > 0) {
			addDetail(MessageDetail.EXCEPTION, s);
		}
		addDate();
		MessageManager.fireMessageChanged(this);
		return this;
	}

	/**
	 * Supprime tous les d�tails.
	 */
	public void clear() {
		details.clear();
		maxDetailsType = 0;
		MessageManager.fireMessageChanged(this);
	}

	public int detailCount() {
		return details.size();
	}

	/**
	 * @return
	 */
	public String getCommand() {
		return command;
	}

	@Override
	public String getDescription() {
		return getCommand();
	}

	public MessageDetail getDetailAt(final int index) {
		return details.get(index);
	}

	/**
	 * @return
	 */
	public List<MessageDetail> getDetails() {
		return details;
	}

	/**
	 * @return
	 */
	public int getLevel() {
		return level;
	}

	@Override
	public int getMaxDetailsType() {
		return maxDetailsType;
	}

	@Override
	public List<? extends IMessageDetails> getMessageDetails() {
		return details;
	}

	/**
	 * @returns the style associated with the message, {@link StatusManager#NONE} by default
	 * @see StatusManager#handle(org.eclipse.core.runtime.IStatus, int)
	 */
	public int getStyle() {
		return style;
	}

	/**
	 * @param showParam
	 * @return
	 */
	public boolean isVisibleTo(final int showParam) {
		for (final MessageDetail detail : details) {
			if ((showParam & detail.getType()) != 0) {
				return true;
			}
		}
		return false;
	}

	public void print() {
		MessageManager.logError("COMMAND : " + toString()); //$NON-NLS-1$
		for (int i = 0; i < detailCount(); i++) {
			getDetailAt(i).print();
		}
	}

	public void removeDetailAt(final int index) {
		details.remove(index);
		if (!isFixedType) {
			for (int i = 0; i < details.size(); i++) {
				// <FM number="2014/00293" version="10.03.00" date="Mar. 28 2014" user="FPO">
				if (i == 0) {
					maxDetailsType = details.get(i).getType();
				}
				// </FM>
				if (details.get(i).getType() > maxDetailsType) {
					maxDetailsType = details.get(i).getType();
				}
			}
		}
		MessageManager.fireMessageChanged(this);
	}

	/**
	 * @param string
	 */
	public void setCommand(final String string) {
		command = string;
	}

	/**
	 * @param list
	 */
	public void setDetails(final List<MessageDetail> list) {
		details = list;
		maxDetailsType = 0;
		if (list != null && !isFixedType) {
			for (final MessageDetail element : list) {
				if (element.getType() > maxDetailsType) {
					maxDetailsType = element.getType();
				}
			}
		}
		MessageManager.fireMessageChanged(this);
	}

	/**
	 * @param i
	 */
	public void setLevel(final int i) {
		level = i;
	}

	public void setMaxDetailsType(final int maxDetailsType) {
		this.maxDetailsType = maxDetailsType;
	}

	/**
	 * Retourne la liste des d�tails du message
	 * 
	 * @return
	 */
	public MessageDetail[] toArray() {
		if (details == null) {
			return new MessageDetail[0];
		} else {
			return details.toArray(new MessageDetail[details.size()]);
		}
	}

	@Override
	public String toString() {
		final char ln = Character.LINE_SEPARATOR;
		final StringBuilder buffer = new StringBuilder(String.format(
				"[%1$s] %2$s",
				// FormatDateTools.getFormattedDateTime(creationDate),
				new SimpleDateFormat("yyyy-MM-dd HH.mm.ss").format(creationDate),
				getCommand()));

		for (final MessageDetail detail : details) {
			buffer.append(ln)
					.append("   ") //$NON-NLS-1$
					.append(detail.toString());
		}

		return buffer.toString();
	}

}
