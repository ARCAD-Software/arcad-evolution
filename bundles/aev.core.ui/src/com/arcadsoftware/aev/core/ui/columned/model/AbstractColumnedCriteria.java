package com.arcadsoftware.aev.core.ui.columned.model;

import com.arcadsoftware.aev.core.tools.StringTools;

/**
 * @author dlelong
 */
public abstract class AbstractColumnedCriteria {

	protected int id = 0;
	protected String columnName = StringTools.EMPTY;
	protected int columnIndex = 0;

	public AbstractColumnedCriteria() {
		super();
	}

	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getColumnIndex() {
		return columnIndex;
	}

	public void setColumnIndex(int columnIndex) {
		this.columnIndex = columnIndex;
	}
}
