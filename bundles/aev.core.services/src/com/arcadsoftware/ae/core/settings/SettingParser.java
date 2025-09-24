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
package com.arcadsoftware.ae.core.settings;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.arcadsoftware.ae.core.utils.IXMLContentParser;

public class SettingParser implements IXMLContentParser {

	private final Setting setting;

	public SettingParser() {
		setting = new Setting();
	}

	@Override
	public String getRootName() {
		return "configuration";
	}

	public Setting getSetting() {
		return setting;
	}

	@Override
	public boolean parse(final Element node) {
		final String category = getAttributeValue(node, "category");
		Category c = setting.getCategoryByName(category);
		if (c == null) {
			c = new Category();
			c.setLabel(category);
			setting.addCategory(c);
		}
		final SectionId section = new SectionId();
		final String sectionId = getAttributeValue(node, "sid");
		final String label = getAttributeValue(node, "label");
		final String help = getAttributeValue(node, "help");
		section.setId(sectionId);
		section.setLabel(label);
		section.setHelp(help);
		final List<ConsoleField> fields = new ArrayList<>();
		final NodeList nodes = node.getChildNodes();
		for (int i = 0; i < nodes.getLength(); i++) {
			final Node child = nodes.item(i);
			ConsoleField field = null;
			final String id = getAttributeValue(child, "id");
			final String pLabel = getAttributeValue(child, "label");
			final String pHelp = getAttributeValue(child, "help");
			final String pDefault = getAttributeValue(child, "default");

			final String nodeName = child.getNodeName();
			if (nodeName.equalsIgnoreCase("property")) {
				field = new ConsoleProperty();
				final ConsoleProperty cp = (ConsoleProperty) field;
				final String data = child.getTextContent();
				cp.setId(id);
				cp.setDefaultvalue(pDefault);
				cp.setValue(data);
				final NodeList items = child.getChildNodes();
				for (final int j = 0; j < items.getLength(); i++) {
					final Node item = items.item(j);
					if (item.getNodeName().equalsIgnoreCase("item")) {
						cp.getList().add(item.getTextContent());
					}
				}
			} else if (nodeName.equalsIgnoreCase("text")) {
				field = new ConsoleText();
			} else if (nodeName.equalsIgnoreCase("set")) {
				field = new ConsoleSet();
			}
			if (field != null) {
				field.setLabel(pLabel);
				field.setHelp(pHelp);
				fields.add(field);
			}
		}
		final Form form = new Form(section, fields);
		c.getList().add(form);
		return true;
	}
}
