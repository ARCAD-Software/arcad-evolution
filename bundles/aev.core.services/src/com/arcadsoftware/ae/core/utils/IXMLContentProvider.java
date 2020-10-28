package com.arcadsoftware.ae.core.utils;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public interface IXMLContentProvider {
	public String getRootName();
	public void provide(Document document, Element rootNode);
	public String getEncoding();
	
	default Element addElement(Document document, Node node, String elementName) {
		final Element element = document.createElement(elementName);
		node.appendChild(element);
		return element;
	}
}
