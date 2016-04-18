/*
 * Created on Jul 26, 2006
 */
package com.arcadsoftware.aev.core.ui.wizards;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;

import com.arcadsoftware.aev.core.ui.tools.GuiFormatTools;

/**
 * @author MD
 */
public abstract class AbstractSelectionPage extends AbstractSimpleItemWizardPage {

	protected StructuredViewer viewer;
	protected SelectionChangedListener selectionChangedListener;
	Action doubleClickAction = null;

	protected class SelectionChangedListener implements ISelectionChangedListener {
		public void selectionChanged(SelectionChangedEvent event) {
			setPageComplete(false);
			if (event.getSelection().isEmpty()) {
				return;
			}
			if (event.getSelection() instanceof IStructuredSelection) {
				doOnSelect((IStructuredSelection) event.getSelection());
				setPageComplete(checkSelection(((IStructuredSelection) event.getSelection())));
			}
		}

	}

	/**
	 * @param pageName
	 * @param title
	 * @param description
	 */
	public AbstractSelectionPage(String pageName, String title, String description) {
		super(pageName, title, description);
	}

	private void hookDoubleClickAction() {
		viewer.addDoubleClickListener(new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent event) {
				doubleClickAction.run();
			}
		});
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.arcadsoftware.aev.core.ui.wizards.AbstractSimpleItemWizardPage#
	 * createControlPage(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	protected void createControlPage(Composite parent) {
		Composite composite = GuiFormatTools.createComposite(parent);
		createExtendedControlBefore(composite);
		makeViewer(composite);
		if (viewer != null) {
			GridData gridData = new GridData();
			gridData.verticalAlignment = GridData.FILL;
			gridData.horizontalAlignment = GridData.FILL;
			gridData.grabExcessVerticalSpace = true;
			gridData.grabExcessHorizontalSpace = true;
			gridData.horizontalSpan = 3;
			viewer.getControl().setLayoutData(gridData);
			selectionChangedListener = new SelectionChangedListener();
			viewer.addSelectionChangedListener(selectionChangedListener);
		}
		createExtendedControlAfter(composite);
		setControl(composite);

		if (doubleClickAction == null) {
			doubleClickAction = new Action() {
				@Override
				public void run() {
					doOnDoubleClick();
				}
			};
		}
		hookDoubleClickAction();
	}

	/**
	 * @param sel
	 */
	protected void doOnSelect(IStructuredSelection sel) {
		// Do nothing
	}

	protected void doOnDoubleClick() {
		// Do nothing
	}

	/**
	 * @param parent
	 */
	protected void createExtendedControlBefore(Composite parent) {
		// Do nothing
	}

	/**
	 * @param parent
	 */
	protected void createExtendedControlAfter(Composite parent) {
		// Do nothing
	}

	public abstract boolean checkSelection(IStructuredSelection sel);

	public abstract void makeViewer(Composite composite);

	public abstract void makeInput();

}
