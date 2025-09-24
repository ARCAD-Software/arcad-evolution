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
/*
 * Created on 28 f�vr. 2006
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
	 * @see org.eclipse.jface.preference.PreferencePage#createContents(org.eclipse .swt.widgets.Composite)
	 */
	@Override
	protected Control createContents(final Composite parent) {
		final Composite composite = new Composite(parent, SWT.NONE);
		final GridLayout grid = new GridLayout();
		grid.numColumns = 3;
		composite.setLayout(grid);
		final GridData gridData = new GridData(GridData.BEGINNING);
		gridData.horizontalAlignment = GridData.FILL;
		composite.setLayoutData(gridData);
		fillContents(composite);
		return composite;
	}

	/*
	 * (non-Javadoc)
	 * @see com.arcadsoftware.aev.core.ui.propertypages.ArcadPropertyPage#doAfterCreating ()
	 */
	@Override
	public void doAfterCreating() {
		super.doAfterCreating();
		setValue();
	}
}
