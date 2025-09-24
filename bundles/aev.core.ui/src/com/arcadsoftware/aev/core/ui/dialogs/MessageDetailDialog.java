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
package com.arcadsoftware.aev.core.ui.dialogs;

import java.util.Iterator;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.arcadsoftware.aev.core.messages.IMessagesListener;
import com.arcadsoftware.aev.core.messages.Message;
import com.arcadsoftware.aev.core.messages.MessageDetail;
import com.arcadsoftware.aev.core.messages.MessageManager;
import com.arcadsoftware.aev.core.tools.StringTools;
import com.arcadsoftware.aev.core.ui.IDocProvider;
import com.arcadsoftware.aev.core.ui.messages.MessageIconHelper;
import com.arcadsoftware.aev.core.ui.tools.GuiFormatTools;

/**
 * Affiche le d�tail d'un message Arcad.
 *
 * @author mlafon
 */
public class MessageDetailDialog extends ArcadDialog implements IMessagesListener, ISelectionChangedListener {

	private static class MessageContentProvider implements IStructuredContentProvider {

		/*
		 * (non-Javadoc)
		 * @see org.eclipse.jface.viewers.IContentProvider#dispose()
		 */
		@Override
		public void dispose() {
			// Do nothing
		}

		/*
		 * (non-Javadoc)
		 * @see org.eclipse.jface.viewers.IStructuredContentProvider#getElements( java.lang.Object)
		 */
		@Override
		public Object[] getElements(final Object inputElement) {
			if (inputElement instanceof Message) {
				return ((Message) inputElement).toArray();
			}
			return new Object[0];
		}

		/*
		 * (non-Javadoc)
		 * @see org.eclipse.jface.viewers.IContentProvider#inputChanged(org.eclipse .jface.viewers.Viewer,
		 * java.lang.Object, java.lang.Object)
		 */
		@Override
		public void inputChanged(final Viewer viewer, final Object oldInput, final Object newInput) {
			// Do nothing
		}
	}

	private class MessageLabelProvider extends LabelProvider {

		public MessageLabelProvider() {
			super();
		}

		@Override
		public Image getImage(final Object element) {
			if (element instanceof Message) {
				return MessageIconHelper.getMessageIcon((Message) element).image();
			} else if (element instanceof MessageDetail) {
				return MessageIconHelper.getMessageDetailIcon((MessageDetail) element).image();
			}
			return null;
		}

		/*
		 * (non-Javadoc)
		 * @see org.eclipse.jface.viewers.ILabelProvider#getText(java.lang.Object)
		 */
		@Override
		public String getText(final Object element) {
			if (element instanceof Message && ((Message) element).getCommand() != null) {
				return ((Message) element).getCommand();
			}
			if (element instanceof MessageDetail && ((MessageDetail) element).getDescription() != null) {
				return ((MessageDetail) element).getDescription();
			}
			return StringTools.EMPTY;
		}
	}

	/**
	 * Cr�� et ouvre la dialog en une ligne.
	 *
	 * @param parentShell
	 *            Le shell parent (la dialog est modale).
	 * @param message
	 *            Le message � afficher
	 */
	public static void showMessageDetails(final Shell parentShell, final Message message) {
		new MessageDetailDialog(parentShell).open(message);
	}

	private Text detail;

	private TableViewer detailsList;

	private Message message = null;

	private Label messageText;

	/**
	 * Contructeur de la classe MessageDetailDialog.
	 *
	 * @param parentShell
	 */
	public MessageDetailDialog(final Shell parentShell) {
		super(parentShell);
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.jface.window.Window#close()
	 */
	@Override
	public boolean close() {
		MessageManager.removeListener(this);
		return super.close();
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.jface.window.Window#configureShell(org.eclipse.swt.widgets .Shell)
	 */
	@Override
	protected void configureShell(final Shell newShell) {
		super.configureShell(newShell);
		if (message != null) {
			int i = message.getCommand().indexOf("/n"); //$NON-NLS-1$
			if (i <= 0) {
				i = message.getCommand().length();
			}
			if (i > 80) {
				i = 80;
			}
			if (i != message.getCommand().length()) {
				newShell.setText(message.getCommand().substring(i) + "..."); //$NON-NLS-1$
			} else {
				newShell.setText(message.getCommand());
			}

			newShell.setImage(MessageIconHelper.getMessageIcon(message).image());
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.jface.dialogs.Dialog#createButtonsForButtonBar(org.eclipse .swt.widgets.Composite)
	 */
	@Override
	protected void createButtonsForButtonBar(final Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, DialogConstantProvider.getInstance().OK_LABEL, true);
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.jface.dialogs.Dialog#createDialogArea(org.eclipse.swt.widgets .Composite)
	 */
	@Override
	protected Control createDialogArea(final Composite parent) {
		final Composite comp = (Composite) super.createDialogArea(parent);

		messageText = new Label(comp, SWT.LEFT | SWT.WRAP);
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.widthHint = 450;
		gridData.heightHint = 30;
		messageText.setLayoutData(gridData);

		detailsList = new TableViewer(comp);
		gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.widthHint = 450;
		gridData.heightHint = 110;
		gridData.grabExcessVerticalSpace = true;
		detailsList.getTable().setLayoutData(gridData);
		detailsList.setContentProvider(new MessageContentProvider());
		detailsList.setLabelProvider(new MessageLabelProvider());
		detailsList.addSelectionChangedListener(this);

		detail = new Text(comp, SWT.LEFT | SWT.READ_ONLY | SWT.MULTI | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.widthHint = 450;
		gridData.heightHint = 160;
		detail.setLayoutData(gridData);

		GuiFormatTools.setHelp(comp, IDocProvider.HLP_MESSAGESDETAIL);
		updateDialog();
		return comp;
	}

	@Override
	protected boolean isResizable() {
		return true;
	}

	/*
	 * (non-Javadoc)
	 * @see com.arcadsoftware.aev.core.messages.IMessagesListener#messageChanged(
	 * com.arcadsoftware.aev.core.messages.Message)
	 */
	@Override
	public void messageChanged(final Message changedMessage) {
		if (message != null && message.equals(changedMessage)) {
			updateDialog();
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.arcadsoftware.aev.core.messages.IMessagesListener#messageDeleted(
	 * com.arcadsoftware.aev.core.messages.Message)
	 */
	@Override
	public void messageDeleted(final Message deletedMessage) {
		if (message != null && message.equals(deletedMessage)) {
			message = null;
			updateDialog();
		}
	}

	@Override
	public void newMessageAdded(final Message newMessage) {
		newMessageAdded(newMessage, null);
	}

	/*
	 * (non-Javadoc)
	 * @see com.arcadsoftware.aev.core.messages.IMessagesListener#newMessageAdded
	 * (com.arcadsoftware.aev.core.messages.Message)
	 */
	@Override
	public void newMessageAdded(final Message newMessage, final Throwable e) {
		// Do nothing
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.jface.window.Window#open()
	 */
	public int open(final Message openMessage) {
		message = openMessage;
		MessageManager.addListener(this);
		return super.open();
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ISelectionChangedListener#selectionChanged(
	 * org.eclipse.jface.viewers.SelectionChangedEvent)
	 */
	@Override
	public void selectionChanged(final SelectionChangedEvent event) {
		final IStructuredSelection selection = (IStructuredSelection) detailsList.getSelection();

		if (selection.isEmpty()) {
			return;
		}

		final Iterator<?> iterator = selection.iterator();

		if (iterator.hasNext() && detail != null && !detail.isDisposed()) {
			detail.setText(((MessageDetail) iterator.next()).getDescription());
		} else if(detail != null) {
			detail.setText(StringTools.EMPTY);
		}
	}

	protected void updateDialog() {
		if (detailsList != null && !detailsList.getTable().isDisposed()) {
			detail.setText(StringTools.EMPTY);
			detailsList.setInput(message);
			if (message == null) {
				messageText.setText(StringTools.EMPTY);
			} else {
				messageText.setText(message.getCommand());
			}
		}
	}
}
