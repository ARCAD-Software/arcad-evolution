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

import com.arcadsoftware.aev.core.tools.CoreLabels;
import com.arcadsoftware.aev.core.tools.StringTools;

/**
 * Description d'un message � collectionner.
 * 
 * @author MD
 */
public class Message implements IMessageDetails {

	public static final int STYLE_NONE = 0;
	public static final int STYLE_LOG = 0x01;
	public static final int STYLE_SHOW = 0x02;
	public static final int STYLE_BLOCK = 0x04;
	
	// Formater utilisé pour les details datant les messages.
	private static final SimpleDateFormat dateForMessage = new SimpleDateFormat("HH:mm:ss (dd.MM.yyyy)"); //$NON-NLS-1$

	private ArrayList<MessageDetail> details = new ArrayList<MessageDetail>();
	private String command;
	private int maxDetailsType;
	private int level;
	protected boolean isFixedType = false;
	protected Date creationDate = new Date();
	protected int style = STYLE_NONE;
	
	/**
	 * 
	 */
	public Message(String command) {
		super();
		setCommand(command);
		level = MessageManager.LEVEL_DEVELOPMENT;
		maxDetailsType = MessageDetail.COMPLETION;
	}
	
	/**
	 * Cr�ation d'un message avec un d�tail (Type,description) et de
	 * niveau D�veloppement.
	 *
	 * @param command le texte de la commande (titre) associ� au message.
	 * @param type le type du d�tail (Completion, diagnostic, warning, error).
	 * @param description le contenu du d�tail.
	 */
	public Message(String command,int type,String description) {
		this(command);
		addDetail(type,description);
		maxDetailsType = type;
	}	

	public Message(String command,int type,int level, String description, boolean fixedType, int style) {
		this(command);
		if(!StringTools.isEmpty(description))
			addDetail(type,description);
		maxDetailsType = type;
		this.level = level;
		this.isFixedType = fixedType;
		this.style = style;
	}	
	public Message(String command,int type,int level, String description, boolean fixedType) {
		this(command, type, level, description, fixedType, STYLE_NONE);
	}

	public Message(String command,int type,int level, String description) {
		this(command, type, level, description, false);
	}

	/**
	 * @return
	 */
	public String getCommand() {
		return command;
	}

	/**
	 * @param string
	 */
	public void setCommand(String string) {
		command = string;
	}

	/**
	 * @return
	 */
	public ArrayList<MessageDetail> getDetails() {
		return details;
	}

	/**
	 * Retourne la liste des d�tails du message
	 * @return
	 */
	public MessageDetail[] toArray() {
		if (details == null) {
			return new MessageDetail[0];
		} else {
			return (MessageDetail[])details.toArray(new MessageDetail[details.size()]);
		}
	}

	/**
	 * @param list
	 */
	public void setDetails(ArrayList<MessageDetail> list) {
		details = list;
		maxDetailsType = 0;
		if (list != null && !isFixedType){
			for (int i = 0; i < list.size(); i++) {
				if (list.get(i).getType() > maxDetailsType)
					maxDetailsType = ((MessageDetail)list.get(i)).getType();
			}
		}
		MessageManager.fireMessageChanged(this);
	}

	protected MessageDetail addMessageDetail(int type,String description){
		MessageDetail messageDetail = new MessageDetail(this,type,description);	
		details.add(messageDetail);	
		if (!isFixedType && type > maxDetailsType)
			maxDetailsType = type;
		MessageManager.fireMessageChanged(this);
		return messageDetail;
	}
	/**
	 * Ajoute un d�tail au message.<br/>
	 * Cette methode retourne le message lui-m�me, de telle sorte que les addDetails peuvent se
	 * succeder.
	 * 
	 * @param type
	 * @param description
	 */
	public Message addDetail(int type,String description){
		addMessageDetail(type, description);
		return this;
	}

	/**
	 * Ajoute des d�tails relatifs � l'exception.
	 * 
	 * @param e
	 * @return
	 */
	public Message addException(Exception e){
		return addThrowable(e);
	}

	/**
	 * Ajoute des d�tails relatifs � l'exception.
	 * 
	 * @param throwable
	 */
	public Message addThrowable(Throwable throwable) {
		String m = throwable.getLocalizedMessage();
		if (m == null)
			m = throwable.getMessage();
		if (m == null)
			m = CoreLabels.resString("MessageManager.ExceptionThrown");  //$NON-NLS-1$
		addDetail(MessageDetail.EXCEPTION,m);
		try {
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			throwable.printStackTrace(new PrintWriter(out,true));
			out.flush();
			String s = out.toString();
			out.close();
			if ((s != null) && (s.length() > 0)) {
				addDetail(MessageDetail.EXCEPTION,s);
			}
		} catch (IOException e1) {
			addDetail(MessageDetail.EXCEPTION,e1.getLocalizedMessage());
		}
		addDate();
		MessageManager.fireMessageChanged(this);
		return this;
	}
	
	/**
	 * Ajoute un d�tail indiquant la date courante.
	 *
	 */
	public void addDate() {
		addDetail(MessageDetail.DIAGNOSTIC,dateForMessage.format(new Date()));
	}
	
	/** 
	 * Supprime tous les d�tails.
	 *
	 */
	public void clear(){
		details.clear();	
		maxDetailsType = 0;
		MessageManager.fireMessageChanged(this);
	}	

	public MessageDetail getDetailAt(int index) {
		return (MessageDetail)details.get(index);
	}

	public void removeDetailAt(int index) {
		details.remove(index);		
		if (details != null && !isFixedType){
			for (int i = 0; i < details.size(); i++) {
				//<FM number="2014/00293" version="10.03.00" date="Mar. 28 2014" user="FPO">
				if (i == 0)
					maxDetailsType = details.get(i).getType();
				//</FM>
				if (((MessageDetail)details.get(i)).getType() > maxDetailsType)
					maxDetailsType = details.get(i).getType();
			}
		}
		MessageManager.fireMessageChanged(this);
	}	

	public int detailCount(){
		return details.size();
	}

	public void print(){
		System.err.println("COMMAND : " + this.toString());		//$NON-NLS-1$ 
		for (int i=0;i<detailCount();i++) {
			getDetailAt(i).print();
		}
	}

	@Override
	public String toString() {
		char ln = Character.LINE_SEPARATOR;
		StringBuffer buffer = new StringBuffer(String.format(
			"[%1$s] %2$s", 
			//FormatDateTools.getFormattedDateTime(creationDate), 
			new SimpleDateFormat("yyyy-MM-dd HH.mm.ss").format(creationDate), 
			getCommand()
		));
		
		for(MessageDetail detail : details){
			buffer	.append(ln)
					.append("   ")								//$NON-NLS-1$
					.append(detail.toString());
		}
		
		return buffer.toString();
	}
	
	@Override
	public int getMaxDetailsType() {
		return maxDetailsType;
	}

	public void setMaxDetailsType(int maxDetailsType) {
		this.maxDetailsType = maxDetailsType;
	}

	/**
	 * @param showParam
	 * @return
	 */
	public boolean isVisibleTo(int showParam) {
		for (int j = 0; j < details.size(); j++) {
			if ((showParam & details.get(j).getType()) != 0) {
				return true;
			}
		}
		return false;
	}

	/**
	 * @return
	 */
	public int getLevel() {
		return level;
	}

	/**
	 * @param i
	 */
	public void setLevel(int i) {
		level = i;
	}

	@Override
	public List<? extends IMessageDetails> getMessageDetails() {
		return details;
	}

	@Override
	public String getDescription() {
		return command;
	}	
	
	/**
	 * @returns the style associated with the message, {@link StatusManager#NONE} by default
	 * @see StatusManager#handle(org.eclipse.core.runtime.IStatus, int)
	 */
	public int getStyle() {
		return style;
	}

}
