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

import java.util.Optional;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public interface IXMLContentParser {
	default String getAttributeValue(final Node e, final String name) {
		if (e instanceof Element) {
			return Optional.ofNullable(((Element) e).getAttribute(name)).orElse(""); //$NON-NLS-1$
		} else {
			return ""; //$NON-NLS-1$
		}
	}

	default Optional<Node> getNode(final Node e, final String name) {
		final NodeList nodes = e.getChildNodes();
		if (nodes != null) {
			for (int i = 0; i < nodes.getLength(); i++) {
				final Node node = nodes.item(i);
				if (node.getNodeName().equalsIgnoreCase(name)) {
					return Optional.of(node);
				}
			}
		}

		return Optional.empty();
	}

	default String getNodeValue(final Node e, final String name) {
		return getNode(e, name).map(Node::getTextContent).orElse("");
	}

	String getRootName();

	boolean parse(Element node);
}
