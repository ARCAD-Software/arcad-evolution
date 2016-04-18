package com.arcadsoftware.aev.core.ui.viewerfilters.columned;

import java.text.Collator;
import java.util.ArrayList;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;

import com.arcadsoftware.aev.core.ui.columned.model.ArcadColumns;
import com.arcadsoftware.aev.core.ui.columned.model.ColumnedSearchCriteria;
import com.arcadsoftware.aev.core.ui.columned.model.ColumnedSearchCriteriaList;
import com.arcadsoftware.aev.core.ui.viewers.columned.impl.IColumnResolver;

/**
 * @author dlelong
 */
public class ColumnedViewerFilter extends ViewerFilter {

	private ColumnedSearchCriteriaList criteriaList;
	private Collator collator;
	IColumnResolver resolver;
	private boolean casse = false;

	/**
	 * @param referenceColumns
	 */
	public ColumnedViewerFilter(ColumnedSearchCriteriaList criteriaList, ArcadColumns referenceColumns,
			IColumnResolver resolver) {
		super();
		this.collator = Collator.getInstance();
		this.criteriaList = criteriaList;
		this.resolver = resolver;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.viewers.ViewerFilter#select(org.eclipse.jface.viewers
	 * .Viewer, java.lang.Object, java.lang.Object)
	 */
	@Override
	public boolean select(Viewer viewer, Object parentElement, Object element) {
		int criteriaNumber = criteriaList.getSize();
		ColumnedSearchCriteria[] criteria = new ColumnedSearchCriteria[criteriaNumber];
		for (int i = 0; i < criteriaList.getSize(); i++) {
			criteria[i] = (ColumnedSearchCriteria) criteriaList.getCriteria().get(i);
			String operator = criteria[i].getOperator();
			String value = resolver.getValue(element, criteria[i].getColumnIndex());

			int compare = collator.compare(value, criteria[i].getKeyword());
			if (operator.equals(ColumnedSearchCriteriaList.EQUAL_ID)) {
				if (!value.equalsIgnoreCase(criteria[i].getKeyword()))
					return false;
			} else if (operator.equals(ColumnedSearchCriteriaList.DIFFERENT_ID)) {
				if (value.equalsIgnoreCase(criteria[i].getKeyword()))
					return false;
			} else if (operator.equals(ColumnedSearchCriteriaList.STRICTLY_HIGHER_ID)) {
				if (compare <= 0)
					return false;
			} else if (operator.equals(ColumnedSearchCriteriaList.STRICTLY_LOWER_ID)) {
				if (compare >= 0)
					return false;
			} else if (operator.equals(ColumnedSearchCriteriaList.HIGHER_OR_EQUAL_ID)) {
				if (compare < 0)
					return false;
			} else if (operator.equals(ColumnedSearchCriteriaList.LOWER_OR_EQUAL_ID)) {
				if (compare > 0)
					return false;
			} else if (operator.equals(ColumnedSearchCriteriaList.LIKE_ID)) {
				String s = criteria[i].getKeyword();
				s = regexStar(s);
				if (casse) {
					if (!value.matches(s))
						return false;
				} else {
					if (!value.toLowerCase().matches(s.toLowerCase()))
						return false;
				}
			} else if (operator.equals(ColumnedSearchCriteriaList.NOT_LIKE_ID)) {
				String s = criteria[i].getKeyword();
				s = regexStar(s);
				if (casse) {
					if (value.matches(s))
						return false;
				} else {
					if (value.toLowerCase().matches(s.toLowerCase()))
						return false;
				}
			}
		}
		return true;
	}

	public ColumnedSearchCriteriaList getCriteriaList() {
		return criteriaList;
	}

	public void setCriteriaList(ColumnedSearchCriteriaList criteriaList) {
		this.criteriaList = criteriaList;
	}

	public boolean isCasse() {
		return casse;
	}

	public void setCasse(boolean casse) {
		this.casse = casse;
	}

	@SuppressWarnings("unchecked")
	private String regexStar(String s) {
		String result = s;
		if (s.lastIndexOf("*") >= 0) { //$NON-NLS-1$
			String f = new String();
			ArrayList list = new ArrayList();
			char[] c = s.toCharArray();
			for (int j = 0; j < c.length; j++) {
				if (c[j] == '*')
					list.add(String.valueOf('.'));
				list.add(String.valueOf(c[j]));
			}
			for (int j = 0; j < list.size(); j++)
				f += list.get(j);
			result = f;
		}
		return result;
	}
}
