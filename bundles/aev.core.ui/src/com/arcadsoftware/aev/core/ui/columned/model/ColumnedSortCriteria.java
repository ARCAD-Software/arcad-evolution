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
public class ColumnedSortCriteria extends AbstractColumnedCriteria {

	private String sortOrder = StringTools.EMPTY;

	public ColumnedSortCriteria() {
		super();
	}

	public ColumnedSortCriteria(final int id, final String firstColumnName) {
		super();
		setId(id);
		setColumnName(firstColumnName);
		setColumnIndex(0);
		setSortOrder(ColumnedSortCriteriaList.ASCENDING);
	}

	public void assignTo(final ColumnedSortCriteria target) {
		target.setId(id);
		target.setColumnName(columnName);
		target.setColumnIndex(columnIndex);
		target.setSortOrder(sortOrder);
	}

	public ColumnedSortCriteria duplicate() {
		final ColumnedSortCriteria result = new ColumnedSortCriteria(id, columnName);
		result.setColumnIndex(columnIndex);
		result.setSortOrder(sortOrder);
		return result;
	}

	/**
	 * @return Renvoie sortOrder.
	 */
	public String getSortOrder() {
		return sortOrder;
	}

	/**
	 * @param sortOrder
	 *            sortOrder � d�finir.
	 */
	public void setSortOrder(final String sortOrder) {
		this.sortOrder = sortOrder;
	}
}
