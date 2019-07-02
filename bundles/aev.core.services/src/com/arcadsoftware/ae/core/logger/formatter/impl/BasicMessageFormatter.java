package com.arcadsoftware.ae.core.logger.formatter.impl;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.arcadsoftware.ae.core.logger.formatter.AbstractMessageFormatter;
import com.arcadsoftware.ae.core.logger.messages.AbstractMessage;
import com.arcadsoftware.ae.core.logger.messages.MessageData;
import com.arcadsoftware.ae.core.logger.messages.Messages;

public class BasicMessageFormatter extends AbstractMessageFormatter {

	SimpleDateFormat sp = new SimpleDateFormat("yyyyMMdd-hh:mm:ss:SSS"); 

    private String createHeader(AbstractMessage message) {
        StringBuffer sb = new StringBuffer();
        sb.append('[').append(sp.format(new Date())).append(']')
          .append('[').append(message.getServiceName()).append(']')
          .append('[').append(message.getMessageType()).append(']')                    
          .append(message.getMessageText());        
        return sb.toString();
    }
	
	public String format(AbstractMessage message) {
        StringBuffer sb = new StringBuffer(createHeader(message));
        if(!message.getDatas().isEmpty()){
            sb.append('(');
            for (int i=0;i<message.getDatas().size();i++) {
                MessageData md = (MessageData)message.getDatas().get(i);
                String key = md.getKey();
                String value = md.getData();
                sb.append(key).append('=').append(value).append(';');
            }
            sb.append(')');
        }
        return sb.toString();
	}

	public String format(Messages messages) {
		StringBuffer sb = new StringBuffer();		
		int count = messages.messageCount();
		for (int i=0;i<count;i++){
			AbstractMessage message = 
				messages.messageAt(i);			
			sb.append(format(message)).append('\n');
		}
		return sb.toString();
	}

}
