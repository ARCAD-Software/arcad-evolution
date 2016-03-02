/*
 * Cr‚‚ le 13 mars 2007
 *
 */
package com.arcadsoftware.ae.core.logger.formatter.impl;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.arcadsoftware.ae.core.logger.formatter.AbstractMessageFormatter;
import com.arcadsoftware.ae.core.logger.messages.AbstractMessage;
import com.arcadsoftware.ae.core.logger.messages.MessageData;
import com.arcadsoftware.ae.core.logger.messages.Messages;



/**
 * Classe de formatage basic d'un message.<br>
 * C'est cette classe qui est utilis‚e par d‚faut pour le formatage des messages.
 * @author MD
 *
 */
public class BasicMessageFormatter extends AbstractMessageFormatter {

	SimpleDateFormat sp = new SimpleDateFormat("yyyyMMdd-hh:mm:ss:SSS"); 
    /**
     * M‚thode de cr‚ation de l'en-tˆte du message.
     * @return String : en-tˆte du message.
     */
    private String createHeader(AbstractMessage message) {
        StringBuffer sb = new StringBuffer();
        sb.append("[").append(sp.format(new Date())).append("]")
          .append("[").append(message.getServiceName()).append("]")
          .append("[").append(message.getMessageType()).append("]")                    
          .append(message.getMessageText());        
        return sb.toString();
    }
	
	public String format(AbstractMessage message) {
        StringBuffer sb = new StringBuffer(createHeader(message));
        sb.append("(");
        for (int i=0;i<message.getDatas().size();i++) {
        	MessageData md = (MessageData)message.getDatas().get(i);
            String key = md.getKey();
            String value = md.getData();
            sb.append(key).append("=").append(value).append(";");
        }
        sb.append(")");
        return sb.toString();
	}

	/* (non-Javadoc)
	 * @see com.arcadsoftware.serviceprovider.message.AbstractMessageFormatter#format(com.arcadsoftware.serviceprovider.message.Messages)
	 */
	public String format(Messages messages) {
		StringBuffer sb = new StringBuffer();		
		int count = messages.messageCount();
		for (int i=0;i<count;i++){
			AbstractMessage message = 
				messages.messageAt(i);			
			sb.append(format(message)).append("\n");
		}
		return sb.toString();
	}

}
