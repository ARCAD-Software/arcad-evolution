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

import java.util.ArrayList;

public abstract class AbstractColumnedCriteriaList {

	public String[] columnNames;
	protected final int COUNT = 10;
	protected ArrayList<AbstractColumnedCriteria> criteria = new ArrayList<>(COUNT);

	protected ArcadColumns referenceColumns;

	public AbstractColumnedCriteriaList(final ArcadColumns referenceColumns, final boolean withInitData) {
		this.referenceColumns = referenceColumns;
		columnNames = referenceColumns.getUserNameValues();
		if (withInitData) {
			initData();
		}
	}

	public void add(final AbstractColumnedCriteria criterion) {
		criteria.add(criterion);
	}

	public String[] getColumnNames() {
		return columnNames;
	}

	/**
	 * Return the collection of criteria
	 */
	public ArrayList<AbstractColumnedCriteria> getCriteria() {
		return criteria;
	}

	/**
	 * Return the collection of criteria
	 */
	public AbstractColumnedCriteria getItems(final int index) {
		return criteria.get(index);
	}

	public int getSize() {
		return criteria.size();
	}

	protected abstract void initData();

	public void removeCriterion(final AbstractColumnedCriteria criterion) {
		criteria.remove(criterion);
	}

	public void setColumnNames(final String[] columnNames) {
		this.columnNames = columnNames;
	}
}
