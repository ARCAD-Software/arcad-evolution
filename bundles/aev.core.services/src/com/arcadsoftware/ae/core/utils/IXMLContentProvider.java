package com.arcadsoftware.ae.core.utils;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public interface IXMLContentProvider {
	default Element addElement(final Document document, final Node node, final String elementName) {
		final Element element = document.createElement(elementName);
		node.appendChild(element);
		return element;
	}

	String getEncoding();

	String getRootName();

	void provide(Document document, Element rootNode);
}
