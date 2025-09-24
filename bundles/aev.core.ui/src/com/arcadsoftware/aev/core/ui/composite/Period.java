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
package com.arcadsoftware.aev.core.ui.composite;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

import com.arcadsoftware.aev.core.ui.tools.CoreUILabels;
import com.arcadsoftware.aev.core.ui.tools.GuiFormatTools;

public class Period extends Composite {

	protected Text beginText;
	protected Text endText;

	public Period(final Composite parent, final int style) {
		super(parent, style);
		createContent();
	}

	protected void createContent() {
		setLayout(new GridLayout(3, false));
		setLayoutData(new GridData(GridData.FILL_BOTH));
		beginText = GuiFormatTools.createLabelledDateWithDateSelector(this,
				CoreUILabels.resString("Period.beginDate"), SWT.NONE, "*"); //$NON-NLS-1$ //$NON-NLS-2$
		endText = GuiFormatTools.createLabelledDateWithDateSelector(this,
				CoreUILabels.resString("Period.endDate"), SWT.NONE, "*"); //$NON-NLS-1$ //$NON-NLS-2$
	}

	public Text getBeginText() {
		return beginText;
	}

	public Text getEndText() {
		return endText;
	}

	public void setBeginDate(final String beginDate) {
		beginText.setText(beginDate);
	}

	public void setEndDate(final String endDate) {
		endText.setText(endDate);
	}
}
