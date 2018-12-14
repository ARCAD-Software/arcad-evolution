package com.arcadsoftware.aev.core.ui.columned.model;

import com.arcadsoftware.aev.core.tools.StringTools;

/**
 * @author dlelong
 */
public class ColumnedSearchCriteria extends AbstractColumnedCriteria {

	private String operator = StringTools.EMPTY;
	private String keyword = StringTools.EMPTY;

	public ColumnedSearchCriteria(int id, String firstColumnName) {
		super();
		setId(id);
		setColumnName(firstColumnName);
		setColumnIndex(0);
		setOperator(ColumnedSearchCriteriaList.OPERATOR_ARRAY[0]);
		setKeyword(StringTools.EMPTY);
	}

	public ColumnedSearchCriteria duplicate() {
		ColumnedSearchCriteria result = new ColumnedSearchCriteria(id, columnName);
		result.setColumnIndex(this.columnIndex);
		result.setOperator(this.operator);
		result.setKeyword(this.keyword);
		return result;
	}

	/**
	 * @return Renvoie keyword.
	 */
	public String getKeyword() {
		return keyword;
	}

	/**
	 * @param keyword
	 *            keyword à définir.
	 */
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	/**
	 * @return Renvoie operator.
	 */
	public String getOperator() {
		return operator;
	}

	/**
	 * @param operator
	 *            operator à définir.
	 */
	public void setOperator(String operator) {
		this.operator = operator;
	}
}
