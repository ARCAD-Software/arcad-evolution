/*
 * Créé le 9 juin 2004
 * Projet : ARCAD Plugin Core UI
 * <i> Copyright 2004, Arcad-Software.</i>
 *
 */
package com.arcadsoftware.aev.core.ui.dialogs;

import java.util.List;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

import com.arcadsoftware.aev.core.messages.IMessagesListener;
import com.arcadsoftware.aev.core.messages.Message;
import com.arcadsoftware.aev.core.messages.MessageManager;
import com.arcadsoftware.aev.core.ui.EvolutionCoreUIPlugin;
import com.arcadsoftware.aev.core.ui.IDocProvider;
import com.arcadsoftware.aev.core.ui.tools.CoreUILabels;
import com.arcadsoftware.aev.core.ui.tools.GuiFormatTools;
import com.arcadsoftware.aev.core.ui.treeviewers.MessagesTreeViewer;

/**
 * @author mlafon
 * @version 1.0.0
 */
public class MessageDialog extends ArcadDialog implements IMessagesListener {

	public static Message add(final String command, final int type, final int level, final String description) {
		return MessageManager.addMessage(command, type, level, description);
	}

	/**
	 * Débute un block d'enregistrement de messges pour la dialog.
	 *
	 * @param blockContext
	 * @return Message
	 */
	public static Message begin(final String blockContext) {
		return MessageManager.beginMessageBlock(blockContext);
	}

	/**
	 * Termine un block d'enregistrement de messages pour la dialog.
	 *
	 * @param shell
	 * @param showParam
	 */
	public static void end(final Shell shell, final int showParam) {
		int showParameter = showParam;
		if (showParameter == 0) {
			showParameter = MessageManager.SHOW_ALL;
		}

		final List<Message> list = MessageManager.endMessageBlock(showParameter);
		if (list != null) {
			final MessageDialog dialog = new MessageDialog(shell, list, showParameter);
			MessageManager.addListener(dialog);
			dialog.open();
			MessageManager.removeListener(dialog);
		}
	}

	private final int levelFilter;
	private final List<Message> messages;
	private MessagesTreeViewer messagesTree;

	public MessageDialog(final Shell parentShell, final List<Message> messages, final int levelFilter) {
		super(parentShell);
		this.messages = messages;
		this.levelFilter = levelFilter;
	}

	@Override
	protected void configureShell(final Shell newShell) {
		super.configureShell(newShell);
		// Spécification du titre de la fenêtre en fonction du détail des
		// messages affichés.
		String text = CoreUILabels.resString("MessageDialog.Messages"); //$NON-NLS-1$
		switch (EvolutionCoreUIPlugin.getDefault().getMessagesLevel()) {
		case MessageManager.LEVEL_DEVELOPMENT:
			text += CoreUILabels.resString("MessageDialog.Level.Development"); //$NON-NLS-1$
			break;

		case MessageManager.LEVEL_PRODUCTION:
			text += CoreUILabels.resString("MessageDialog.Level.Production"); //$NON-NLS-1$
			break;

		default:
			text += CoreUILabels.resString("MessageDialog.Level.BetaTest"); //$NON-NLS-1$
		}
		newShell.setText(text);
	}

	@Override
	protected void createButtonsForButtonBar(final Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, DialogConstantProvider.getInstance().OK_LABEL, true);
	}

	@Override
	protected Control createDialogArea(final Composite parent) {
		final Composite comp = (Composite) super.createDialogArea(parent);

		messagesTree = new MessagesTreeViewer(comp, SWT.NONE | SWT.FULL_SELECTION, levelFilter);
		final GridData gridData = new GridData(GridData.FILL_BOTH);
		gridData.widthHint = 420;
		gridData.heightHint = 320;
		messagesTree.getTree().setLayoutData(gridData);
		messagesTree.getTree().setLinesVisible(false);
		messagesTree.setInput(messages);
		((TreeViewer) messagesTree.getViewer()).expandAll();

		GuiFormatTools.setHelp(comp, IDocProvider.HLP_MESSAGESLOG);
		return comp;
	}

	@Override
	protected boolean isResizable() {
		return true;
	}

	@Override
	public void messageChanged(final Message message) {
		if (message.isVisibleTo(levelFilter)) {
			final int i = messages.indexOf(message);
			if (i != -1) {
				messages.add(message);
			}
			if (messagesTree != null) {
				messagesTree.refresh();
				((TreeViewer) messagesTree.getViewer()).expandAll();
			}
		}
	}

	@Override
	public void messageDeleted(final Message message) {
		if (message != null) {
			final int i = messages.indexOf(message);
			if (i != -1) {
				messages.remove(i);
				if (messagesTree != null) {
					messagesTree.refresh();
					((TreeViewer) messagesTree.getViewer()).expandAll();
				}
			}
		} else {
			messages.clear();
			if (messagesTree != null) {
				messagesTree.refresh();
				((TreeViewer) messagesTree.getViewer()).expandAll();
			}
		}
	}

	@Override
	public void newMessageAdded(final Message newMessage) {
		newMessageAdded(newMessage, null);
	}

	@Override
	public void newMessageAdded(final Message message, final Throwable e) {
		if (message.isVisibleTo(levelFilter)) {
			messages.add(message);
			if (messagesTree != null) {
				messagesTree.refresh();
				((TreeViewer) messagesTree.getViewer()).expandAll();
			}
		}
	}

}
