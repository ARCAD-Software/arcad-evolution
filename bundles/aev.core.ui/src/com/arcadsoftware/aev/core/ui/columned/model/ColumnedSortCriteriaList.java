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

import com.arcadsoftware.aev.core.ui.tools.CoreUILabels;

/**
 * @author dlelong
 */
public class ColumnedSortCriteriaList extends AbstractColumnedCriteriaList {

	private static final String[] SORT_ORDER_ARRAY = { CoreUILabels.resString("combo.clm.AscendingSort"), //$NON-NLS-1$
			CoreUILabels.resString("combo.clm.DescendingSort") }; //$NON-NLS-1$
	
	public static final String ASCENDING = SORT_ORDER_ARRAY[0];
	public static final String DESCENDING = SORT_ORDER_ARRAY[1];
	
	public static String[] getSortOrderArray() {
		return SORT_ORDER_ARRAY;
	}
	
	public ColumnedSortCriteriaList(final ArcadColumns referenceColumns) {
		this(referenceColumns, false);
	}

	/**
	 * Constructor
	 */
	public ColumnedSortCriteriaList(final ArcadColumns referenceColumns, final boolean withInitData) {
		super(referenceColumns, withInitData);
	}

	/**
	 * Add a new criterion to the collection of criteria
	 */
	public void addCriterion() {
		final ColumnedSortCriteria criterion = new ColumnedSortCriteria(criteria.size() + 1, columnNames[0]);
		criteria.add(criteria.size(), criterion);
	}

	public ColumnedSortCriteriaList duplicate() {
		final ColumnedSortCriteriaList result = new ColumnedSortCriteriaList(referenceColumns);
		for (int i = 0; i < criteria.size(); i++) {
			final ColumnedSortCriteria c = (ColumnedSortCriteria) getItems(i);
			result.add(c.duplicate());
		}
		return result;
	}

	/*
	 * Initialize the table data. Create COUNT criteria and add them them to the collection of criteria
	 */
	@Override
	protected void initData() {
		ColumnedSortCriteria criterion;
		criterion = new ColumnedSortCriteria(1, columnNames[0]);
		criteria.add(criterion);
	}

	public void orderCriterion() {
		final ColumnedSortCriteriaList temp = new ColumnedSortCriteriaList(referenceColumns);
		ColumnedSortCriteria tempCriteria;
		final int size = getSize();
		for (int i = 1; i <= size; i++) {
			for (int j = 1; j <= size; j++) {
				tempCriteria = (ColumnedSortCriteria) criteria.get(j - 1);
				if (tempCriteria.getId() == i) {
					temp.add(tempCriteria);
					break;
				}
			}
		}
		criteria = temp.getCriteria();
	}

	public void swap(final ColumnedSortCriteria criteria1, final ColumnedSortCriteria criteria2) {
		final ColumnedSortCriteria temp = new ColumnedSortCriteria();
		criteria2.assignTo(temp);
		criteria1.assignTo(criteria2);
		temp.assignTo(criteria1);
	}
}
