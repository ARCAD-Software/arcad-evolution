/*
 * Created on 27 nov. 2006
 */
package com.arcadsoftware.aev.core.ui.viewers.columned.impl;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.ui.part.ViewPart;

/**
 * @author MD
 */
public class TestTableView extends ViewPart {
	Table table;
	TestTableViewer viewer;

	/**
	 * 
	 */
	public TestTableView() {
		super();
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.IWorkbenchPart#createPartControl(org.eclipse.swt.widgets .Composite)
	 */
	@Override
	public void createPartControl(final Composite parent) {
		viewer = new TestTableViewer(parent, SWT.NONE | SWT.FULL_SELECTION);

	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.IWorkbenchPart#setFocus()
	 */
	@Override
	public void setFocus() {
		// Do nothing
	}

}
