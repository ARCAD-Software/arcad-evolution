package com.arcadsoftware.aev.core.ui.dialogs.gui;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

import com.arcadsoftware.aev.core.ui.EvolutionCoreUIPlugin;
import com.arcadsoftware.aev.core.ui.dialogs.ArcadCenteredDialog;
import com.arcadsoftware.aev.core.ui.tools.GuiFormatTools;

/**
 * @author SJU
 */
public abstract class ListSelectionDialog<E> extends ArcadCenteredDialog {

	private final List<E> displayedList;
	private Text filter;
	private final Image icon;

	private final List<E> originalList;
	private final List<E> selection;

	private TableViewer table;

	public ListSelectionDialog(final Shell parentShell, final String title, final Image icon, final Point size,
			final List<E> input) {
		super(parentShell, size.x, size.y, title);
		this.icon = icon;
		this.originalList = input;
		this.displayedList = new ArrayList<>(originalList);
		this.selection = new ArrayList<>();
		setShellStyle(getShellStyle() | SWT.RESIZE);
	}

	protected boolean canFilter() {
		return true;
	}

	@Override
	protected void configureShell(final Shell newShell) {
		super.configureShell(newShell);
		if (this.icon != null) {
			newShell.setImage(icon);
		}
	}

	@Override
	protected Control createDialogArea(final Composite parent) {
		final Composite composite = (Composite) super.createDialogArea(parent);
		final GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 1;
		composite.setLayout(gridLayout);

		if (canFilter()) {
			filter = GuiFormatTools.createLabelledText(composite,
					EvolutionCoreUIPlugin.getResourceString("filterDialog.title"));
			filter.addModifyListener(e -> {
				final String filterText = filter.getText().trim().toLowerCase();
				if (!filterText.isEmpty()) {
					displayedList.clear();
					for (final E entity : originalList) {
						if (getEntityFieldToDisplay(entity).toLowerCase().startsWith(filterText)) {
							displayedList.add(entity);
						}
					}
				} else {
					displayedList.addAll(originalList);
				}
				table.refresh();
			});
			filter.setFocus();
		}

		table = new TableViewer(composite, SWT.MULTI | SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.BORDER);
		table.setContentProvider(ArrayContentProvider.getInstance());
		table.getControl().setLayoutData(GridDataFactory.fillDefaults().grab(true, true).create());
		table.getTable().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDoubleClick(final MouseEvent e) {
				okPressed();
			}
		});

		final TableViewerColumn c1 = new TableViewerColumn(table, SWT.LEFT, 0);
		c1.getColumn().setWidth(getColumnWidth());
		final String columnName = getColumnName();
		if (columnName != null) {
			c1.getColumn().setText(columnName);
			table.getTable().setHeaderVisible(true);
		} else {
			table.getTable().setHeaderVisible(false);
		}
		table.setLabelProvider(new LabelProvider() {
			@SuppressWarnings("unchecked")
			@Override
			public String getText(final Object element) {
				return getEntityFieldToDisplay((E) element);
			}
		});

		table.setInput(this.displayedList);

		return composite;
	}

	/**
	 * If this method returns <b>null</b>, then no header will be displayed for the column.
	 *
	 * @return the column header.
	 */
	protected String getColumnName() {
		return null;
	}

	protected abstract int getColumnWidth();

	protected abstract String getEntityFieldToDisplay(E entity);

	public List<E> getSelection() {
		return selection;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void okPressed() {
		final TableItem[] selected = table.getTable().getSelection();
		for (final TableItem tableItem : selected) {
			selection.add((E) tableItem.getData());
		}
		super.okPressed();
	}
}
