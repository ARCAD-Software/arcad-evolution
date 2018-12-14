/*
 * Créé le 25 mai 04
 *
 * Pour changer le modèle de ce fichier généré, allez à :
 * Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
package com.arcadsoftware.aev.core.ui.wizards;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

/**
 * @author MD
 * 
 *         Pour changer le modèle de ce commentaire de type généré, allez à :
 *         Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et
 *         commentaires
 */
public abstract class WizardSelectionPage extends ArcadWizardPage {
	protected StructuredViewer viewer;
	protected SelectionChangedListener selectionChangedListener;
	Action doubleClickAction = null;

	/**
	 * @param pageName
	 * @param title
	 */
	public WizardSelectionPage(String pageName, String title) {
		super(pageName, title);
		setPageComplete(false);
	}

	/**
	 * @param pageName
	 * @param title
	 * @param titleImage
	 */
	public WizardSelectionPage(String pageName, String title, ImageDescriptor titleImage) {
		super(pageName, title, titleImage);
	}

	public abstract void makeViewer(Composite composite);

	public abstract void makeInput();

	public abstract boolean checkSelection(IStructuredSelection sel);

	protected class SelectionChangedListener implements ISelectionChangedListener {
		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.eclipse.jface.viewers.ISelectionChangedListener#selectionChanged
		 * (org.eclipse.jface.viewers.SelectionChangedEvent)
		 */
		public void selectionChanged(SelectionChangedEvent event) {
			setPageComplete(false);
			// Object selectedVersion = null;
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

	/**
	 * @param sel
	 */
	protected void doOnSelect(IStructuredSelection sel) {
		// Do nothing
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets
	 * .Composite)
	 */
	public void createControl(Composite arg0) {
		Composite composite = new Composite(arg0, SWT.NONE);
		GridLayout grid = new GridLayout();
		grid.numColumns = 1;
		composite.setLayout(grid);
		createExtendedControlBefore(composite);
		makeViewer(composite);

		if (viewer != null) {
			GridData gridData = new GridData();
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
	 * @return StructuredViewer
	 */
	public StructuredViewer getViewer() {
		return viewer;
	}

	private void hookDoubleClickAction() {
		viewer.addDoubleClickListener(new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent event) {
				doubleClickAction.run();
			}
		});
	}

	protected void doOnDoubleClick() {
		// Do nothing
	}

}
