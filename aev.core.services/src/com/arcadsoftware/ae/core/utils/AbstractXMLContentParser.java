package com.arcadsoftware.ae.core.utils;

import org.dom4j.Attribute;
import org.dom4j.Element;

public abstract class AbstractXMLContentParser implements IXMLContentParser {

	protected String getAttributeValue(Element e, String name) {
    	Attribute attribute = e.attribute(name);
    	if (attribute!=null) 
    		return attribute.getStringValue();
    	return ""; //$NON-NLS-1$
	}		
	
	protected String getElementValue(Element e, String name) {
    	Element element = e.element(name);
    	if (element!=null) 
    		return element.getText();
    	return ""; //$NON-NLS-1$
	}	
}
