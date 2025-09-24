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
import com.arcadsoftware.aev.core.ui.tools.CoreUILabels;

/**
 * @author dlelong
 */
public class ColumnedSearchCriteriaList extends AbstractColumnedCriteriaList {
	// Combo box "sort order"
	private static final String[] OPERATOR_ARRAY = { CoreUILabels.resString("combo.clm.Equal"), //$NON-NLS-1$
			CoreUILabels.resString("combo.clm.Different"), //$NON-NLS-1$
			CoreUILabels.resString("combo.clm.StrictlyHigher"), //$NON-NLS-1$
			CoreUILabels.resString("combo.clm.HigherOrEqual"), //$NON-NLS-1$
			CoreUILabels.resString("combo.clm.StrictlyLower"), //$NON-NLS-1$
			CoreUILabels.resString("combo.clm.LowerOrEqual"), //$NON-NLS-1$
			CoreUILabels.resString("combo.clm.Like"), //$NON-NLS-1$
			CoreUILabels.resString("combo.clm.NotLike") }; //$NON-NLS-1$
		
	public static final String DIFFERENT_ID = OPERATOR_ARRAY[1];

	public static final String EQUAL_ID = OPERATOR_ARRAY[0];
	public static final String HIGHER_OR_EQUAL_ID = OPERATOR_ARRAY[3];
	public static final String LIKE_ID = OPERATOR_ARRAY[6];
	public static final String LOWER_OR_EQUAL_ID = OPERATOR_ARRAY[5];
	public static final String NOT_LIKE_ID = OPERATOR_ARRAY[7];
	
	public static final String STRICTLY_HIGHER_ID = OPERATOR_ARRAY[2];
	public static final String STRICTLY_LOWER_ID = OPERATOR_ARRAY[4];

	public static String[] getOperatorArray() {
		return OPERATOR_ARRAY;
	}
	
	/**
	 * Constructor
	 */
	public ColumnedSearchCriteriaList(final ArcadColumns referenceColumns) {
		this(referenceColumns, false);
	}

	/**
	 * Constructor
	 */
	public ColumnedSearchCriteriaList(final ArcadColumns referenceColumns, final boolean withInitData) {
		super(referenceColumns, withInitData);
	}

	/**
	 * Add a new criterion to the collection of criteria
	 */
	public void addCriterion() {
		final ColumnedSearchCriteria criterion = new ColumnedSearchCriteria(criteria.size() + 1, columnNames[0]);
		criteria.add(criteria.size(), criterion);
	}

	public ColumnedSearchCriteriaList duplicate() {
		final ColumnedSearchCriteriaList result = new ColumnedSearchCriteriaList(referenceColumns);
		for (int i = 0; i < criteria.size(); i++) {
			final ColumnedSearchCriteria c = (ColumnedSearchCriteria) getItems(i);
			result.add(c.duplicate());
		}
		return result;
	}

	/*
	 * Initialize the table data. Create COUNT criteria and add them them to the collection of criteria
	 */
	@Override
	protected void initData() {
		ColumnedSearchCriteria criterion;
		criterion = new ColumnedSearchCriteria(1, columnNames[0]);
		criterion.setKeyword(StringTools.EMPTY);
		criteria.add(criterion);
	}
}
