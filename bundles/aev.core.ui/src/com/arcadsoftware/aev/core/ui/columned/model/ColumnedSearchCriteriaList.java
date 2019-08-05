package com.arcadsoftware.aev.core.ui.columned.model;

import com.arcadsoftware.aev.core.tools.StringTools;
import com.arcadsoftware.aev.core.ui.tools.CoreUILabels;

/**
 * @author dlelong
 */
public class ColumnedSearchCriteriaList extends AbstractColumnedCriteriaList {

	// Combo box "sort order"
	public static final String[] OPERATOR_ARRAY = { CoreUILabels.resString("combo.clm.Equal"), //$NON-NLS-1$
			CoreUILabels.resString("combo.clm.Different"), //$NON-NLS-1$
			CoreUILabels.resString("combo.clm.StrictlyHigher"), //$NON-NLS-1$
			CoreUILabels.resString("combo.clm.HigherOrEqual"), //$NON-NLS-1$
			CoreUILabels.resString("combo.clm.StrictlyLower"), //$NON-NLS-1$
			CoreUILabels.resString("combo.clm.LowerOrEqual"), //$NON-NLS-1$
			CoreUILabels.resString("combo.clm.Like"), //$NON-NLS-1$
			CoreUILabels.resString("combo.clm.NotLike") }; //$NON-NLS-1$

	public static String EQUAL_ID = OPERATOR_ARRAY[0];
	public static String DIFFERENT_ID = OPERATOR_ARRAY[1];
	public static String STRICTLY_HIGHER_ID = OPERATOR_ARRAY[2];
	public static String HIGHER_OR_EQUAL_ID = OPERATOR_ARRAY[3];
	public static String STRICTLY_LOWER_ID = OPERATOR_ARRAY[4];
	public static String LOWER_OR_EQUAL_ID = OPERATOR_ARRAY[5];
	public static String LIKE_ID = OPERATOR_ARRAY[6];
	public static String NOT_LIKE_ID = OPERATOR_ARRAY[7];

	/**
	 * Constructor
	 */
	public ColumnedSearchCriteriaList(ArcadColumns referenceColumns, boolean withInitData) {
		super(referenceColumns, withInitData);
	}

	/**
	 * Constructor
	 */
	public ColumnedSearchCriteriaList(ArcadColumns referenceColumns) {
		this(referenceColumns, false);
	}

	/**
	 * Add a new criterion to the collection of criteria
	 */
	public void addCriterion() {
		ColumnedSearchCriteria criterion = new ColumnedSearchCriteria(criteria.size() + 1, columnNames[0]);
		criteria.add(criteria.size(), criterion);
	}

	/*
	 * Initialize the table data. Create COUNT criteria and add them them to the
	 * collection of criteria
	 */
	@Override
	protected void initData() {
		ColumnedSearchCriteria criterion;
		criterion = new ColumnedSearchCriteria(1, columnNames[0]);
		criterion.setKeyword(StringTools.EMPTY);
		criteria.add(criterion);
	}

	public ColumnedSearchCriteriaList duplicate() {
		ColumnedSearchCriteriaList result = new ColumnedSearchCriteriaList(referenceColumns);
		for (int i = 0; i < criteria.size(); i++) {
			ColumnedSearchCriteria c = (ColumnedSearchCriteria) getItems(i);
			result.add(c.duplicate());
		}
		return result;
	}
}
