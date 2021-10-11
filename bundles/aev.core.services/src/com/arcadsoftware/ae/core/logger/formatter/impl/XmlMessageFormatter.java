package com.arcadsoftware.ae.core.logger.formatter.impl;

import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.arcadsoftware.ae.core.logger.formatter.AbstractMessageFormatter;
import com.arcadsoftware.ae.core.logger.messages.AbstractMessage;
import com.arcadsoftware.ae.core.logger.messages.IMessageXmlPartProvider;
import com.arcadsoftware.ae.core.logger.messages.MessageData;
import com.arcadsoftware.ae.core.logger.messages.Messages;
import com.arcadsoftware.ae.core.utils.XMLUtils;

public class XmlMessageFormatter implements AbstractMessageFormatter {

	private static final String ATTR_COUNT = "count";

	private static final String ATTR_DATE = "date";
	private static final String ATTR_KEY = "key";
	private static final String ATTR_SERVICE = "service";
	private static final String ATTR_TYPE = "type";
	private static final String ATTR_VALUE = "value";
	private static final String ITEM_DATA = "data";
	private static final String ITEM_DATASET = "dataset";
	private static final String ITEM_TEXT = "text";
	private static final String MSG_ROOTNAME = "message";
	private static final String ROOTNAME = "output";

	public static boolean isStatusOk(final String message, final String type) {
		try {
			final Document doc = XMLUtils.loadXMLFromString(message);
			final NodeList msgNodes = doc.getElementsByTagName(MSG_ROOTNAME);
			if (msgNodes != null && msgNodes.getLength() > 0) {
				final Node node = msgNodes.item(0);
				if (node instanceof Element) {
					final Element msgNode = (Element) node;
					final String attr = msgNode.getAttribute(ATTR_TYPE);
					if (attr != null && attr.equals(type)) {
						final String attrValue = msgNode.getAttribute("status");
						if (attrValue != null) {
							return attrValue.equals(String.valueOf(true));
						}
					}
				}
			}
		} catch (final Exception e) {
			return false;			
		}
		return false;
	}

	SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd-hh:mm:SS:sss");

	private Document document;

	private void createMessage(final AbstractMessage message, final Element root) {
		final Element messageRoot = createMessageRoot(message, root);
		if (!message.getMessageText().equals("")) {
			final Element textElement = document.createElement(ITEM_TEXT);
			messageRoot.appendChild(textElement);
			textElement.setTextContent(message.getMessageText());
		}
		final int count = message.getMessageCount();
		if (count > 0) {
			final Element dataset = document.createElement(ITEM_DATASET);
			messageRoot.appendChild(dataset);
			dataset.setAttribute(ATTR_COUNT, String.valueOf(count));
			for (int i = 0; i < count; i++) {
				final MessageData msg = message.messageDataAt(i);
				final Element data = document.createElement(ITEM_DATA);
				dataset.appendChild(data);
				data.setAttribute(ATTR_KEY, msg.getKey());
				data.setAttribute(ATTR_VALUE, msg.getData());
			}
		}
		if (message instanceof IMessageXmlPartProvider) {
			((IMessageXmlPartProvider) message).setXMLPart(messageRoot);
		}
	}

	private Element createMessageRoot(final AbstractMessage message, final Element root) {
		final Element msgElement = document.createElement(MSG_ROOTNAME);
		root.appendChild(msgElement);
		msgElement.setAttribute(ATTR_SERVICE, message.getServiceName());
		msgElement.setAttribute(ATTR_TYPE, message.getMessageType());
		msgElement.setAttribute(ATTR_DATE, df.format(new Date()));
		if (message instanceof IMessageXmlPartProvider) {
			((IMessageXmlPartProvider) message).setXMLHeaderPart(msgElement);
		}
		return msgElement;
	}

	private Element createRoot() throws ParserConfigurationException {
		document = XMLUtils.createNewXMLDocument();
		final Element root = document.createElement(ROOTNAME);
		document.appendChild(root);
		return root;
	}

	public void format(final AbstractMessage message, final Element root) {
		createMessage(message, root);
	}

	@Override
	public String format(final Messages messages) {
		final int count = messages.messageCount();
		try {
			final Element root = createRoot();
			for (int i = 0; i < count; i++) {
				final AbstractMessage msg = messages.messageAt(i);
				format(msg, root);
			}

			final String data = XMLUtils.outputXMLDocumentToString(document, StandardCharsets.UTF_8.name());
			document = XMLUtils.createNewXMLDocument();
			return data;
		} catch (final Exception e) {			
			return "";
		}
	}

}
