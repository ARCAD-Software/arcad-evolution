package com.arcadsoftware.aev.core.ui.dialogs.columned;

import java.util.ArrayList;
import java.util.Iterator;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.TypedEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

import com.arcadsoftware.aev.core.tools.StringTools;
import com.arcadsoftware.aev.core.ui.EvolutionCoreUIPlugin;
import com.arcadsoftware.aev.core.ui.columned.model.ArcadColumn;
import com.arcadsoftware.aev.core.ui.columned.model.ArcadColumns;
import com.arcadsoftware.aev.core.ui.dialogs.DialogConstantProvider;
import com.arcadsoftware.aev.core.ui.listeners.columned.IDialogListener;
import com.arcadsoftware.aev.core.ui.tools.CoreUILabels;
import com.arcadsoftware.aev.core.ui.tools.GuiFormatTools;

public class ColumnedDisplayDialog extends ColumnedDialog {
	private Button applyButton;
	private Button defaultButton;
	private Button displayedButton;
	private Button allDisplayedButton;
	private Button maskedButton;
	private Button allMaskedButton;
	private Button reinitializeButton;

	Label headerDefault;
	Text headerUserText;
	private Composite container;

	protected static int DEFAULT_ID = 46;
	protected static int APPLY_ID = 47;

	ArcadColumns referenceColumns;
	ArcadColumns displayedColumns = new ArcadColumns();
	ArcadColumns maskedColumns = new ArcadColumns();

	private ArrayList<IDialogListener> listeners = new ArrayList<IDialogListener>();

	Table maskedIdentifierTable;
	private Table displayedIdentifierTable;

	private ArcadColumn selectedColumn = new ArcadColumn();

	public ColumnedDisplayDialog(Shell parentShell) {
		super(parentShell);
	}

	public ColumnedDisplayDialog(Shell parentShell, ArcadColumns displayedColumns) {
		super(parentShell);
		this.referenceColumns = displayedColumns;
		initColumns();
	}

	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText(CoreUILabels.resString("display.preferences.title")); //$NON-NLS-1$
		int width = 700;
		int height = 350;
		newShell.setSize(width, height);
		Rectangle parentBounds = newShell.getDisplay().getClientArea();
		int x = parentBounds.x + (parentBounds.width - width) / 2;
		int y = parentBounds.y + (parentBounds.height - height) / 2;
		newShell.setLocation(x, y);
	}

	public void initColumns() {
		maskedColumns = new ArcadColumns();
		Iterator<?> it = referenceColumns.getList().iterator();
		while (it.hasNext()) {
			ArcadColumn column = (ArcadColumn) it.next();
			if (column.getVisible() == ArcadColumn.VISIBLE)
				displayedColumns.add(column);
			else
				maskedColumns.add(column);
		}
	}

	private void manageDisplayButton(boolean flag) {
		displayedButton.setEnabled(flag);
		allDisplayedButton.setEnabled(flag);
	}

	private void manageHideButton(boolean flag) {
		maskedButton.setEnabled(flag);
		allMaskedButton.setEnabled(flag);
	}

	void updateTableItem(Table table, ArcadColumns cols, int index) {
		table.getItem(index).setText(0, cols.items(index).getLabel());
	}

	void swap(boolean up) {
		if (getDisplayedIdentifierTable().getSelectionCount() != 0) {
			int sourceIndex = getDisplayedIdentifierTable().getSelectionIndex();
			int targetIndex = sourceIndex;
			if (sourceIndex == 0)
				return;
			if (up) {
				if (sourceIndex == 1)
					targetIndex = getDisplayedIdentifierTable().getItemCount() - 1;
				else
					targetIndex--;
			} else {
				if (sourceIndex == getDisplayedIdentifierTable().getItemCount() - 1)
					targetIndex = 1;
				else
					targetIndex++;
			}
			displayedColumns.swap(sourceIndex, targetIndex);
			updateTableItem(getDisplayedIdentifierTable(), displayedColumns, sourceIndex);
			updateTableItem(getDisplayedIdentifierTable(), displayedColumns, targetIndex);
			getDisplayedIdentifierTable().setSelection(targetIndex);
			getDisplayedIdentifierTable().redraw();
		}
	}

	void displayColumnAtIndex(int index) {
		ArcadColumn colToShow = maskedColumns.items(index);
		maskedColumns.delete(index);
		maskedIdentifierTable.remove(index);

		TableItem tableItem = new TableItem(getDisplayedIdentifierTable(), SWT.NONE);
		tableItem.setText(0, colToShow.getLabel());
		displayedColumns.add(colToShow);
		colToShow.setVisible(ArcadColumn.VISIBLE);
		manageDisplayButton(maskedColumns.count() > 0);
		manageHideButton(displayedColumns.count() > 1);
		manageMoveButtons(displayedColumns.count() > 2);
	}

	void hideColumnAtIndex(int index) {
		if (index != 0) {
			ArcadColumn colToHide = displayedColumns.items(index);
			displayedColumns.delete(index);
			getDisplayedIdentifierTable().remove(index);

			TableItem tableItem = new TableItem(maskedIdentifierTable, SWT.NONE);
			tableItem.setText(0, colToHide.getLabel());
			maskedColumns.add(colToHide);
			colToHide.setVisible(ArcadColumn.HIDDEN);
			manageDisplayButton(maskedColumns.count() > 0);
			manageHideButton(displayedColumns.count() > 1);
			manageMoveButtons(displayedColumns.count() > 2);
		}
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		container = (Composite) super.createDialogArea(parent);
		createListHeaderGroup(container);
		createHeaderManagementGroup(container);
		Dialog.applyDialogFont(container);
		return container;
	}

	private void createHeaderManagementGroup(Composite parent) {
		Group group = GuiFormatTools.createGroup(parent, CoreUILabels.resString("group.clm.HeaderManagementGroup")); //$NON-NLS-1$
		headerDefault = GuiFormatTools.createLabelledLabel(group, CoreUILabels
				.resString("label.clm.DefaultHeaderLabel")); //$NON-NLS-1$
		headerUserText = GuiFormatTools.createLabelledText(group, CoreUILabels.resString("label.clm.UserHeaderLabel")); //$NON-NLS-1$
		headerUserText.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.keyCode == SWT.CR) {
					changeHeader(e);
					if (getSelectedColumn().getVisible() == 0)
						getDisplayedIdentifierTable().forceFocus();
					else
						maskedIdentifierTable.forceFocus();
				}
			}
		});

		headerUserText.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				changeHeader(e);
			}
		});
	}

	void changeHeader(TypedEvent e) {
		Text text = (Text) e.widget;
		if (!text.getText().equals(StringTools.EMPTY)) {
			if (!isUniqueHeader(text.getText(), getSelectedColumn().getIdentifier())) {
				MessageDialog.openError(this.getShell(),
						CoreUILabels.resString("messageBox.clm.renameHeaderErrorText"), //$NON-NLS-1$
						CoreUILabels.resString("messageBox.clm.renameHeaderErrorMessage")); //$NON-NLS-1$
			} else {
				getSelectedColumn().setUserName(text.getText());
				referenceColumns.items(getSelectedColumn().getIdentifier()).setUserName(text.getText());
				if (getSelectedColumn().getVisible() == ArcadColumn.VISIBLE)
					updateTableItem(getDisplayedIdentifierTable(), displayedColumns, getDisplayedIdentifierTable()
							.getSelectionIndex());
				else {
					if (maskedIdentifierTable.getSelectionIndex() >= 0)
						updateTableItem(maskedIdentifierTable, maskedColumns, maskedIdentifierTable.getSelectionIndex());
				}
			}
		}
	}

	private boolean isUniqueHeader(String header, String identifier) {
		ArcadColumn column = new ArcadColumn();
		for (int i = 0; i < referenceColumns.count(); i++) {
			column = referenceColumns.items(i);
			if (column.getName().equalsIgnoreCase(header) && !column.getIdentifier().equalsIgnoreCase(identifier))
				return false;
		}
		return true;
	}

	private void createListHeaderGroup(Composite parent) {
		Group containerHeader = GuiFormatTools.createGroup(parent, CoreUILabels
				.resString("group.clm.HeaderListManagement")); //$NON-NLS-1$
		Composite composite = GuiFormatTools.createComposite(containerHeader, 4, false);
		maskedIdentifierTable = new Table(composite, SWT.BORDER | SWT.FULL_SELECTION | SWT.V_SCROLL | SWT.H_SCROLL);
		TableColumn maskedColumnColumn = new TableColumn(maskedIdentifierTable, SWT.SIMPLE);
		maskedColumnColumn.setWidth(250);
		maskedColumnColumn.setAlignment(SWT.LEFT);
		maskedColumnColumn.setText(CoreUILabels.resString("label.clm.MaskedColumnLabel")); //$NON-NLS-1$
		maskedIdentifierTable.setHeaderVisible(true);
		TableItem tableItem;
		for (int i = 0; i < maskedColumns.getIdentifiers().length; i++) {
			tableItem = new TableItem(maskedIdentifierTable, SWT.NONE);
			tableItem.setText(0, maskedColumns.items(i).getLabel());
		}

		maskedIdentifierTable.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				getDisplayedIdentifierTable().deselectAll();
				ArcadColumn column = maskedColumns.items(ColumnedDisplayDialog.this.maskedIdentifierTable
						.getSelectionIndex());
				headerDefault.setText(column.getName());
				headerUserText.setText(column.getUserName());
				setSelectedColumn(column);
			}
		});

		GridData gridData = new GridData(GridData.FILL_BOTH);
		gridData.verticalSpan = 6;
		maskedIdentifierTable.setLayoutData(gridData);

		createToolbarMoveButtonBar(composite);

		composite = GuiFormatTools.createComposite(containerHeader, 4, false);
		setDisplayedIdentifierTable(new Table(composite, SWT.BORDER | SWT.FULL_SELECTION | SWT.V_SCROLL | SWT.H_SCROLL));
		TableColumn displayedColumnColumn = new TableColumn(getDisplayedIdentifierTable(), SWT.NONE);
		displayedColumnColumn.setText(CoreUILabels.resString("label.clm.VisbleColumnLabel")); //$NON-NLS-1$
		displayedColumnColumn.setWidth(250);
		getDisplayedIdentifierTable().setHeaderVisible(true);
		for (int i = 0; i < displayedColumns.getIdentifiers().length; i++) {
			tableItem = new TableItem(getDisplayedIdentifierTable(), SWT.NONE);
			tableItem.setText(0, displayedColumns.items(i).getLabel());
		}
		getDisplayedIdentifierTable().getItem(0).setBackground(new Color(this.getShell().getDisplay(), 230, 230, 230));

		getDisplayedIdentifierTable().addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				maskedIdentifierTable.deselectAll();
				ArcadColumn column = displayedColumns.items(ColumnedDisplayDialog.this.getDisplayedIdentifierTable()
						.getSelectionIndex());
				headerDefault.setText(column.getName());
				headerUserText.setText(column.getUserName());
				setSelectedColumn(column);
			}
		});
		gridData = new GridData(GridData.FILL_BOTH);
		gridData.verticalSpan = 2;
		getDisplayedIdentifierTable().setLayoutData(gridData);
		createToolbarButtonBar(composite);
		manageMoveButtons(getDisplayedIdentifierTable().getItemCount() > 2);
	}

	private void createToolbarButtonBar(Composite parent) {
		upButton = GuiFormatTools.createButton(parent, StringTools.EMPTY, GridData.VERTICAL_ALIGN_BEGINNING);
		upButton.setImage(CoreUILabels.getImage(EvolutionCoreUIPlugin.ICO_CLM_SELECT_PREV));
		upButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				swap(true);
			}
		});
		downButton = GuiFormatTools.createButton(parent, StringTools.EMPTY, GridData.VERTICAL_ALIGN_END);
		downButton.setImage(CoreUILabels.getImage(EvolutionCoreUIPlugin.ICO_CLM_SELECT_NEXT));
		downButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				swap(false);
			}
		});
	}

	private void createToolbarMoveButtonBar(Composite parent) {
		displayedButton = GuiFormatTools.createButton(parent, StringTools.EMPTY, GridData.VERTICAL_ALIGN_CENTER
				| GridData.HORIZONTAL_ALIGN_CENTER);
		displayedButton.setImage(CoreUILabels.getImage(EvolutionCoreUIPlugin.ICO_CLM_ADD));
		displayedButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				int selectedIndex = maskedIdentifierTable.getSelectionIndex();
				if (selectedIndex != -1)
					displayColumnAtIndex(selectedIndex);
			}
		});

		allDisplayedButton = GuiFormatTools.createButton(parent, StringTools.EMPTY, GridData.VERTICAL_ALIGN_CENTER
				| GridData.HORIZONTAL_ALIGN_CENTER);
		allDisplayedButton.setImage(CoreUILabels.getImage(EvolutionCoreUIPlugin.ICO_CLM_ADD_ALL));
		allDisplayedButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				while (maskedColumns.count() > 0) {
					displayColumnAtIndex(0);
				}
			}
		});

		maskedButton = GuiFormatTools.createButton(parent, StringTools.EMPTY, GridData.VERTICAL_ALIGN_CENTER
				| GridData.HORIZONTAL_ALIGN_CENTER);
		maskedButton.setImage(CoreUILabels.getImage(EvolutionCoreUIPlugin.ICO_CLM_REMOVE));
		maskedButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				int selectedIndex = getDisplayedIdentifierTable().getSelectionIndex();
				if (selectedIndex > 0)
					hideColumnAtIndex(selectedIndex);
			}
		});

		allMaskedButton = GuiFormatTools.createButton(parent, StringTools.EMPTY, GridData.VERTICAL_ALIGN_CENTER
				| GridData.HORIZONTAL_ALIGN_CENTER);
		allMaskedButton.setImage(CoreUILabels.getImage(EvolutionCoreUIPlugin.ICO_CLM_REMOVE_ALL));
		allMaskedButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				while (displayedColumns.count() > 1) {
					hideColumnAtIndex(1);
				}
			}
		});
	}

	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		defaultButton = createButton(parent, DEFAULT_ID, CoreUILabels.resString("button.clm.DefaultButton"), false); //$NON-NLS-1$
		defaultButton.setToolTipText(CoreUILabels.resString("text.clm.DefaultButtonToolTipText")); //$NON-NLS-1$
		defaultButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (MessageDialog.openConfirm(ColumnedDisplayDialog.this.getShell(), CoreUILabels
						.resString("messageBox.clm.defaultHeaderText"), //$NON-NLS-1$
						CoreUILabels.resString("messageBox.clm.defaultHeaderMessage"))) { //$NON-NLS-1$
					for (int i = 0; i < displayedColumns.count(); i++) {
						displayedColumns.items(i).setUserName(displayedColumns.items(i).getName());
						updateTableItem(getDisplayedIdentifierTable(), displayedColumns, i);
					}
					for (int i = 0; i < maskedColumns.count(); i++) {
						maskedColumns.items(i).setUserName(maskedColumns.items(i).getName());
						updateTableItem(maskedIdentifierTable, maskedColumns, i);
					}
				}
			}
		});

		reinitializeButton = createButton(parent, IDialogConstants.OK_ID, CoreUILabels
				.resString("button.clm.reinitializeButton"), false); //$NON-NLS-1$
		reinitializeButton.addSelectionListener(new SelectionAdapter() {
			@SuppressWarnings("synthetic-access")
			@Override
			public void widgetSelected(SelectionEvent e) {
				referenceColumns = null;
				okPressed();
			}
		});
		reinitializeButton.setToolTipText(CoreUILabels.resString("button.clm.reinitializeButton.tooltip")); //$NON-NLS-1$
		okButton = createButton(parent, IDialogConstants.OK_ID,  DialogConstantProvider.getInstance().OK_LABEL, false);
		okButton.addSelectionListener(new SelectionAdapter() {
			@SuppressWarnings("synthetic-access")
			@Override
			public void widgetSelected(SelectionEvent e) {
				okPressed();
			}
		});
		cancelButton = createButton(parent, IDialogConstants.CANCEL_ID, CoreUILabels
				.resString("button.clm.CancelButton"), false); //$NON-NLS-1$
		applyButton = createButton(parent, APPLY_ID, CoreUILabels.resString("button.clm.ApplyButton"), false); //$NON-NLS-1$
		applyButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				fireDialogListener();
				// TODO verifier si le ArcadColumns du settings contient
				// bien la bonne reference pour lancer le save
				// ColumnedViewerMementoTools.getInstance().save();
			}
		});
	}

	public ArcadColumns getReferenceColumns() {
		return referenceColumns;
	}

	public void addListener(IDialogListener listener) {
		listeners.add(listener);
	}

	public void removeListener(IDialogListener listener) {
		listeners.remove(listener);
	}

	public void fireDialogListener() {
		Iterator<IDialogListener> it = listeners.iterator();
		while (it.hasNext())
			((IDialogListener) it.next()).doOnApply();
	}

	public void setSelectedColumn(ArcadColumn selectedColumn) {
		this.selectedColumn = selectedColumn;
	}

	public ArcadColumn getSelectedColumn() {
		return selectedColumn;
	}

	public void setDisplayedIdentifierTable(Table displayedIdentifierTable) {
		this.displayedIdentifierTable = displayedIdentifierTable;
	}

	public Table getDisplayedIdentifierTable() {
		return displayedIdentifierTable;
	}
}
