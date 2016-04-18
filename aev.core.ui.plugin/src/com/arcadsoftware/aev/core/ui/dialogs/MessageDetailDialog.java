/*
 * Copyright 2005 ARCAD-Software.
 * Créé par mlafon
 * Créé le 23 juin 05
 */
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
import com.arcadsoftware.aev.core.ui.EvolutionCoreUIPlugin;
import com.arcadsoftware.aev.core.ui.IDocProvider;
import com.arcadsoftware.aev.core.ui.tools.CoreUILabels;
import com.arcadsoftware.aev.core.ui.tools.GuiFormatTools;

/**
 * Affiche le détail d'un message Arcad.
 * 
 * @author mlafon
 */
public class MessageDetailDialog extends ArcadDialog implements IMessagesListener, ISelectionChangedListener {

	private Message message = null;
	private Label messageText;
	private TableViewer detailsList;
	private Text detail;

	/**
	 * Créé et ouvre la dialog en une ligne.
	 * 
	 * @param parentShell
	 *            Le shell parent (la dialog est modale).
	 * @param message
	 *            Le message à afficher
	 */
	static public void showMessageDetails(Shell parentShell, Message message) {
		(new MessageDetailDialog(parentShell)).open(message);
	}

	static protected class MessageContentProvider implements IStructuredContentProvider {

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.eclipse.jface.viewers.IStructuredContentProvider#getElements(
		 * java.lang.Object)
		 */
		public Object[] getElements(Object inputElement) {
			if ((inputElement != null) && (inputElement instanceof Message)) {
				return ((Message) inputElement).toArray();
			}
			return null;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.jface.viewers.IContentProvider#dispose()
		 */
		public void dispose() {
			// Do nothing
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.eclipse.jface.viewers.IContentProvider#inputChanged(org.eclipse
		 * .jface.viewers.Viewer, java.lang.Object, java.lang.Object)
		 */
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
			// Do nothing
		}
	}

	/**
	 * Contructeur de la classe MessageDetailDialog.
	 * 
	 * @param parentShell
	 */
	public MessageDetailDialog(Shell parentShell) {
		super(parentShell);
	}

	protected void updateDialog() {
		if ((detailsList != null) && !detailsList.getTable().isDisposed()) {
			detail.setText(StringTools.EMPTY);
			detailsList.setInput(message);
			if (message == null) {
				messageText.setText(StringTools.EMPTY);
			} else {
				messageText.setText(message.getCommand());
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.dialogs.Dialog#createButtonsForButtonBar(org.eclipse
	 * .swt.widgets.Composite)
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID,DialogConstantProvider.getInstance().OK_LABEL, true);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.dialogs.Dialog#createDialogArea(org.eclipse.swt.widgets
	 * .Composite)
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite comp = (Composite) super.createDialogArea(parent);

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

	private class MessageLabelProvider extends LabelProvider {

		public MessageLabelProvider() {
			super();
		}

		@Override
		public Image getImage(Object element) {
			if (element instanceof Message) {
				switch (((Message) element).getMaxDetailsType()) {
				case MessageDetail.COMPLETION:
					return CoreUILabels.getImage(EvolutionCoreUIPlugin.ICO_MES_COMPL);
				case MessageDetail.DIAGNOSTIC:
					return CoreUILabels.getImage(EvolutionCoreUIPlugin.ICO_MES_DIAG);
				case MessageDetail.ERROR:
					return CoreUILabels.getImage(EvolutionCoreUIPlugin.ICO_MES_ERROR);
				case MessageDetail.WARNING:
					return CoreUILabels.getImage(EvolutionCoreUIPlugin.ICO_MES_WARN);
				default/* case MessageDetail.EXCEPTION */:
					return CoreUILabels.getImage(EvolutionCoreUIPlugin.ICO_MES_EXCEP);
				}
			} else if (element instanceof MessageDetail) {
				switch (((MessageDetail) element).getType()) {
				case MessageDetail.COMPLETION:
					return CoreUILabels.getImage(EvolutionCoreUIPlugin.ICO_MESDETAIL_COMPL);
				case MessageDetail.DIAGNOSTIC:
					return CoreUILabels.getImage(EvolutionCoreUIPlugin.ICO_MESDETAIL_DIAG);
				case MessageDetail.ERROR:
					return CoreUILabels.getImage(EvolutionCoreUIPlugin.ICO_MESDETAIL_ERROR);
				case MessageDetail.WARNING:
					return CoreUILabels.getImage(EvolutionCoreUIPlugin.ICO_MESDETAIL_WARN);
				default/* case MessageDetail.EXCEPTION */:
					return CoreUILabels.getImage(EvolutionCoreUIPlugin.ICO_MESDETAIL_EXCEP);
				}
			}
			return null;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.eclipse.jface.viewers.ILabelProvider#getText(java.lang.Object)
		 */
		@Override
		public String getText(Object element) {
			if ((element instanceof Message) && (((Message) element).getCommand() != null)) {
				return ((Message) element).getCommand();
			}
			if ((element instanceof MessageDetail) && (((MessageDetail) element).getDescription() != null)) {
				return ((MessageDetail) element).getDescription();
			}
			return StringTools.EMPTY;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.window.Window#configureShell(org.eclipse.swt.widgets
	 * .Shell)
	 */
	@Override
	protected void configureShell(Shell newShell) {
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
			switch (message.getMaxDetailsType()) {
			case MessageDetail.COMPLETION:
				newShell.setImage(CoreUILabels.getImage(EvolutionCoreUIPlugin.ICO_MES_COMPL));
				break;
			case MessageDetail.DIAGNOSTIC:
				newShell.setImage(CoreUILabels.getImage(EvolutionCoreUIPlugin.ICO_MES_DIAG));
				break;
			case MessageDetail.ERROR:
				newShell.setImage(CoreUILabels.getImage(EvolutionCoreUIPlugin.ICO_MES_ERROR));
				break;
			case MessageDetail.WARNING:
				newShell.setImage(CoreUILabels.getImage(EvolutionCoreUIPlugin.ICO_MES_WARN));
				break;
			default/* case MessageDetail.EXCEPTION */:
				newShell.setImage(CoreUILabels.getImage(EvolutionCoreUIPlugin.ICO_MES_EXCEP));
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.arcadsoftware.aev.core.messages.IMessagesListener#newMessageAdded
	 * (com.arcadsoftware.aev.core.messages.Message)
	 */
	public void newMessageAdded(Message newMessage) {
		// Do nothing
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.arcadsoftware.aev.core.messages.IMessagesListener#messageDeleted(
	 * com.arcadsoftware.aev.core.messages.Message)
	 */
	public void messageDeleted(Message deletedMessage) {
		if ((this.message != null) && (this.message.equals(deletedMessage))) {
			this.message = null;
			updateDialog();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.arcadsoftware.aev.core.messages.IMessagesListener#messageChanged(
	 * com.arcadsoftware.aev.core.messages.Message)
	 */
	public void messageChanged(Message changedMessage) {
		if ((this.message != null) && (this.message.equals(changedMessage))) {
			updateDialog();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.window.Window#open()
	 */
	public int open(Message openMessage) {
		this.message = openMessage;
		MessageManager.addListener(this);
		return super.open();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.window.Window#close()
	 */
	@Override
	public boolean close() {
		MessageManager.removeListener(this);
		return super.close();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.viewers.ISelectionChangedListener#selectionChanged(
	 * org.eclipse.jface.viewers.SelectionChangedEvent)
	 */
	@SuppressWarnings("unchecked")
	public void selectionChanged(SelectionChangedEvent event) {
		IStructuredSelection selection = (IStructuredSelection) detailsList.getSelection();

		if (selection.isEmpty())
			return;

		Iterator iterator = selection.iterator();

		if ((iterator.hasNext()) && (detail != null) && !detail.isDisposed()) {
			detail.setText(((MessageDetail) iterator.next()).getDescription());
		} else {
			detail.setText(StringTools.EMPTY);
		}
	}
}
