package com.arcadsoftware.aev.core.ui.columned.model;

import com.arcadsoftware.aev.core.tools.StringTools;

/**
 * @author dlelong
 */
public class ColumnedSortCriteria extends AbstractColumnedCriteria {

	private String sortOrder = StringTools.EMPTY;

	public ColumnedSortCriteria(int id, String firstColumnName) {
		super();
		setId(id);
		setColumnName(firstColumnName);
		setColumnIndex(0);
		setSortOrder(ColumnedSortCriteriaList.ASCENDING);
	}

	public ColumnedSortCriteria() {
		super();
	}

	public ColumnedSortCriteria duplicate() {
		ColumnedSortCriteria result = new ColumnedSortCriteria(id, columnName);
		result.setColumnIndex(this.columnIndex);
		result.setSortOrder(this.sortOrder);
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
	public void setSortOrder(String sortOrder) {
		this.sortOrder = sortOrder;
	}

	public void assignTo(ColumnedSortCriteria target) {
		target.setId(id);
		target.setColumnName(columnName);
		target.setColumnIndex(columnIndex);
		target.setSortOrder(sortOrder);
	}
}
