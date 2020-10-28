package com.arcadsoftware.ae.core.utils;

import java.util.Optional;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public interface IXMLContentParser {
	public String getRootName();
	public boolean parse(Element node);
	
	default String getAttributeValue(final Node e, final String name) {
		if(e instanceof Element) {
			return Optional.ofNullable(((Element)e).getAttribute(name)).orElse(""); //$NON-NLS-1$
		}
		else {
			return ""; //$NON-NLS-1$
		}
	}		
	
	default Optional<Node> getNode(final Node e, final String name) {
    	final NodeList nodes = e.getChildNodes();
    	if (nodes != null) {
    		for(int i = 0; i < nodes.getLength(); i++) {
    			final Node node = nodes.item(i);
    			if(node.getNodeName().equalsIgnoreCase(name)){
    				return Optional.of(node); //$NON-NLS-1$ 
    			}
    		}
    	}

    	return Optional.empty();
	}
	
	default String getNodeValue(final Node e, final String name) {
		return getNode(e, name).map(Node::getTextContent).orElse("");
	}
}
