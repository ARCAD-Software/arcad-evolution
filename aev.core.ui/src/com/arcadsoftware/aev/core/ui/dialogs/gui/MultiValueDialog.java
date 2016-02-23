/*
 * Created on 9 févr. 2006
 */
package com.arcadsoftware.aev.core.ui.dialogs.gui;

import java.util.StringTokenizer;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.arcadsoftware.aev.core.tools.StringTools;
import com.arcadsoftware.aev.core.ui.dialogs.ArcadCenteredDialog;
import com.arcadsoftware.aev.core.ui.tools.CoreUILabels;

/**
 * @author MD
 */
public class MultiValueDialog extends ArcadCenteredDialog {

	protected static int DIALOG_WIDTH = 350;
	protected static int DIALOG_HEIGHT = 200;

	List list = null;
	String value = null;
	String[] values = null;

	/**
	 * @param parentShell
	 * @param width
	 * @param height
	 * @param title
	 */
	public MultiValueDialog(Shell parentShell) {
		this(parentShell, CoreUILabels.resString("MultiValueDialog.title")); //$NON-NLS-1$
	}

	public MultiValueDialog(Shell parentShell, String description) {
		super(parentShell, DIALOG_WIDTH, DIALOG_HEIGHT, description);
	}

	private class AddSelectionAdapter extends SelectionAdapter {
		private List addList;
		private Text text;

		public AddSelectionAdapter(List list, Text text) {
			super();
			this.addList = list;
			this.text = text;
		}

		@Override
		public void widgetSelected(SelectionEvent e) {
			if (!text.getText().equals(StringTools.EMPTY)) {
				addList.add(text.getText());
				text.setText(StringTools.EMPTY);
			}
		}
	}

	private class ListSelectionAdapter extends SelectionAdapter {
		private List selectionList;
		private Text text;

		public ListSelectionAdapter(List list, Text text) {
			super();
			this.selectionList = list;
			this.text = text;
		}

		@Override
		public void widgetSelected(SelectionEvent e) {
			text.setText(selectionList.getItem(selectionList.getSelectionIndex()));
		}
	}

	private class DeleteSelectionAdapter extends SelectionAdapter {
		private List deleteList;

		public DeleteSelectionAdapter(List list) {
			super();
			this.deleteList = list;
		}

		@Override
		public void widgetSelected(SelectionEvent e) {
			if (deleteList.getSelectionIndex() != -1) {
				deleteList.remove(deleteList.getSelectionIndex());
			}
		}
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Text text = null;
		Composite composite = (Composite) super.createDialogArea(parent);
		GridData gridData;
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 1;
		composite.setLayout(gridLayout);

		// -- Option de mise à disposition
		list = new List(composite, SWT.BORDER);
		gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.verticalAlignment = GridData.FILL;
		gridData.grabExcessVerticalSpace = true;
		list.setLayoutData(gridData);

		if (values != null)
			list.setItems(values);

		// Création du composite de réception
		Composite p = new Composite(composite, SWT.NONE);
		GridLayout layout = new GridLayout(3, false);
		layout.marginWidth = 0;
		layout.marginHeight = 0;
		layout.horizontalSpacing = 0;
		p.setLayout(layout);
		gridData = new GridData(GridData.BEGINNING);
		gridData.horizontalAlignment = GridData.FILL;
		gridData.grabExcessHorizontalSpace = true;
		p.setLayoutData(gridData);

		text = new Text(p, SWT.BORDER);
		gridData = new GridData(GridData.BEGINNING);
		gridData.horizontalAlignment = GridData.FILL;
		gridData.grabExcessHorizontalSpace = true;
		text.setLayoutData(gridData);
		Button bAdd = new Button(p, SWT.PUSH);
		gridData = new GridData();
		gridData.widthHint = 20;
		gridData.heightHint = 20;
		bAdd.setLayoutData(gridData);
		bAdd.setText("+"); //$NON-NLS-1$

		bAdd.addSelectionListener(new AddSelectionAdapter(list, text));

		Button bDelete = new Button(p, SWT.PUSH);
		gridData = new GridData();
		gridData.widthHint = 20;
		gridData.heightHint = 20;
		bDelete.setText("-"); //$NON-NLS-1$
		bDelete.setLayoutData(gridData);

		bDelete.addSelectionListener(new DeleteSelectionAdapter(list));

		list.addSelectionListener(new ListSelectionAdapter(list, text));

		return composite;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.dialogs.Dialog#okPressed()
	 */
	@Override
	protected void okPressed() {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < list.getItemCount(); i++) {
			if (i > 0)
				sb.append(";"); //$NON-NLS-1$
			sb.append(list.getItem(i));
		}
		value = sb.toString();
		super.okPressed();
	}

	public String getValue() {
		return value;
	}

	public void setValue(String s) {
		StringTokenizer st = new StringTokenizer(s, ";"); //$NON-NLS-1$
		values = new String[st.countTokens()];
		int i = 0;
		while (st.hasMoreTokens()) {
			values[i] = st.nextToken();
			i++;
		}
	}

}
