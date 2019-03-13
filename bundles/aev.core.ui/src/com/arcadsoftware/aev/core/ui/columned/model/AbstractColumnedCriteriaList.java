package com.arcadsoftware.aev.core.ui.columned.model;

import java.util.ArrayList;

public abstract class AbstractColumnedCriteriaList {

	protected final int COUNT = 10;
	protected ArrayList<AbstractColumnedCriteria> criteria = new ArrayList<AbstractColumnedCriteria>(COUNT);
	protected ArcadColumns referenceColumns;

	public String[] columnNames;

	public AbstractColumnedCriteriaList(ArcadColumns referenceColumns, boolean withInitData) {
		this.referenceColumns = referenceColumns;
		this.columnNames = referenceColumns.getUserNameValues();
		if (withInitData)
			this.initData();
	}

	protected abstract void initData();

	/**
	 * Return the collection of criteria
	 */
	public ArrayList<AbstractColumnedCriteria> getCriteria() {
		return criteria;
	}

	public int getSize() {
		return (criteria.size());
	}

	public void add(AbstractColumnedCriteria criterion) {
		criteria.add(criterion);
	}

	public void removeCriterion(AbstractColumnedCriteria criterion) {
		criteria.remove(criterion);
	}

	public String[] getColumnNames() {
		return columnNames;
	}

	public void setColumnNames(String[] columnNames) {
		this.columnNames = columnNames;
	}

	/**
	 * Return the collection of criteria
	 */
	public AbstractColumnedCriteria getItems(int index) {
		return (AbstractColumnedCriteria) criteria.get(index);
	}
}
