package com.arcadsoftware.aev.core.ui.composite;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.ExpandBar;
import org.eclipse.swt.widgets.Label;

import com.arcadsoftware.aev.core.ui.editors.AbstractArcadEditorPart;
import com.arcadsoftware.aev.core.ui.tools.GuiFormatTools;

public class AbstractTwoPartsComposite extends AbstractEditorComposite {

	Label creationInformationLabel;
	Composite leftComposite;
	Label modificationInformationLabel;
	Composite rightComposite;
	CustomExpandBar sourceBar;

	public AbstractTwoPartsComposite(final Composite parent, final int style, final Object edited,
			final AbstractArcadEditorPart editor) {
		super(parent, style, edited, editor);
	}

	@Override
	public void createContent() {
		final SashForm listAndEditor = new SashForm(this, SWT.HORIZONTAL);

		GridData gridData = new GridData(GridData.FILL_BOTH);
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessVerticalSpace = true;
		listAndEditor.setLayoutData(gridData);

		//
		leftComposite = createLeftPart(listAndEditor);
		gridData = new GridData(GridData.FILL_BOTH);
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessVerticalSpace = true;
		leftComposite.setLayoutData(gridData);

		rightComposite = createRightPart(listAndEditor);

		gridData = new GridData(GridData.FILL_BOTH);
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessVerticalSpace = true;
		rightComposite.setLayoutData(gridData);

		listAndEditor.setWeights(new int[] { 40, 100 });

	}

	protected void createExpandBarItems(final ExpandBar parent) {
		// To override to add new ExpandBerItem
	}

	private Composite createLeftPart(final Composite parent) {
		final Composite composite = GuiFormatTools.createComposite(parent, 3, false, SWT.BORDER, true);
		final GridLayout l = (GridLayout) composite.getLayout();
		l.marginHeight = l.marginWidth = 0;
		l.marginHeight = l.marginWidth = l.marginBottom = l.marginTop = 0;
		composite.layout();

		sourceBar = new CustomExpandBar(composite, SWT.V_SCROLL);
		final GridData gridData = new GridData(GridData.FILL_BOTH);
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessVerticalSpace = true;
		gridData.horizontalSpan = 3;
		sourceBar.getExpandBar().setLayoutData(gridData);
		sourceBar.getExpandBar().setSpacing(1);

		createExpandBarItems(sourceBar.getExpandBar());
		sourceBar.initialize();
		return composite;
	}

	private Composite createRightPart(final Composite parent) {
		final Composite composite = GuiFormatTools.createComposite(parent, 3, false, SWT.BORDER, true);
		final GridLayout l = (GridLayout) composite.getLayout();
		l.marginHeight = l.marginWidth = l.marginBottom = l.marginTop = 2;
		l.marginLeft = 3;
		composite.layout();
		createRightPartComposite(composite);
		return composite;
	}

	protected void createRightPartComposite(final Composite parent) {

	}

	protected int getItemNumber() {
		return sourceBar.getExpandBar().getItemCount();
	}

}
