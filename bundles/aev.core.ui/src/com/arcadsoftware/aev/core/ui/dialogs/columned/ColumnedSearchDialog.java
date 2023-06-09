package com.arcadsoftware.aev.core.ui.dialogs.columned;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Shell;

import com.arcadsoftware.aev.core.tools.StringTools;
import com.arcadsoftware.aev.core.ui.EvolutionCoreUIPlugin;
import com.arcadsoftware.aev.core.ui.IDocProvider;
import com.arcadsoftware.aev.core.ui.columned.model.ArcadColumns;
import com.arcadsoftware.aev.core.ui.columned.model.ColumnedSearchCriteria;
import com.arcadsoftware.aev.core.ui.columned.model.ColumnedSearchCriteriaList;
import com.arcadsoftware.aev.core.ui.tools.CoreUILabels;
import com.arcadsoftware.aev.core.ui.tools.GuiFormatTools;
import com.arcadsoftware.aev.core.ui.viewers.columned.impl.ColumnedSearchTableViewer;
import com.arcadsoftware.aev.icons.Icon;

/**
 * @author dlelong
 */
public class ColumnedSearchDialog extends ColumnedDialog {

	private final static String DIALOG_NAME = "ColumnedSearchDialog"; //$NON-NLS-1$
	protected static final String STORE_CASSE = IDocProvider.ID + ".CLMSearchDialog_CASSE";//$NON-NLS-1$
	boolean casse = false;
	Button casseButton;
	private final ColumnedSearchCriteriaList criteriaList;
	protected ArcadColumns referenceColumns;

	protected String removeCriterionMessage = "messageBox.clm.removeSearcherCriterionMessage"; //$NON-NLS-1$
	protected IDialogSettings settings;
	protected ColumnedSearchTableViewer tableViewer;
	protected ColumnedSearchCriteria tempCriterion;

	public ColumnedSearchDialog(final Shell parentShell, final ColumnedSearchCriteriaList criteriaList,
			final ArcadColumns referenceColumns) {
		super(parentShell);
		this.referenceColumns = referenceColumns;
		this.criteriaList = criteriaList;
		settings = EvolutionCoreUIPlugin.getSettings(getDialogName());
		if (settings.get(STORE_CASSE) == null) {
			initWidgetValues();
		}
	}

	protected void addPressed() {
		criteriaList.addCriterion();
		tableViewer.refresh();
		manageMoveButtons(tableViewer.getTable().getItemCount() > 1);
	}

	@Override
	protected void configureShell(final Shell newShell) {
		super.configureShell(newShell);
		newShell.setText(CoreUILabels.resString("searcherDialog.title")); //$NON-NLS-1$
	}

	@Override
	protected void createButtonsForButtonBar(final Composite parent) {
		okButton = createButton(parent, IDialogConstants.OK_ID, CoreUILabels.resString("button.clm.SearchButton"), //$NON-NLS-1$
				true);
		okButton.addSelectionListener(new SelectionAdapter() {
			@SuppressWarnings("synthetic-access")
			@Override
			public void widgetSelected(final SelectionEvent e) {
				okPressed();
			}
		});
		cancelButton = createButton(parent, IDialogConstants.CANCEL_ID, CoreUILabels
				.resString("button.clm.CancelButton"), true); //$NON-NLS-1$
		cancelButton.addSelectionListener(new SelectionAdapter() {
			@SuppressWarnings("synthetic-access")
			@Override
			public void widgetSelected(final SelectionEvent e) {
				cancelPressed();
			}
		});
	}

	@Override
	protected Control createDialogArea(final Composite parent) {
		final Composite container = (Composite) super.createDialogArea(parent);
		final Group composite = new Group(container, SWT.NONE);
		composite.setText(CoreUILabels.resString("group.clm.CriteriaSearchManagement")); //$NON-NLS-1$
		composite.setLayout(new GridLayout(4, false));
		GridData gridData = new GridData(GridData.FILL_BOTH);
		gridData.heightHint = 200;
		composite.setLayoutData(gridData);

		tableViewer = new ColumnedSearchTableViewer(composite, SWT.MULTI | SWT.BORDER | SWT.FULL_SELECTION
				| SWT.READ_ONLY | SWT.V_SCROLL | SWT.DefaultSelection, referenceColumns);
		tableViewer.setInput(criteriaList);

		gridData = new GridData(GridData.FILL_BOTH);
		gridData.verticalSpan = 2;
		tableViewer.getTable().setLayoutData(gridData);

		createToolbarButtonBar(composite);

		createNumberCriteriaManagementGroup(container);
		casseButton = GuiFormatTools.createCheckButton(container, CoreUILabels
				.resString("columnedSearchDialog.checkBox.casse"), //$NON-NLS-1$
				SWT.CENTER);
		casseButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				casse = casseButton.getSelection();
			}
		});
		if (settings != null) {
			loadWidgetValues(settings);
		}
		Dialog.applyDialogFont(container);
		return container;
	}

	private void createNumberCriteriaManagementGroup(final Composite parent) {
		final Composite container = GuiFormatTools.createComposite(parent, 6, true);

		addButton = GuiFormatTools.createButton(container,
				CoreUILabels.resString("button.clm.AddButton"), GridData.FILL_BOTH); //$NON-NLS-1$
		addButton.setToolTipText(CoreUILabels.resString("button.clm.AddButtonToolTipText")); //$NON-NLS-1$
		addButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				addPressed();
				tableViewer.getTable().setFocus();
			}
		});

		removeButton = GuiFormatTools.createButton(container,
				CoreUILabels.resString("button.clm.RemoveButton"), GridData.FILL_BOTH); //$NON-NLS-1$
		removeButton.setToolTipText(CoreUILabels.resString("button.clm.RemoveButtonToolTipText")); //$NON-NLS-1$
		removeButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				removePressed();
				tableViewer.getTable().setFocus();
			}
		});
	}

	private void createToolbarButtonBar(final Composite parent) {
		upButton = GuiFormatTools.createButton(parent, StringTools.EMPTY, GridData.VERTICAL_ALIGN_BEGINNING);
		upButton.setImage(Icon.PREVIOUS_ARROW.image());
		upButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				swap(true);
				tableViewer.getTable().setFocus();
			}
		});

		downButton = GuiFormatTools.createButton(parent, StringTools.EMPTY, GridData.VERTICAL_ALIGN_END);
		downButton.setImage(Icon.NEXT_ARROW.image());
		downButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				swap(false);
				tableViewer.getTable().setFocus();
			}
		});

		manageMoveButtons(tableViewer.getTable().getItemCount() > 1);
	}

	public ColumnedSearchCriteriaList getCriteriaList() {
		return criteriaList;
	}

	protected String getDialogName() {
		return DIALOG_NAME;
	}

	public IDialogSettings getSettings() {
		return settings;
	}

	protected void initWidgetValues() {
		settings.put(STORE_CASSE, isCasse());
	}

	public boolean isCasse() {
		return casse;
	}

	protected void loadWidgetValues(final IDialogSettings newSettings) {
		setCasse(newSettings.getBoolean(STORE_CASSE));
		casseButton.setSelection(newSettings.getBoolean(STORE_CASSE));
	}

	protected void removePressed() {
		final ColumnedSearchCriteria criterion = (ColumnedSearchCriteria) ((IStructuredSelection) tableViewer
				.getSelection())
						.getFirstElement();
		if (criterion != null) {
			if (MessageDialog.openQuestion(getShell(), CoreUILabels
					.resString("messageBox.clm.removeCriterionText"), //$NON-NLS-1$
					CoreUILabels.resString(removeCriterionMessage)) == true) {
				criteriaList.removeCriterion(criterion);
				for (int i = criterion.getId(); i < criteriaList.getSize() + 1; i++) {
					final ColumnedSearchCriteria criteria = (ColumnedSearchCriteria) criteriaList.getItems(i - 1);
					criteria.setId(criteria.getId() - 1);
				}
			}
		}
		tableViewer.refresh();
		manageMoveButtons(tableViewer.getTable().getItemCount() > 1);
	}

	public void saveWidgetValues(final IDialogSettings newSettings) {
		newSettings.put(STORE_CASSE, isCasse());
	}

	public void setCasse(final boolean casse) {
		this.casse = casse;
	}

	protected void swap(final boolean up) {
		final IStructuredSelection selection = (IStructuredSelection) tableViewer.getSelection();
		final ColumnedSearchCriteria criterion = (ColumnedSearchCriteria) selection.getFirstElement();
		if (criterion != null) {
			final int selectedCriterionId = criterion.getId();
			for (int i = 0; i < criteriaList.getSize(); i++) {
				tempCriterion = (ColumnedSearchCriteria) criteriaList.getItems(i);
				if (up) {
					if (selectedCriterionId == 1) {
						if (tempCriterion == criterion) {
							criterion.setId(criteriaList.getSize());
						} else {
							tempCriterion.setId(tempCriterion.getId() - 1);
						}
					} else {
						if (tempCriterion == criterion) {
							criterion.setId(selectedCriterionId - 1);
						} else if (tempCriterion.getId() == selectedCriterionId - 1) {
							tempCriterion.setId(selectedCriterionId);
						}
					}
				} else {
					if (selectedCriterionId == criteriaList.getSize()) {
						if (tempCriterion == criterion) {
							criterion.setId(1);
						} else {
							tempCriterion.setId(tempCriterion.getId() + 1);
						}
					} else {
						if (tempCriterion == criterion) {
							criterion.setId(selectedCriterionId + 1);
						} else if (tempCriterion.getId() == selectedCriterionId + 1) {
							tempCriterion.setId(selectedCriterionId);
						}
					}
				}
			}
		}
		tableViewer.setSelection(selection);
		tableViewer.refresh();
	}
}
