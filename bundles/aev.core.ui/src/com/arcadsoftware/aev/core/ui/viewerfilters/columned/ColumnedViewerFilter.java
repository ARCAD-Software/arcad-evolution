package com.arcadsoftware.aev.core.ui.viewerfilters.columned;

import java.text.Collator;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;

import com.arcadsoftware.aev.core.tools.StringTools;
import com.arcadsoftware.aev.core.ui.columned.model.ArcadColumns;
import com.arcadsoftware.aev.core.ui.columned.model.ColumnedSearchCriteria;
import com.arcadsoftware.aev.core.ui.columned.model.ColumnedSearchCriteriaList;
import com.arcadsoftware.aev.core.ui.viewers.columned.impl.IColumnResolver;

/**
 * @author dlelong
 */
public class ColumnedViewerFilter extends ViewerFilter {

	private static final String SPECIAL_REGEX = "<([{\\^-=$!|]})?*+.>";
	private boolean casse = false;
	private final Collator collator;
	private ColumnedSearchCriteriaList criteriaList;

	IColumnResolver resolver;

	/**
	 * @param referenceColumns
	 */
	public ColumnedViewerFilter(final ColumnedSearchCriteriaList criteriaList, final ArcadColumns referenceColumns,
			final IColumnResolver resolver) {
		super();
		collator = Collator.getInstance();
		this.criteriaList = criteriaList;
		this.resolver = resolver;
	}

	public ColumnedSearchCriteriaList getCriteriaList() {
		return criteriaList;
	}

	public boolean isCasse() {
		return casse;
	}

	/**
	 * @return regex string, taking account special regex escape characters and '*' treated as wildcard
	 */
	private String regexStar(final String s) {
		String result = s;
		// <MR number="2018/00035" date="Jan 30, 2018" type="Bug" user="ACL">
		// ParseException error when Filter string contains any Regex "special" character
		if (StringTools.containsAny(s, SPECIAL_REGEX)) {
			final StringBuilder sb = new StringBuilder();
			final char[] c = s.toCharArray();
			for (final char cj : c) {
				// Interpret any '*' values in string as regex pattern match '.'
				if (cj == '*') {
					sb.append('.');
				} else if (SPECIAL_REGEX.indexOf(cj) > 0) {
					sb.append('\\');
				}
				sb.append(cj);
			}
			result = sb.toString();
		}
		// </MR>
		return result;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ViewerFilter#select(org.eclipse.jface.viewers .Viewer, java.lang.Object,
	 * java.lang.Object)
	 */
	@Override
	public boolean select(final Viewer viewer, final Object parentElement, final Object element) {
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
				s = regexStar(s);
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
				s = regexStar(s);
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

	public void setCasse(final boolean casse) {
		this.casse = casse;
	}

	public void setCriteriaList(final ColumnedSearchCriteriaList criteriaList) {
		this.criteriaList = criteriaList;
	}
}
