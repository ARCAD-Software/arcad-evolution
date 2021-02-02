package com.arcadsoftware.aev.core.ui.wizards.columned;

import org.eclipse.jface.wizard.WizardPage;

import com.arcadsoftware.aev.core.ui.actions.columned.ColumnedExportAction;
import com.arcadsoftware.aev.core.ui.wizards.AbstractWizard;

/**
 * @author dlelong
 */
public class ColumnedCSVExportWizard extends AbstractWizard {

	private ColumnedExportAction action;
	private WizardPage[] pages;

	public ColumnedCSVExportWizard(final ColumnedExportAction action, final String title) {
		this(title);
		this.action = action;
	}

	public ColumnedCSVExportWizard(final String title) {
		super();
		setNeedsProgressMonitor(true);
		setWindowTitle(title);
	}

	@Override
	public void addPages() {
		pages = action.getPages();
		for (final WizardPage page : pages) {
			addPage(page);
		}
	}

	@Override
	public boolean performFinish() {
		try {
			return action.process();
		} catch (final Exception e) {
			return false;
		}
	}
}
