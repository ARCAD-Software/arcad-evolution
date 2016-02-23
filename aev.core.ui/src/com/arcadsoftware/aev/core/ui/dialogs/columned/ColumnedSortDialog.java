package com.arcadsoftware.aev.core.ui.dialogs.columned;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Shell;

import com.arcadsoftware.aev.core.tools.StringTools;
import com.arcadsoftware.aev.core.ui.EvolutionCoreUIPlugin;
import com.arcadsoftware.aev.core.ui.columned.model.ArcadColumns;
import com.arcadsoftware.aev.core.ui.columned.model.ColumnedSortCriteria;
import com.arcadsoftware.aev.core.ui.columned.model.ColumnedSortCriteriaList;
import com.arcadsoftware.aev.core.ui.tools.CoreUILabels;
import com.arcadsoftware.aev.core.ui.tools.GuiFormatTools;
import com.arcadsoftware.aev.core.ui.viewers.columned.impl.ColumnedSortTableViewer;

/**
 * @author dlelong
 */
public class ColumnedSortDialog extends ColumnedDialog {

	ColumnedSortTableViewer tableViewer;
	private ColumnedSortCriteriaList criteriaList;
	private ColumnedSortCriteria tempCriterion;
	private ArcadColumns referenceColumns;

	public ColumnedSortDialog(Shell parentShell, ColumnedSortCriteriaList criteriaList, ArcadColumns referenceColumns) {
		super(parentShell);
		this.referenceColumns = referenceColumns;
		this.criteriaList = criteriaList;
	}

	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText(CoreUILabels.resString("sorterDialog.title")); //$NON-NLS-1$        
	}

	protected void swap(boolean up) {
		IStructuredSelection selection = (IStructuredSelection) tableViewer.getSelection();
		ColumnedSortCriteria criterion = (ColumnedSortCriteria) selection.getFirstElement();
		if (criterion != null) {
			int selectedCriterionId = criterion.getId();
			for (int i = 0; i < criteriaList.getSize(); i++) {
				tempCriterion = (ColumnedSortCriteria) criteriaList.getItems(i);
				if (up) {
					if (selectedCriterionId == 1) {
						if (tempCriterion == criterion)
							criterion.setId(criteriaList.getSize());
						else
							tempCriterion.setId(tempCriterion.getId() - 1);
					} else {
						if (tempCriterion == criterion)
							criterion.setId(selectedCriterionId - 1);
						else if (tempCriterion.getId() == selectedCriterionId - 1)
							tempCriterion.setId(selectedCriterionId);
					}
				} else {
					if (selectedCriterionId == criteriaList.getSize()) {
						if (tempCriterion == criterion)
							criterion.setId(1);
						else
							tempCriterion.setId(tempCriterion.getId() + 1);
					} else {
						if (tempCriterion == criterion)
							criterion.setId(selectedCriterionId + 1);
						else if (tempCriterion.getId() == selectedCriterionId + 1)
							tempCriterion.setId(selectedCriterionId);
					}
				}
			}
		}
		tableViewer.setSelection(selection);
		tableViewer.refresh();
		criteriaList.orderCriterion();
	}

	protected void addPressed() {
		criteriaList.addCriterion();
		tableViewer.refresh();
		manageMoveButtons(tableViewer.getTable().getItemCount() > 1);
	}

	protected void removePressed() {

		ColumnedSortCriteria criterion = (ColumnedSortCriteria) ((IStructuredSelection) tableViewer.getSelection())
				.getFirstElement();
		if (criterion != null) {
			if (MessageDialog.openQuestion(this.getShell(), CoreUILabels
					.resString("messageBox.clm.removeCriterionText"), //$NON-NLS-1$
					CoreUILabels.resString("messageBox.clm.removeCriterionMessage")) //$NON-NLS-1$
			== true) {
				criteriaList.removeCriterion(criterion);
				for (int i = criterion.getId(); i < criteriaList.getSize() + 1; i++) {
					ColumnedSortCriteria criteria = (ColumnedSortCriteria) criteriaList.getItems(i - 1);
					criteria.setId(criteria.getId() - 1);
				}
			}
		}
		manageMoveButtons(tableViewer.getTable().getItemCount() > 1);
		tableViewer.refresh();
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		Group composite = new Group(container, SWT.NONE);
		composite.setText(CoreUILabels.resString("group.clm.CriteriaSearchManagement")); //$NON-NLS-1$
		composite.setLayout(new GridLayout(4, false));
		GridData gridData = new GridData(GridData.FILL_BOTH);
		gridData.heightHint = 200;
		composite.setLayoutData(gridData);
		tableViewer = new ColumnedSortTableViewer(composite, SWT.MULTI | SWT.BORDER | SWT.FULL_SELECTION
				| SWT.READ_ONLY | SWT.V_SCROLL | SWT.DefaultSelection, referenceColumns);
		tableViewer.setInput(criteriaList);

		gridData = new GridData(GridData.FILL_BOTH);
		gridData.verticalSpan = 2;
		tableViewer.getTable().setLayoutData(gridData);

		createToolbarButtonBar(composite);
		createNumberCriteriaManagementGroup(container);

		Dialog.applyDialogFont(container);
		return container;
	}

	private void createNumberCriteriaManagementGroup(Composite parent) {
		Composite container = GuiFormatTools.createComposite(parent, 6, true);

		addButton = GuiFormatTools.createButton(container,
				CoreUILabels.resString("button.clm.AddButton"), GridData.FILL_BOTH); //$NON-NLS-1$
		addButton.setToolTipText(CoreUILabels.resString("button.clm.AddButtonToolTipText")); //$NON-NLS-1$
		addButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				addPressed();
				tableViewer.getTable().setFocus();
			}
		});

		removeButton = GuiFormatTools.createButton(container,
				CoreUILabels.resString("button.clm.RemoveButton"), GridData.FILL_BOTH); //$NON-NLS-1$
		removeButton.setToolTipText(CoreUILabels.resString("button.clm.RemoveButtonToolTipText")); //$NON-NLS-1$
		removeButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				removePressed();
				tableViewer.getTable().setFocus();
			}
		});
	}

	private void createToolbarButtonBar(Composite parent) {
		upButton = GuiFormatTools.createButton(parent, StringTools.EMPTY, GridData.VERTICAL_ALIGN_BEGINNING);
		upButton.setImage(CoreUILabels.getImage(EvolutionCoreUIPlugin.ICO_CLM_SELECT_PREV));
		upButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				swap(true);
				tableViewer.getTable().setFocus();
			}
		});

		downButton = GuiFormatTools.createButton(parent, StringTools.EMPTY, GridData.VERTICAL_ALIGN_END);
		downButton.setImage(CoreUILabels.getImage(EvolutionCoreUIPlugin.ICO_CLM_SELECT_NEXT));
		downButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				swap(false);
				tableViewer.getTable().setFocus();
			}
		});

		manageMoveButtons(tableViewer.getTable().getItemCount() > 1);
	}

	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		okButton = createButton(parent, IDialogConstants.OK_ID, CoreUILabels.resString("button.clm.SortButton"), true); //$NON-NLS-1$
		okButton.addSelectionListener(new SelectionAdapter() {
			@SuppressWarnings("synthetic-access")
			@Override
			public void widgetSelected(SelectionEvent e) {
				okPressed();
			}
		});
		cancelButton = createButton(parent, IDialogConstants.CANCEL_ID, CoreUILabels
				.resString("button.clm.CancelButton"), true); //$NON-NLS-1$
		cancelButton.addSelectionListener(new SelectionAdapter() {
			@SuppressWarnings("synthetic-access")
			@Override
			public void widgetSelected(SelectionEvent e) {
				cancelPressed();
			}
		});
	}

	public ColumnedSortCriteriaList getCriteriaList() {
		return criteriaList;
	}
}
