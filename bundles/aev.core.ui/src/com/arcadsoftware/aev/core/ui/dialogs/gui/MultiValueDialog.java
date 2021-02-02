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

	private class AddSelectionAdapter extends SelectionAdapter {
		private final List addList;
		private final Text text;

		public AddSelectionAdapter(final List list, final Text text) {
			super();
			addList = list;
			this.text = text;
		}

		@Override
		public void widgetSelected(final SelectionEvent e) {
			if (!text.getText().equals(StringTools.EMPTY)) {
				addList.add(text.getText());
				text.setText(StringTools.EMPTY);
			}
		}
	}

	private class DeleteSelectionAdapter extends SelectionAdapter {
		private final List deleteList;

		public DeleteSelectionAdapter(final List list) {
			super();
			deleteList = list;
		}

		@Override
		public void widgetSelected(final SelectionEvent e) {
			if (deleteList.getSelectionIndex() != -1) {
				deleteList.remove(deleteList.getSelectionIndex());
			}
		}
	}

	private class ListSelectionAdapter extends SelectionAdapter {
		private final List selectionList;
		private final Text text;

		public ListSelectionAdapter(final List list, final Text text) {
			super();
			selectionList = list;
			this.text = text;
		}

		@Override
		public void widgetSelected(final SelectionEvent e) {
			text.setText(selectionList.getItem(selectionList.getSelectionIndex()));
		}
	}

	protected static int DIALOG_HEIGHT = 200;
	protected static int DIALOG_WIDTH = 350;

	List list = null;

	String value = null;

	String[] values = null;

	/**
	 * @param parentShell
	 * @param width
	 * @param height
	 * @param title
	 */
	public MultiValueDialog(final Shell parentShell) {
		this(parentShell, CoreUILabels.resString("MultiValueDialog.title")); //$NON-NLS-1$
	}

	public MultiValueDialog(final Shell parentShell, final String description) {
		super(parentShell, DIALOG_WIDTH, DIALOG_HEIGHT, description);
	}

	@Override
	protected Control createDialogArea(final Composite parent) {
		Text text = null;
		final Composite composite = (Composite) super.createDialogArea(parent);
		GridData gridData;
		final GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 1;
		composite.setLayout(gridLayout);

		// -- Option de mise à disposition
		list = new List(composite, SWT.BORDER);
		gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.verticalAlignment = GridData.FILL;
		gridData.grabExcessVerticalSpace = true;
		list.setLayoutData(gridData);

		if (values != null) {
			list.setItems(values);
		}

		// Création du composite de réception
		final Composite p = new Composite(composite, SWT.NONE);
		final GridLayout layout = new GridLayout(3, false);
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
		final Button bAdd = new Button(p, SWT.PUSH);
		gridData = new GridData();
		gridData.widthHint = 20;
		gridData.heightHint = 20;
		bAdd.setLayoutData(gridData);
		bAdd.setText("+"); //$NON-NLS-1$

		bAdd.addSelectionListener(new AddSelectionAdapter(list, text));

		final Button bDelete = new Button(p, SWT.PUSH);
		gridData = new GridData();
		gridData.widthHint = 20;
		gridData.heightHint = 20;
		bDelete.setText("-"); //$NON-NLS-1$
		bDelete.setLayoutData(gridData);

		bDelete.addSelectionListener(new DeleteSelectionAdapter(list));

		list.addSelectionListener(new ListSelectionAdapter(list, text));

		return composite;
	}

	public String getValue() {
		return value;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.jface.dialogs.Dialog#okPressed()
	 */
	@Override
	protected void okPressed() {
		final StringBuilder sb = new StringBuilder();
		for (int i = 0; i < list.getItemCount(); i++) {
			if (i > 0) {
				sb.append(';');
			}
			sb.append(list.getItem(i));
		}
		value = sb.toString();
		super.okPressed();
	}

	public void setValue(final String s) {
		final StringTokenizer st = new StringTokenizer(s, ";"); //$NON-NLS-1$
		values = new String[st.countTokens()];
		int i = 0;
		while (st.hasMoreTokens()) {
			values[i] = st.nextToken();
			i++;
		}
	}

}
