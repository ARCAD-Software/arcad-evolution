package com.arcadsoftware.aev.core.ui.contentproviders;

import java.util.Iterator;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

public class IteratorItemContentProvider implements IStructuredContentProvider {

	@Override
	public void dispose() {
		// Do nothing
	}

	@Override
	public Object[] getElements(final Object inputElement) {
		if (inputElement instanceof Iterator) {
			Object[] result = new Object[0];
			final Iterator<?> it = (Iterator<?>) inputElement;
			int index = 0;
			while (it.hasNext()) {
				final Object[] tmp = new Object[result.length + 1];
				System.arraycopy(result, 0, tmp, 0, result.length);
				tmp[index] = it.next();
				index++;
				result = tmp;
			}
			return result;
		}
		return new Object[0];
	}

	@Override
	public void inputChanged(final Viewer arg0, final Object arg1, final Object arg2) {
		// Do nothing
	}

}
