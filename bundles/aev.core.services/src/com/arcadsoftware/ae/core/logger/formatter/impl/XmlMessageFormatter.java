package com.arcadsoftware.ae.core.logger.formatter.impl;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.arcadsoftware.ae.core.logger.formatter.AbstractMessageFormatter;
import com.arcadsoftware.ae.core.logger.messages.AbstractMessage;
import com.arcadsoftware.ae.core.logger.messages.IMessageXmlPartProvider;
import com.arcadsoftware.ae.core.logger.messages.MessageData;
import com.arcadsoftware.ae.core.logger.messages.Messages;

public class XmlMessageFormatter extends AbstractMessageFormatter {
	
	SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd-hh:mm:SS:sss"); 
	
	private static final String ROOTNAME = "output";		
	private static final String MSG_ROOTNAME = "message";	
	private static final String ATTR_SERVICE = "service";
	private static final String ATTR_TYPE = "type";	
	private static final String ATTR_DATE = "date";	
	private static final String ITEM_TEXT = "text";	
	private static final String ITEM_DATASET = "dataset";	
	private static final String ATTR_COUNT = "count";	
	private static final String ITEM_DATA = "data";	
	private static final String ATTR_KEY = "key";	
	private static final String ATTR_VALUE = "value";	

	private Document document;
	
	
	private Element createRoot() { 
		document = DocumentHelper.createDocument();
		document.addDocType(ROOTNAME,null,null);
		document.setXMLEncoding("ISO-8859-1");
		Element root = document.addElement(ROOTNAME);
		return root;	
	} 		
		
	private Element createMessageRoot(AbstractMessage message,Element root) { 		
		Element msgElement = root.addElement(MSG_ROOTNAME);
		msgElement.addAttribute(ATTR_SERVICE,message.getServiceName());		
		msgElement.addAttribute(ATTR_TYPE,message.getMessageType());	
		msgElement.addAttribute(ATTR_DATE,df.format(new Date()));		
		if (message instanceof IMessageXmlPartProvider) {
			((IMessageXmlPartProvider)message).setXMLHeaderPart(msgElement);
		}		
		return msgElement;	
	}
	
	private void createMessage(AbstractMessage message,Element root) {
		Element messageRoot = createMessageRoot(message,root);
		if (!message.getMessageText().equals("")){
			Element e = messageRoot.addElement(ITEM_TEXT);
			e.setText(message.getMessageText());
		}
		int count = message.getMessageCount();
		if (count>0) {
			Element dataset = messageRoot.addElement(ITEM_DATASET);
			dataset.addAttribute(ATTR_COUNT,String.valueOf(count));
			for (int i=0;i<count;i++) {
				MessageData msg = message.messageDataAt(i);
				Element data = dataset.addElement(ITEM_DATA);
				data.addAttribute(ATTR_KEY,msg.getKey());
				data.addAttribute(ATTR_VALUE,msg.getData());			
			}		
		}
		if (message instanceof IMessageXmlPartProvider) {
			((IMessageXmlPartProvider)message).setXMLPart(messageRoot);
		}		
	}

	public void format(AbstractMessage message,Element root) {
		createMessage(message,root);	
	}
			
	public String format(Messages messages) {
		int count = messages.messageCount();
		Element root = createRoot();
		for (int i=0;i<count;i++){
			AbstractMessage msg = messages.messageAt(i);
			format(msg,root);
		}
		String data = document.asXML(); 
		document.clearContent();
		return data;
		
	}	

	public static boolean isStatusOk(String message,String type) {
		try {   		    		    		
			Document doc = DocumentHelper.parseText(message);
			Element root = doc.getRootElement();
			
			Element msgNode = root.element(MSG_ROOTNAME);
			if (msgNode!=null) {
				Attribute attr = msgNode.attribute(ATTR_TYPE);
				if (attr!=null) {
					if (attr.getValue().equals(type)) {
						Attribute attrValue = msgNode.attribute("status");
						if (attrValue!=null) {
							return attrValue.getValue().equals(String.valueOf(true));
						}
					}
				}				
			}
		} catch (DocumentException e) {
			e.printStackTrace();
		}			
		return false;
	}
	
	
}

