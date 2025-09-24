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
 * Cr�� le 5 avr. 2007
 *

 */
package com.arcadsoftware.aev.core.ui.composite;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

/**
 * @author MD
 */
public abstract class HorizontalExpandBarComposite extends AbstractExpandBarComposite {

	/**
	 * @param parent
	 * @param style
	 */
	public HorizontalExpandBarComposite(final Composite parent, final int style, final String title,
			final int headerAreaHeight,
			final int bodyAreaHeight) {
		super(parent, style, title, headerAreaHeight, bodyAreaHeight, ORIENTATION_HORIZONTAL);
	}

	@Override
	public void createBodyAreaComposite() {
		userArea = new Composite(this, SWT.NONE);
		final GridLayout gridLayout = new GridLayout(1, false);
		gridLayout.marginHeight = 0;
		gridLayout.marginWidth = 0;
		userArea.setLayout(gridLayout);

		final GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.heightHint = bodyHeight;
		gridData.horizontalSpan = 2;
		gridData.grabExcessHorizontalSpace = true;
		userArea.setLayoutData(gridData);
	}

	@Override
	public void formatComposite() {
		final GridLayout gridLayout = new GridLayout(2, false);
		gridLayout.marginHeight = 0;
		gridLayout.marginWidth = 0;
		setLayout(gridLayout);
		final GridData gridData = new GridData();
		gridData.heightHint = headerHeihght + bodyHeight + OFFSET;
		gridData.horizontalAlignment = GridData.FILL;
		gridData.grabExcessHorizontalSpace = true;
		setLayoutData(gridData);
	}

	@Override
	public void formatExpander() {
		final GridData gridData = new GridData();
		gridData.heightHint = headerHeihght;
		moreParameter.setLayoutData(gridData);
	}

	@Override
	public void formatTitle(final String text) {
		// Label d'affichage du titre
		titleLabel = new Label(this, SWT.NONE);
		titleLabel.setText(text);
		final GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.grabExcessHorizontalSpace = true;
		gridData.heightHint = headerHeihght;
		titleLabel.setLayoutData(gridData);
	}

}
