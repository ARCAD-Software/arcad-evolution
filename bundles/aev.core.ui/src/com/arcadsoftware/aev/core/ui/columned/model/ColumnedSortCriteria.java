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
	 *            sortOrder à définir.
	 */
	public void setSortOrder(final String sortOrder) {
		this.sortOrder = sortOrder;
	}
}
