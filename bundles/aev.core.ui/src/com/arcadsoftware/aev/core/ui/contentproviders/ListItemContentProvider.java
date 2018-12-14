package com.arcadsoftware.aev.core.ui.contentproviders;

import java.util.List;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

public class ListItemContentProvider implements IStructuredContentProvider {

	@SuppressWarnings("unchecked")
	public Object[] getElements(Object inputElement) {
		if (inputElement instanceof List) {
			List list = (List) inputElement;
			return list.toArray();
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
