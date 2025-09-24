/*******************************************************************************
 * Copyright (c) 2025 ARCAD Software.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     ARCAD Software - initial API and implementation
 *******************************************************************************/
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
import com.arcadsoftware.aev.core.ui.columned.model.ArcadColumn;
import com.arcadsoftware.aev.core.ui.columned.model.ArcadColumns;
import com.arcadsoftware.aev.core.ui.dialogs.DialogConstantProvider;
import com.arcadsoftware.aev.core.ui.listeners.columned.IDialogListener;
import com.arcadsoftware.aev.core.ui.tools.CoreUILabels;
import com.arcadsoftware.aev.core.ui.tools.GuiFormatTools;
import com.arcadsoftware.aev.icons.Icon;

public class ColumnedDisplayDialog extends ColumnedDialog {
	protected static int APPLY_ID = 47;
	protected static int DEFAULT_ID = 46;
	private Button allDisplayedButton;
	private Button allMaskedButton;
	private Button applyButton;
	private Composite container;
	private Button defaultButton;

	private Button displayedButton;
	ArcadColumns displayedColumns = new ArcadColumns();
	private Table displayedIdentifierTable;

	Label headerDefault;
	Text headerUserText;

	private final ArrayList<IDialogListener> listeners = new ArrayList<>();
	private Button maskedButton;
	ArcadColumns maskedColumns = new ArcadColumns();

	Table maskedIdentifierTable;

	ArcadColumns referenceColumns;
	private Button reinitializeButton;

	private ArcadColumn selectedColumn = new ArcadColumn();

	public ColumnedDisplayDialog(final Shell parentShell) {
		super(parentShell);
	}

	public ColumnedDisplayDialog(final Shell parentShell, final ArcadColumns displayedColumns) {
		super(parentShell);
		referenceColumns = displayedColumns;
		initColumns();
	}

	public void addListener(final IDialogListener listener) {
		listeners.add(listener);
	}

	void changeHeader(final TypedEvent e) {
		final Text text = (Text) e.widget;
		if (!text.getText().equals(StringTools.EMPTY)) {
			if (!isUniqueHeader(text.getText(), getSelectedColumn().getIdentifier())) {
				MessageDialog.openError(getShell(),
						CoreUILabels.resString("messageBox.clm.renameHeaderErrorText"), //$NON-NLS-1$
						CoreUILabels.resString("messageBox.clm.renameHeaderErrorMessage")); //$NON-NLS-1$
			} else {
				getSelectedColumn().setUserName(text.getText());
				referenceColumns.items(getSelectedColumn().getIdentifier()).setUserName(text.getText());
				if (getSelectedColumn().getVisible() == ArcadColumn.VISIBLE) {
					updateTableItem(getDisplayedIdentifierTable(), displayedColumns, getDisplayedIdentifierTable()
							.getSelectionIndex());
				} else {
					if (maskedIdentifierTable.getSelectionIndex() >= 0) {
						updateTableItem(maskedIdentifierTable, maskedColumns,
								maskedIdentifierTable.getSelectionIndex());
					}
				}
			}
		}
	}

	@Override
	protected void configureShell(final Shell newShell) {
		super.configureShell(newShell);
		newShell.setText(CoreUILabels.resString("display.preferences.title")); //$NON-NLS-1$
		final int width = 700;
		final int height = 350;
		newShell.setSize(width, height);
		final Rectangle parentBounds = newShell.getDisplay().getClientArea();
		final int x = parentBounds.x + (parentBounds.width - width) / 2;
		final int y = parentBounds.y + (parentBounds.height - height) / 2;
		newShell.setLocation(x, y);
	}

	@Override
	protected void createButtonsForButtonBar(final Composite parent) {
		defaultButton = createButton(parent, DEFAULT_ID, CoreUILabels.resString("button.clm.DefaultButton"), false); //$NON-NLS-1$
		defaultButton.setToolTipText(CoreUILabels.resString("text.clm.DefaultButtonToolTipText")); //$NON-NLS-1$
		defaultButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
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
			public void widgetSelected(final SelectionEvent e) {
				referenceColumns = null;
				okPressed();
			}
		});
		reinitializeButton.setToolTipText(CoreUILabels.resString("button.clm.reinitializeButton.tooltip")); //$NON-NLS-1$
		okButton = createButton(parent, IDialogConstants.OK_ID, DialogConstantProvider.getInstance().OK_LABEL, false);
		okButton.addSelectionListener(new SelectionAdapter() {
			@SuppressWarnings("synthetic-access")
			@Override
			public void widgetSelected(final SelectionEvent e) {
				okPressed();
			}
		});
		cancelButton = createButton(parent, IDialogConstants.CANCEL_ID, CoreUILabels
				.resString("button.clm.CancelButton"), false); //$NON-NLS-1$
		applyButton = createButton(parent, APPLY_ID, CoreUILabels.resString("button.clm.ApplyButton"), false); //$NON-NLS-1$
		applyButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				fireDialogListener();
				// TODO verifier si le ArcadColumns du settings contient
				// bien la bonne reference pour lancer le save
				// ColumnedViewerMementoTools.getInstance().save();
			}
		});
	}

	@Override
	protected Control createDialogArea(final Composite parent) {
		container = (Composite) super.createDialogArea(parent);
		createListHeaderGroup(container);
		createHeaderManagementGroup(container);
		Dialog.applyDialogFont(container);
		return container;
	}

	private void createHeaderManagementGroup(final Composite parent) {
		final Group group = GuiFormatTools.createGroup(parent,
				CoreUILabels.resString("group.clm.HeaderManagementGroup")); //$NON-NLS-1$
		headerDefault = GuiFormatTools.createLabelledLabel(group, CoreUILabels
				.resString("label.clm.DefaultHeaderLabel")); //$NON-NLS-1$
		headerUserText = GuiFormatTools.createLabelledText(group, CoreUILabels.resString("label.clm.UserHeaderLabel")); //$NON-NLS-1$
		headerUserText.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(final KeyEvent e) {
				if (e.keyCode == SWT.CR) {
					changeHeader(e);
					if (getSelectedColumn().getVisible() == 0) {
						getDisplayedIdentifierTable().forceFocus();
					} else {
						maskedIdentifierTable.forceFocus();
					}
				}
			}
		});

		headerUserText.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(final FocusEvent e) {
				changeHeader(e);
			}
		});
	}

	private void createListHeaderGroup(final Composite parent) {
		final Group containerHeader = GuiFormatTools.createGroup(parent, CoreUILabels
				.resString("group.clm.HeaderListManagement")); //$NON-NLS-1$
		Composite composite = GuiFormatTools.createComposite(containerHeader, 4, false);
		maskedIdentifierTable = new Table(composite, SWT.BORDER | SWT.FULL_SELECTION | SWT.V_SCROLL | SWT.H_SCROLL);
		final TableColumn maskedColumnColumn = new TableColumn(maskedIdentifierTable, SWT.SIMPLE);
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
			public void widgetSelected(final SelectionEvent e) {
				getDisplayedIdentifierTable().deselectAll();
				final ArcadColumn column = maskedColumns.items(maskedIdentifierTable
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
		setDisplayedIdentifierTable(
				new Table(composite, SWT.BORDER | SWT.FULL_SELECTION | SWT.V_SCROLL | SWT.H_SCROLL));
		final TableColumn displayedColumnColumn = new TableColumn(getDisplayedIdentifierTable(), SWT.NONE);
		displayedColumnColumn.setText(CoreUILabels.resString("label.clm.VisbleColumnLabel")); //$NON-NLS-1$
		displayedColumnColumn.setWidth(250);
		getDisplayedIdentifierTable().setHeaderVisible(true);
		for (int i = 0; i < displayedColumns.getIdentifiers().length; i++) {
			tableItem = new TableItem(getDisplayedIdentifierTable(), SWT.NONE);
			tableItem.setText(0, displayedColumns.items(i).getLabel());
		}
		getDisplayedIdentifierTable().getItem(0).setBackground(new Color(getShell().getDisplay(), 230, 230, 230));

		getDisplayedIdentifierTable().addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				maskedIdentifierTable.deselectAll();
				final ArcadColumn column = displayedColumns
						.items(ColumnedDisplayDialog.this.getDisplayedIdentifierTable()
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

	private void createToolbarButtonBar(final Composite parent) {
		upButton = GuiFormatTools.createButton(parent, StringTools.EMPTY, GridData.VERTICAL_ALIGN_BEGINNING);
		upButton.setImage(Icon.PREVIOUS_ARROW.image());
		upButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				swap(true);
			}
		});
		downButton = GuiFormatTools.createButton(parent, StringTools.EMPTY, GridData.VERTICAL_ALIGN_END);
		downButton.setImage(Icon.NEXT_ARROW.image());
		downButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				swap(false);
			}
		});
	}

	private void createToolbarMoveButtonBar(final Composite parent) {
		displayedButton = GuiFormatTools.createButton(parent, StringTools.EMPTY, GridData.VERTICAL_ALIGN_CENTER
				| GridData.HORIZONTAL_ALIGN_CENTER);
		displayedButton.setImage(Icon.ADD.image());
		displayedButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				final int selectedIndex = maskedIdentifierTable.getSelectionIndex();
				if (selectedIndex != -1) {
					displayColumnAtIndex(selectedIndex);
				}
			}
		});

		allDisplayedButton = GuiFormatTools.createButton(parent, StringTools.EMPTY, GridData.VERTICAL_ALIGN_CENTER
				| GridData.HORIZONTAL_ALIGN_CENTER);
		allDisplayedButton.setImage(Icon.ADD_ALL.image());
		allDisplayedButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				while (maskedColumns.count() > 0) {
					displayColumnAtIndex(0);
				}
			}
		});

		maskedButton = GuiFormatTools.createButton(parent, StringTools.EMPTY, GridData.VERTICAL_ALIGN_CENTER
				| GridData.HORIZONTAL_ALIGN_CENTER);
		maskedButton.setImage(Icon.REMOVE.image());
		maskedButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				final int selectedIndex = getDisplayedIdentifierTable().getSelectionIndex();
				if (selectedIndex > 0) {
					hideColumnAtIndex(selectedIndex);
				}
			}
		});

		allMaskedButton = GuiFormatTools.createButton(parent, StringTools.EMPTY, GridData.VERTICAL_ALIGN_CENTER
				| GridData.HORIZONTAL_ALIGN_CENTER);
		allMaskedButton.setImage(Icon.REMOVE_ALL.image());
		allMaskedButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				while (displayedColumns.count() > 1) {
					hideColumnAtIndex(1);
				}
			}
		});
	}

	void displayColumnAtIndex(final int index) {
		final ArcadColumn colToShow = maskedColumns.items(index);
		maskedColumns.delete(index);
		maskedIdentifierTable.remove(index);

		final TableItem tableItem = new TableItem(getDisplayedIdentifierTable(), SWT.NONE);
		tableItem.setText(0, colToShow.getLabel());
		displayedColumns.add(colToShow);
		colToShow.setVisible(ArcadColumn.VISIBLE);
		manageDisplayButton(maskedColumns.count() > 0);
		manageHideButton(displayedColumns.count() > 1);
		manageMoveButtons(displayedColumns.count() > 2);
	}

	public void fireDialogListener() {
		final Iterator<IDialogListener> it = listeners.iterator();
		while (it.hasNext()) {
			it.next().doOnApply();
		}
	}

	public Table getDisplayedIdentifierTable() {
		return displayedIdentifierTable;
	}

	public ArcadColumns getReferenceColumns() {
		return referenceColumns;
	}

	public ArcadColumn getSelectedColumn() {
		return selectedColumn;
	}

	void hideColumnAtIndex(final int index) {
		if (index != 0) {
			final ArcadColumn colToHide = displayedColumns.items(index);
			displayedColumns.delete(index);
			getDisplayedIdentifierTable().remove(index);

			final TableItem tableItem = new TableItem(maskedIdentifierTable, SWT.NONE);
			tableItem.setText(0, colToHide.getLabel());
			maskedColumns.add(colToHide);
			colToHide.setVisible(ArcadColumn.HIDDEN);
			manageDisplayButton(maskedColumns.count() > 0);
			manageHideButton(displayedColumns.count() > 1);
			manageMoveButtons(displayedColumns.count() > 2);
		}
	}

	public void initColumns() {
		maskedColumns = new ArcadColumns();
		final Iterator<?> it = referenceColumns.getList().iterator();
		while (it.hasNext()) {
			final ArcadColumn column = (ArcadColumn) it.next();
			if (column.getVisible() == ArcadColumn.VISIBLE) {
				displayedColumns.add(column);
			} else {
				maskedColumns.add(column);
			}
		}
	}

	private boolean isUniqueHeader(final String header, final String identifier) {
		ArcadColumn column = new ArcadColumn();
		for (int i = 0; i < referenceColumns.count(); i++) {
			column = referenceColumns.items(i);
			if (column.getName().equalsIgnoreCase(header) && !column.getIdentifier().equalsIgnoreCase(identifier)) {
				return false;
			}
		}
		return true;
	}

	private void manageDisplayButton(final boolean flag) {
		displayedButton.setEnabled(flag);
		allDisplayedButton.setEnabled(flag);
	}

	private void manageHideButton(final boolean flag) {
		maskedButton.setEnabled(flag);
		allMaskedButton.setEnabled(flag);
	}

	public void removeListener(final IDialogListener listener) {
		listeners.remove(listener);
	}

	public void setDisplayedIdentifierTable(final Table displayedIdentifierTable) {
		this.displayedIdentifierTable = displayedIdentifierTable;
	}

	public void setSelectedColumn(final ArcadColumn selectedColumn) {
		this.selectedColumn = selectedColumn;
	}

	void swap(final boolean up) {
		if (getDisplayedIdentifierTable().getSelectionCount() != 0) {
			final int sourceIndex = getDisplayedIdentifierTable().getSelectionIndex();
			int targetIndex = sourceIndex;
			if (sourceIndex == 0) {
				return;
			}
			if (up) {
				if (sourceIndex == 1) {
					targetIndex = getDisplayedIdentifierTable().getItemCount() - 1;
				} else {
					targetIndex--;
				}
			} else {
				if (sourceIndex == getDisplayedIdentifierTable().getItemCount() - 1) {
					targetIndex = 1;
				} else {
					targetIndex++;
				}
			}
			displayedColumns.swap(sourceIndex, targetIndex);
			updateTableItem(getDisplayedIdentifierTable(), displayedColumns, sourceIndex);
			updateTableItem(getDisplayedIdentifierTable(), displayedColumns, targetIndex);
			getDisplayedIdentifierTable().setSelection(targetIndex);
			getDisplayedIdentifierTable().redraw();
		}
	}

	void updateTableItem(final Table table, final ArcadColumns cols, final int index) {
		table.getItem(index).setText(0, cols.items(index).getLabel());
	}
}
