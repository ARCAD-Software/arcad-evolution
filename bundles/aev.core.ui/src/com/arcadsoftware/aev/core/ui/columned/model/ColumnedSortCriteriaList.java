package com.arcadsoftware.aev.core.ui.columned.model;

import com.arcadsoftware.aev.core.ui.tools.CoreUILabels;

/**
 * @author dlelong
 */
public class ColumnedSortCriteriaList extends AbstractColumnedCriteriaList {

	// Combo box "sort order"
	public static final String[] SORT_ORDER_ARRAY = { CoreUILabels.resString("combo.clm.AscendingSort"), //$NON-NLS-1$ 
			CoreUILabels.resString("combo.clm.DescendingSort") }; //$NON-NLS-1$
	// Combo box "column name"

	public static String ASCENDING = SORT_ORDER_ARRAY[0];
	public static String DESCENDING = SORT_ORDER_ARRAY[1];

	/**
	 * Constructor
	 */
	public ColumnedSortCriteriaList(ArcadColumns referenceColumns, boolean withInitData) {
		super(referenceColumns, withInitData);
	}

	public ColumnedSortCriteriaList(ArcadColumns referenceColumns) {
		this(referenceColumns, false);
	}

	/*
	 * Initialize the table data. Create COUNT criteria and add them them to the
	 * collection of criteria
	 */
	@Override
	protected void initData() {
		ColumnedSortCriteria criterion;
		criterion = new ColumnedSortCriteria(1, columnNames[0]);
		criteria.add(criterion);
	}

	/**
	 * Return the array of sort order
	 */
	public String[] getSortOrder() {
		return SORT_ORDER_ARRAY;
	}

	public ColumnedSortCriteriaList duplicate() {
		ColumnedSortCriteriaList result = new ColumnedSortCriteriaList(referenceColumns);
		for (int i = 0; i < criteria.size(); i++) {
			ColumnedSortCriteria c = (ColumnedSortCriteria) getItems(i);
			result.add(c.duplicate());
		}
		return result;
	}

	/**
	 * Add a new criterion to the collection of criteria
	 */
	public void addCriterion() {
		ColumnedSortCriteria criterion = new ColumnedSortCriteria(criteria.size() + 1, columnNames[0]);
		criteria.add(criteria.size(), criterion);
	}

	public void swap(ColumnedSortCriteria criteria1, ColumnedSortCriteria criteria2) {
		ColumnedSortCriteria temp = new ColumnedSortCriteria();
		criteria2.assignTo(temp);
		criteria1.assignTo(criteria2);
		temp.assignTo(criteria1);
	}

	public void orderCriterion() {
		ColumnedSortCriteriaList temp = new ColumnedSortCriteriaList(this.referenceColumns);
		ColumnedSortCriteria tempCriteria;
		int size = this.getSize();
		for (int i = 1; i <= size; i++) {
			for (int j = 1; j <= size; j++) {
				tempCriteria = (ColumnedSortCriteria) criteria.get(j - 1);
				if (tempCriteria.getId() == i) {
					temp.add(tempCriteria);
					break;
				}
			}
		}
		this.criteria = temp.getCriteria();
	}
}
