package com.arcadsoftware.aev.core.ui.columned.model;

import com.arcadsoftware.aev.core.tools.StringTools;

/**
 * @author dlelong
 */
public class ColumnedSearchCriteria extends AbstractColumnedCriteria {

	private String keyword = StringTools.EMPTY;
	private String operator = StringTools.EMPTY;

	public ColumnedSearchCriteria(final int id, final String firstColumnName) {
		super();
		setId(id);
		setColumnName(firstColumnName);
		setColumnIndex(0);
		setOperator(ColumnedSearchCriteriaList.EQUAL_ID);
		setKeyword(StringTools.EMPTY);
	}

	public ColumnedSearchCriteria duplicate() {
		final ColumnedSearchCriteria result = new ColumnedSearchCriteria(id, columnName);
		result.setColumnIndex(columnIndex);
		result.setOperator(operator);
		result.setKeyword(keyword);
		return result;
	}

	/**
	 * @return Renvoie keyword.
	 */
	public String getKeyword() {
		return keyword;
	}

	/**
	 * @return Renvoie operator.
	 */
	public String getOperator() {
		return operator;
	}

	/**
	 * @param keyword
	 *            keyword à définir.
	 */
	public void setKeyword(final String keyword) {
		this.keyword = keyword;
	}

	/**
	 * @param operator
	 *            operator à définir.
	 */
	public void setOperator(final String operator) {
		this.operator = operator;
	}
}
