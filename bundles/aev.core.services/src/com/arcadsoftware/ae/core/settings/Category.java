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

public class Category {

	String label;
	ArrayList<Form> list = new ArrayList<>();

	public String getLabel() {
		return label;
	}

	public ArrayList<Form> getList() {
		return list;
	}

	public void setLabel(final String label) {
		this.label = label;
	}

	public void setList(final ArrayList<Form> list) {
		this.list = list;
	}

}
