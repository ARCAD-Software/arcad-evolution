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

public class SectionId {

	String help;
	int icon;
	String id;
	String label;
	transient int order;

	public String getHelp() {
		return help;
	}

	public int getIcon() {
		return icon;
	}

	public String getId() {
		return id;
	}

	public String getLabel() {
		return label;
	}

	public int getOrder() {
		return order;
	}

	public void setHelp(final String help) {
		this.help = help;
	}

	public void setIcon(final int icon) {
		this.icon = icon;
	}

	public void setId(final String id) {
		this.id = id;
	}

	public void setLabel(final String label) {
		this.label = label;
	}

	public void setOrder(final int order) {
		this.order = order;
	}
}
