package com.arcadsoftware.aev.core.ui.viewers.sorters;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.jface.viewers.ViewerSorter;

import com.arcadsoftware.aev.core.ui.columned.model.ColumnedSortCriteria;
import com.arcadsoftware.aev.core.ui.columned.model.ColumnedSortCriteriaList;
import com.arcadsoftware.aev.core.ui.viewers.columned.impl.IColumnResolver;

/**
 * @author dlelong
 */
public class ColumnedSorter extends ViewerSorter {
	private ColumnedSortCriteriaList criteriaList;
	private IColumnResolver resolver;
	private ViewerComparator viewerComparator = new ViewerComparator();

	public ColumnedSorter(ColumnedSortCriteriaList criteriaList, IColumnResolver resolver) {
		super();
		this.criteriaList = criteriaList;
		this.resolver = resolver;
	}

	@Override
	public int category(Object element) {
		if (element instanceof String)
			return 0;
		return 1;
	}

	@Override
	public int compare(Viewer viewer, Object o1, Object o2) {
		int cat1 = category(o1);
		int cat2 = category(o2);

		if (cat1 != cat2)
			return cat1 - cat2;

		int result = 0;

		int criteriaNumber = criteriaList.getSize();
		ColumnedSortCriteria[] criteria = new ColumnedSortCriteria[criteriaNumber];
		Integer[] columnIndex = new Integer[criteriaList.getSize()];
		for (int i = 0; i < criteriaList.getSize(); i++) {
			criteria[i] = (ColumnedSortCriteria) criteriaList.getCriteria().get(i);
			columnIndex[i] = new Integer(criteria[i].getColumnIndex());
		}

		for (int i = 0; i < columnIndex.length; i++) {
			String text1 = null, text2 = null;
			text1 = resolver.getValue(o1, columnIndex[i].intValue());
			text2 = resolver.getValue(o2, columnIndex[i].intValue());
			if (!text1.equalsIgnoreCase(text2)) {
				// ordre croissant
				if (criteria[i].getSortOrder().equals(ColumnedSortCriteriaList.ASCENDING))
					return viewerComparator.compare(viewer, text1, text2);
				return viewerComparator.compare(viewer, text2, text1);
			}
		}
		return result;
	}

	public ColumnedSortCriteriaList getCriteriaList() {
		return criteriaList;
	}

	public void setCriteriaList(ColumnedSortCriteriaList criteriaList) {
		this.criteriaList = criteriaList;
	}
}
