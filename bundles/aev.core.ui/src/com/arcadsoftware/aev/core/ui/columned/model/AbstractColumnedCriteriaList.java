package com.arcadsoftware.aev.core.ui.columned.model;

import java.util.ArrayList;

/**
 * @author dlelong
 */
public abstract class AbstractColumnedCriteriaList {

	protected final int COUNT = 10;
	@SuppressWarnings("unchecked")
	protected ArrayList criteria = new ArrayList(COUNT);
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
	@SuppressWarnings("unchecked")
	public ArrayList getCriteria() {
		return criteria;
	}

	public int getSize() {
		return (criteria.size());
	}

	@SuppressWarnings("unchecked")
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
