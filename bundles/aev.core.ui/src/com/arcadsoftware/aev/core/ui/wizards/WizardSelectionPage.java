/*
 * Créé le 25 mai 04
 *
 * Pour changer le modèle de ce fichier généré, allez à :
 * Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
package com.arcadsoftware.aev.core.ui.wizards;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

/**
 * @author MD Pour changer le modèle de ce commentaire de type généré, allez à :
 *         Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
public abstract class WizardSelectionPage extends ArcadWizardPage {
	protected class SelectionChangedListener implements ISelectionChangedListener {
		/*
		 * (non-Javadoc)
		 * @see org.eclipse.jface.viewers.ISelectionChangedListener#selectionChanged
		 * (org.eclipse.jface.viewers.SelectionChangedEvent)
		 */
		@Override
		public void selectionChanged(final SelectionChangedEvent event) {
			setPageComplete(false);
			// Object selectedVersion = null;
			if (event.getSelection().isEmpty()) {
				return;
			}
			if (event.getSelection() instanceof IStructuredSelection) {
				doOnSelect((IStructuredSelection) event.getSelection());
				setPageComplete(checkSelection((IStructuredSelection) event.getSelection()));
			}
		}

	}

	Action doubleClickAction = null;
	protected SelectionChangedListener selectionChangedListener;

	protected StructuredViewer viewer;

	/**
	 * @param pageName
	 * @param title
	 */
	public WizardSelectionPage(final String pageName, final String title) {
		super(pageName, title);
		setPageComplete(false);
	}

	/**
	 * @param pageName
	 * @param title
	 * @param titleImage
	 */
	public WizardSelectionPage(final String pageName, final String title, final ImageDescriptor titleImage) {
		super(pageName, title, titleImage);
	}

	public abstract boolean checkSelection(IStructuredSelection sel);

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets .Composite)
	 */
	@Override
	public void createControl(final Composite arg0) {
		final Composite composite = new Composite(arg0, SWT.NONE);
		final GridLayout grid = new GridLayout();
		grid.numColumns = 1;
		composite.setLayout(grid);
		createExtendedControlBefore(composite);
		makeViewer(composite);

		if (viewer != null) {
			final GridData gridData = new GridData();
			gridData.verticalAlignment = GridData.FILL;
			gridData.horizontalAlignment = GridData.FILL;
			gridData.grabExcessVerticalSpace = true;
			gridData.grabExcessHorizontalSpace = true;
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
	 * @param parent
	 */
	protected void createExtendedControlAfter(final Composite parent) {
		// Do nothing
	}

	/**
	 * @param parent
	 */
	protected void createExtendedControlBefore(final Composite parent) {
		// Do nothing
	}

	protected void doOnDoubleClick() {
		// Do nothing
	}

	/**
	 * @param sel
	 */
	protected void doOnSelect(final IStructuredSelection sel) {
		// Do nothing
	}

	/**
	 * @return StructuredViewer
	 */
	public StructuredViewer getViewer() {
		return viewer;
	}

	private void hookDoubleClickAction() {
		viewer.addDoubleClickListener(event -> doubleClickAction.run());
	}

	public abstract void makeInput();

	public abstract void makeViewer(Composite composite);

}
