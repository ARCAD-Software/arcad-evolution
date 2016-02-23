/*
 * Created on 28 févr. 2006
 */
package com.arcadsoftware.aev.core.ui.propertypages;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

/**
 * @author MD
 */
public abstract class SimplePropertyPage extends ArcadPropertyPage {

	public SimplePropertyPage() {
		super();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.preference.PreferencePage#createContents(org.eclipse
	 * .swt.widgets.Composite)
	 */
	@Override
	protected Control createContents(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		GridLayout grid = new GridLayout();
		grid.numColumns = 3;
		composite.setLayout(grid);
		GridData gridData = new GridData(GridData.BEGINNING);
		gridData.horizontalAlignment = GridData.FILL;
		composite.setLayoutData(gridData);
		fillContents(composite);
		return composite;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.arcadsoftware.aev.core.ui.propertypages.ArcadPropertyPage#doAfterCreating
	 * ()
	 */
	@Override
	public void doAfterCreating() {
		super.doAfterCreating();
		setValue();
	}
}
