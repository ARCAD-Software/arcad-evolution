/*******************************************************************************
 * Copyright (c) 2025 ARCAD Software.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     ARCAD Software - initial API and implementation
 *******************************************************************************/
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
	private final IColumnResolver resolver;
	private final ViewerComparator viewerComparator = new ViewerComparator();

	public ColumnedSorter(final ColumnedSortCriteriaList criteriaList, final IColumnResolver resolver) {
		super();
		this.criteriaList = criteriaList;
		this.resolver = resolver;
	}

	@Override
	public int category(final Object element) {
		if (element instanceof String) {
			return 0;
		}
		return 1;
	}

	@Override
	public int compare(final Viewer viewer, final Object o1, final Object o2) {
		final int cat1 = category(o1);
		final int cat2 = category(o2);

		if (cat1 != cat2) {
			return cat1 - cat2;
		}

		final int result = 0;

		final int criteriaNumber = criteriaList.getSize();
		final ColumnedSortCriteria[] criteria = new ColumnedSortCriteria[criteriaNumber];
		final Integer[] columnIndex = new Integer[criteriaList.getSize()];
		for (int i = 0; i < criteriaList.getSize(); i++) {
			criteria[i] = (ColumnedSortCriteria) criteriaList.getCriteria().get(i);
			columnIndex[i] = new Integer(criteria[i].getColumnIndex());
		}

		for (int i = 0; i < columnIndex.length; i++) {
			String text1 = null, text2 = null;
			text1 = resolver.getValue(o1, columnIndex[i]);
			text2 = resolver.getValue(o2, columnIndex[i]);
			if (!text1.equalsIgnoreCase(text2)) {
				// ordre croissant
				if (criteria[i].getSortOrder().equals(ColumnedSortCriteriaList.ASCENDING)) {
					return viewerComparator.compare(viewer, text1, text2);
				}
				return viewerComparator.compare(viewer, text2, text1);
			}
		}
		return result;
	}

	public ColumnedSortCriteriaList getCriteriaList() {
		return criteriaList;
	}

	public void setCriteriaList(final ColumnedSortCriteriaList criteriaList) {
		this.criteriaList = criteriaList;
	}
}
