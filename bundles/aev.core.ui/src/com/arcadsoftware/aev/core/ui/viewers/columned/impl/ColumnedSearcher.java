package com.arcadsoftware.aev.core.ui.viewers.columned.impl;

import java.text.Collator;
import java.util.ArrayList;

import com.arcadsoftware.aev.core.ui.columned.model.ColumnedSearchCriteria;
import com.arcadsoftware.aev.core.ui.columned.model.ColumnedSearchCriteriaList;

/**
 * @author dlelong
 */
public class ColumnedSearcher implements IColumnedSearcher {

	private boolean casse;
	private final Collator collator;
	private ColumnedSearchCriteriaList criteriaList;

	public ColumnedSearcher(final ColumnedSearchCriteriaList criteriaList) {
		collator = Collator.getInstance();
		this.criteriaList = criteriaList;
	}

	public ColumnedSearchCriteriaList getCriteriaList() {
		return criteriaList;
	}

	public boolean isCasse() {
		return casse;
	}

	/*
	 * (non-Javadoc)
	 * @see com.arcadsoftware.aev.core.ui.viewers.columned.impl.IColumnedSearcher #match(java.lang.Object)
	 */
	@Override
	public boolean match(final Object element, final IColumnResolver resolver) {
		final int criteriaNumber = criteriaList.getSize();
		final ColumnedSearchCriteria[] criteria = new ColumnedSearchCriteria[criteriaNumber];
		for (int i = 0; i < criteriaList.getSize(); i++) {
			criteria[i] = (ColumnedSearchCriteria) criteriaList.getCriteria().get(i);
			final String operator = criteria[i].getOperator();
			final String value = resolver.getValue(element, criteria[i].getColumnIndex());
			final int compare = collator.compare(value, criteria[i].getKeyword());
			if (operator.equals(ColumnedSearchCriteriaList.EQUAL_ID)) {
				if (!value.equalsIgnoreCase(criteria[i].getKeyword())) {
					return false;
				}
			} else if (operator.equals(ColumnedSearchCriteriaList.DIFFERENT_ID)) {
				if (value.equalsIgnoreCase(criteria[i].getKeyword())) {
					return false;
				}
			} else if (operator.equals(ColumnedSearchCriteriaList.STRICTLY_HIGHER_ID)) {
				if (compare <= 0) {
					return false;
				}
			} else if (operator.equals(ColumnedSearchCriteriaList.STRICTLY_LOWER_ID)) {
				if (compare >= 0) {
					return false;
				}
			} else if (operator.equals(ColumnedSearchCriteriaList.HIGHER_OR_EQUAL_ID)) {
				if (compare < 0) {
					return false;
				}
			} else if (operator.equals(ColumnedSearchCriteriaList.LOWER_OR_EQUAL_ID)) {
				if (compare > 0) {
					return false;
				}
			} else if (operator.equals(ColumnedSearchCriteriaList.LIKE_ID)) {
				String s = criteria[i].getKeyword();
				s = regexEtoile(s);
				if (casse) {
					if (!value.matches(s)) {
						return false;
					}
				} else {
					if (!value.toLowerCase().matches(s.toLowerCase())) {
						return false;
					}
				}
			} else if (operator.equals(ColumnedSearchCriteriaList.NOT_LIKE_ID)) {
				String s = criteria[i].getKeyword();
				s = regexEtoile(s);
				if (casse) {
					if (value.matches(s)) {
						return false;
					}
				} else {
					if (value.toLowerCase().matches(s.toLowerCase())) {
						return false;
					}
				}
			}
		}
		return true;
	}

	private String regexEtoile(final String s) {
		String result = s;
		if (s.lastIndexOf("*") >= 0) { //$NON-NLS-1$
			String f = new String();
			final ArrayList<String> list = new ArrayList<>();
			final char[] c = s.toCharArray();
			for (final char element : c) {
				if (element == '*') {
					list.add(String.valueOf('.'));
				}
				list.add(String.valueOf(element));
			}
			for (final String element : list) {
				f += element;
			}
			result = f;
		}
		return result;
	}

	public void setCasse(final boolean casse) {
		this.casse = casse;
	}

	public void setCriteriaList(final ColumnedSearchCriteriaList criteriaList) {
		this.criteriaList = criteriaList;
	}
}
