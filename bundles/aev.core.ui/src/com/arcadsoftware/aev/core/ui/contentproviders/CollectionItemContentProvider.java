/**
 *
 */
package com.arcadsoftware.aev.core.ui.contentproviders;

import java.util.Collection;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

/**
 * @author jbeauquis created on 24 oct. 07 16:55:26
 */
public class CollectionItemContentProvider implements IStructuredContentProvider {

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.jface.viewers.IContentProvider#dispose()
	 */
	@Override
	public void dispose() {
		// Do nothing
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.jface.viewers.IStructuredContentProvider#getElements(java .lang.Object)
	 */
	@Override
	public Object[] getElements(final Object inputElement) {
		if (inputElement instanceof Collection) {
			final Collection<?> colllection = (Collection<?>) inputElement;
			return colllection.toArray();
		}
		return new Object[0];
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.jface.viewers.IContentProvider#inputChanged(org.eclipse.jface .viewers.Viewer, java.lang.Object,
	 * java.lang.Object)
	 */
	@Override
	public void inputChanged(final Viewer arg0, final Object arg1, final Object arg2) {
		// Do nothing
	}

}