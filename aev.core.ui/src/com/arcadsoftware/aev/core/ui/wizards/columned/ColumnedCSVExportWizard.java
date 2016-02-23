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

	public ColumnedCSVExportWizard(ColumnedExportAction action, String title) {
		this(title);
		this.action = action;
	}

	public ColumnedCSVExportWizard(String title) {
		super();
		setNeedsProgressMonitor(true);
		setWindowTitle(title);
	}

	@Override
	public void addPages() {
		pages = action.getPages();
		for (int i = 0; i < pages.length; i++) {
			addPage(pages[i]);
		}
	}

	@Override
	public boolean performFinish() {
		try {
			return action.process();
		} catch (Exception e) {
			return false;
		}
	}
}
