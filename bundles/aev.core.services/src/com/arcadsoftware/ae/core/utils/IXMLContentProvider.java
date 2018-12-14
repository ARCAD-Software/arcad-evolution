package com.arcadsoftware.ae.core.utils;

import org.dom4j.Element;

public interface IXMLContentProvider {
	public String getRootName();
	public void provide(Element rootNode);
	public String getEncoding();
}
