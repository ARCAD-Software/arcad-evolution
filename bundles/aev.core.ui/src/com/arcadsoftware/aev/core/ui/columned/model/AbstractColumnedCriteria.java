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
package com.arcadsoftware.aev.core.ui.columned.model;

import com.arcadsoftware.aev.core.tools.StringTools;

/**
 * @author dlelong
 */
public abstract class AbstractColumnedCriteria {

	protected int columnIndex = 0;
	protected String columnName = StringTools.EMPTY;
	protected int id = 0;

	public AbstractColumnedCriteria() {
		super();
	}

	public int getColumnIndex() {
		return columnIndex;
	}

	public String getColumnName() {
		return columnName;
	}

	public int getId() {
		return id;
	}

	public void setColumnIndex(final int columnIndex) {
		this.columnIndex = columnIndex;
	}

	public void setColumnName(final String columnName) {
		this.columnName = columnName;
	}

	public void setId(final int id) {
		this.id = id;
	}
}
