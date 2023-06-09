/*
 * Cr�� le 27 avr. 2007
 *

 */
package com.arcadsoftware.aev.core.ui.composite;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

/**
 * @author MD
 */
public abstract class VerticalExpandBarComposite extends AbstractExpandBarComposite {

	/**
	 * @param parent
	 * @param style
	 * @param title
	 * @param headerSize
	 * @param bodySize
	 * @param orientation
	 */
	public VerticalExpandBarComposite(final Composite parent, final int style, final String title, final int headerSize,
			final int bodySize) {

		super(parent, style, title, headerSize, bodySize, ORIENTATION_VERTICAL);

	}

	@Override
	public void createBodyAreaComposite() {
		userArea = new Composite(this, SWT.NONE);
		final GridLayout gridLayout = new GridLayout(1, true);
		gridLayout.marginHeight = 0;
		gridLayout.marginWidth = 0;
		userArea.setLayout(gridLayout);

		final GridData gridData = new GridData(GridData.FILL_VERTICAL);
		gridData.widthHint = bodyHeight;
		gridData.grabExcessVerticalSpace = true;
		userArea.setLayoutData(gridData);
	}

	@Override
	public void doOnCollapse() {
		final GridData gridData = (GridData) getLayoutData();
		gridData.widthHint = headerHeihght;
	}

	@Override
	public void formatComposite() {
		final GridLayout gridLayout = new GridLayout(2, false);
		gridLayout.marginHeight = 0;
		gridLayout.marginWidth = 0;
		setLayout(gridLayout);
		final GridData gridData = new GridData();
		gridData.widthHint = headerHeihght + bodyHeight + OFFSET;
		gridData.verticalAlignment = GridData.FILL;
		gridData.grabExcessVerticalSpace = true;
		setLayoutData(gridData);
	}

	@Override
	public void formatExpander() {
		final GridData gridData = new GridData();
		gridData.widthHint = headerHeihght;
		gridData.verticalAlignment = GridData.BEGINNING;
		moreParameter.setLayoutData(gridData);
		moreParameter.setToolTipText(labelTitle);
	}
}
