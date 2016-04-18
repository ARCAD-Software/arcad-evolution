package com.arcadsoftware.aev.core.ui.contentproviders;

import java.util.Iterator;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

public class IteratorItemContentProvider implements IStructuredContentProvider {

	@SuppressWarnings("unchecked")
	public Object[] getElements(Object inputElement) {
		if (inputElement instanceof Iterator) {
			Object[] result = new Object[0];
			Iterator it = (Iterator) inputElement;
			int index = 0;
			while (it.hasNext()) {
				Object[] tmp = new Object[result.length + 1];
				System.arraycopy(result, 0, tmp, 0, result.length);
				tmp[index] = it.next();
				index++;
				result = tmp;
			}
			return result;
		}
		return new Object[0];
	}

	public void dispose() {
		// Do nothing
	}

	public void inputChanged(Viewer arg0, Object arg1, Object arg2) {
		// Do nothing
	}

}
