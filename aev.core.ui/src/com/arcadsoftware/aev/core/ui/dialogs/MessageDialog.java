/*
 * Créé le 9 juin 2004
 * Projet : ARCAD Plugin Core UI
 * <i> Copyright 2004, Arcad-Software.</i>
 *  
 */
package com.arcadsoftware.aev.core.ui.dialogs;

import java.util.ArrayList;

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
 * 
 * @author mlafon
 * @version 1.0.0
 */
public class MessageDialog extends ArcadDialog implements IMessagesListener {

	/**
	 * Débute un block d'enregistrement de messges pour la dialog.
	 * 
	 * @param plugin
	 * @param blockContext
	 * @return Message
	 */
	// public static Message begin(Plugin plugin, String blockContext) {
	public static Message begin(String blockContext) {
		return MessageManager.beginMessageBlock(blockContext);
	}

	/**
	 * Termine un block d'enregistrement de messages pour la dialog.
	 * 
	 * @param shell
	 * @param showParam
	 */
	@SuppressWarnings("unchecked")
	public static void end(Shell shell, int showParam) {
		int showParameter = showParam;
		if (showParameter == 0)
			showParameter = MessageManager.SHOW_ALL;

		ArrayList list = MessageManager.endMessageBlock(showParameter);
		if (list != null) {
			MessageDialog dialog = new MessageDialog(shell, list, showParameter);
			MessageManager.addListener(dialog);
			dialog.open();
			MessageManager.removeListener(dialog);
		}
	}

	public static Message add(String command, int type, int level, String description) {
		return MessageManager.addMessage(command, type, level, description);
	}

	@SuppressWarnings("unchecked")
	private ArrayList messages;
	private MessagesTreeViewer messagesTree;
	private int levelFilter;

	/**
	 * @param parentShell
	 */
	@SuppressWarnings("unchecked")
	public MessageDialog(Shell parentShell, ArrayList messages, int levelFilter) {
		super(parentShell);
		this.messages = messages;
		this.levelFilter = levelFilter;
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
		createButton(parent, IDialogConstants.OK_ID, DialogConstantProvider.getInstance().OK_LABEL, true);
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

		messagesTree = new MessagesTreeViewer(comp, SWT.NONE | SWT.FULL_SELECTION, levelFilter);
		GridData gridData = new GridData(GridData.FILL_BOTH);
		gridData.widthHint = 420;
		gridData.heightHint = 320;
		messagesTree.getTree().setLayoutData(gridData);
		messagesTree.getTree().setLinesVisible(false);
		messagesTree.setInput(messages);
		((TreeViewer) messagesTree.getViewer()).expandAll();

		GuiFormatTools.setHelp(comp, IDocProvider.HLP_MESSAGESLOG);
		return comp;
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
		// Spécification du titre de la fenêtre en fonction du détail des
		// messages affichés.
		String text = CoreUILabels.resString("MessageDialog.Messages"); //$NON-NLS-1$
		switch (EvolutionCoreUIPlugin.getDefault().getMessagesLevel()) {
		case MessageManager.LEVEL_DEVELOPMENT: {
			text += CoreUILabels.resString("MessageDialog.Level.Development"); //$NON-NLS-1$
			break;
		}
		case MessageManager.LEVEL_BETATESTING: {
			text += CoreUILabels.resString("MessageDialog.Level.BetaTest"); //$NON-NLS-1$
			break;
		}
		case MessageManager.LEVEL_PRODUCTION: {
			text += CoreUILabels.resString("MessageDialog.Level.Production"); //$NON-NLS-1$
			break;
		}
		}
		newShell.setText(text);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.arcadsoftware.aev.core.messages.IMessagesListener#newMessageAdded
	 * (com.arcadsoftware.aev.core.messages.Message)
	 */
	@SuppressWarnings("unchecked")
	public void newMessageAdded(Message message) {
		if (message.isVisibleTo(levelFilter)) {
			messages.add(message);
			if (messagesTree != null) {
				messagesTree.refresh();
				((TreeViewer) messagesTree.getViewer()).expandAll();
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.arcadsoftware.aev.core.messages.IMessagesListener#messageDeleted()
	 */
	public void messageDeleted(Message message) {
		if (message != null) {
			int i = messages.indexOf(message);
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.arcadsoftware.aev.core.messages.IMessagesListener#messageChanged(
	 * com.arcadsoftware.aev.core.messages.Message)
	 */
	@SuppressWarnings("unchecked")
	public void messageChanged(Message message) {
		if (message.isVisibleTo(levelFilter)) {
			int i = messages.indexOf(message);
			if (i != -1) {
				messages.add(message);
			}
			if (messagesTree != null) {
				messagesTree.refresh();
				((TreeViewer) messagesTree.getViewer()).expandAll();
			}
		}
	}

}
