package com.arcadsoftware.aev.core.ui.viewers.sorters;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;

import com.arcadsoftware.aev.core.ui.columned.model.AbstractColumnedCriteria;

/**
 * @author dlelong
 */
public class ColumnedCriteriaSorter extends ViewerSorter {
	/**
	 * Creates a resource sorter that will use the given sort criteria.
	 *
	 * @param criteria
	 *            the sort criterion to use: one of <code>NAME</code> or <code>TYPE</code>
	 */
	public ColumnedCriteriaSorter() {
		super();
	}

	@Override
	public int compare(final Viewer viewer, final Object o1, final Object o2) {
		final int id1 = ((AbstractColumnedCriteria) o1).getId();
		final int id2 = ((AbstractColumnedCriteria) o2).getId();
		if (id1 < id2) {
			return -1;
		} else if (id1 == id2) {
			return 0;
		} else {
			return 1;
		}
	}
}
