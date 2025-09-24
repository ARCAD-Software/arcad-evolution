/*******************************************************************************
 * Copyright (c) 2025 ARCAD Software.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     ARCAD Software - initial API and implementation
 *******************************************************************************/
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
