/*
 * Created on 2 mars 2006
 *
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.arcadsoftware.aev.core.ui.dialogs.gui;

import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import com.arcadsoftware.aev.core.collections.IArcadDisplayable;
import com.arcadsoftware.aev.core.ui.dialogs.ArcadCenteredDialog;

/**
 * @author MD
 */
public class SimpleListSelectionDialog extends ArcadCenteredDialog {

	@SuppressWarnings("unchecked")
	private ArrayList list;

	protected static int DIALOG_WIDTH = 350;
	protected static int DIALOG_HEIGHT = 400;

	private String columnTitle;
	private String description;

	Table table;
	private Object selectedObject = null;
	private int selectedIndex = -1;

	@SuppressWarnings("unchecked")
	public SimpleListSelectionDialog(Shell parentShell, String title, String description, String columnTitle,
			ArrayList list) {
		super(parentShell, DIALOG_WIDTH, DIALOG_HEIGHT, title);
		this.list = list;
		this.columnTitle = columnTitle;
		this.description = description;
		setShellStyle(getShellStyle() | SWT.RESIZE);
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite composite = (Composite) super.createDialogArea(parent);
		GridData gridData;
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 1;
		composite.setLayout(gridLayout);

		Label label = new Label(composite, SWT.WRAP);
		label.setText(description);
		gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.heightHint = 40;
		label.setLayoutData(gridData);

		table = new Table(composite, SWT.BORDER);
		TableColumn c1 = new TableColumn(table, SWT.LEFT, 0);
		c1.setText(columnTitle);
		c1.setWidth(300);
		gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.verticalAlignment = GridData.FILL;
		gridData.grabExcessVerticalSpace = true;
		table.setLayoutData(gridData);
		table.setHeaderVisible(true);
		for (int i = 0; i < list.size(); i++) {
			TableItem ti = new TableItem(table, SWT.NONE);
			Object o = list.get(i);
			if (o instanceof IArcadDisplayable)
				ti.setText(((IArcadDisplayable) o).getLabel());
			else if (o instanceof String)
				ti.setText((String) o);
			else
				ti.setText(o.toString());
			ti.setData(o);

		}
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDoubleClick(MouseEvent e) {
				TableItem ti = table.getItem(new Point(e.x, e.y));
				if (ti != null) {
					doOnDoubleclick(ti);
				}
			}
		});
		return composite;
	}

	public void doOnDoubleclick(TableItem ti) {
		table.setSelection(new TableItem[] { ti });
		okPressed();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.dialogs.Dialog#okPressed()
	 */
	@Override
	protected void okPressed() {
		int index = table.getSelectionIndex();
		if (index != -1)
			selectedObject = table.getItem(index).getData();
		else
			selectedObject = null;
		selectedIndex = index;
		super.okPressed();
	}

	/**
	 * @return Returns the selectedObject.
	 */
	public Object getSelectedObject() {
		return selectedObject;
	}

	/**
	 * @return Returns the selectedIndex.
	 */
	public int getSelectedIndex() {
		return selectedIndex;
	}

	/**
	 * 
	 * @param parentShell
	 * @param title
	 * @param question
	 * @param answers
	 * @param defaultAnswer
	 * @param freeInput
	 * @return Object
	 */
	@SuppressWarnings("unchecked")
	public static Object selectObject(Shell parentShell, String title, String description, String columnTitle,
			ArrayList list) {

		SimpleListSelectionDialog dialog = new SimpleListSelectionDialog(parentShell, title, description, columnTitle,
				list);
		dialog.open();
		return dialog.getSelectedObject();
	}

	/**
	 * 
	 * @param parentShell
	 * @param title
	 * @param question
	 * @param answers
	 * @param defaultAnswer
	 * @param freeInput
	 * @return int
	 */
	@SuppressWarnings("unchecked")
	public static int selectIndex(Shell parentShell, String title, String description, String columnTitle,
			ArrayList list) {

		SimpleListSelectionDialog dialog = new SimpleListSelectionDialog(parentShell, title, description, columnTitle,
				list);
		dialog.open();
		return dialog.getSelectedIndex();
	}

}
