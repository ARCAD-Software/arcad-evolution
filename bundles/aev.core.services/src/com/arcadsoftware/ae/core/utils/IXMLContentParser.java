package com.arcadsoftware.ae.core.utils;

import org.dom4j.Element;



public interface IXMLContentParser {
	public String getRootName();
	public boolean parse(Element node);
}
