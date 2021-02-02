/*
 * Créé le 10 juin 2004
 * Projet : ARCAD Plugin Core UI
 * <i> Copyright 2004, Arcad-Software.</i>
 *
 */
package com.arcadsoftware.aev.core.ui.wizards;

import java.util.ArrayList;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;

import com.arcadsoftware.aev.core.messages.IMessagesListener;
import com.arcadsoftware.aev.core.messages.Message;
import com.arcadsoftware.aev.core.messages.MessageManager;
import com.arcadsoftware.aev.core.ui.tools.GuiFormatTools;
import com.arcadsoftware.aev.core.ui.treeviewers.MessagesTreeViewer;

/**
 * @author mlafon
 * @version 1.0.0
 */
public class MessagesWizardPage extends ArcadWizardPage implements IMessagesListener {

	private final ArrayList<Message> messages = new ArrayList<>();
	private int showParam = 0;
	private MessagesTreeViewer tree;

	/**
	 * @param pageName
	 */
	public MessagesWizardPage(final String pageName, final int showParam) {
		super(pageName);
		this.showParam = showParam;
	}

	/**
	 * @param pageName
	 * @param title
	 */
	public MessagesWizardPage(final String pageName, final String title, final int showParam) {
		super(pageName, title);
		this.showParam = showParam;
	}

	/**
	 * @param pageName
	 * @param title
	 * @param titleImage
	 */
	public MessagesWizardPage(final String pageName, final String title, final int showParam,
			final ImageDescriptor titleImage) {
		super(pageName, title, titleImage);
		this.showParam = showParam;
	}

	/**
	 * Débute le block de messages.
	 *
	 * @param plugin
	 *            le plugin responsable du block de message (peut être null).
	 * @param blockContext
	 *            message englobant les messages jusqu'à terminaison du block (peut être null).
	 * @return Message
	 */
	public Message begin(final String blockContext) {
		MessageManager.addListener(this);
		return MessageManager.beginMessageBlock(blockContext);
	}

	@Override
	public void createControl(final Composite parent) {
		final Composite compo = GuiFormatTools.createComposite(parent, 1, false);
		tree = new MessagesTreeViewer(compo, SWT.NONE | SWT.FULL_SELECTION, showParam);
		tree.getTree().setLayoutData(new GridData(GridData.FILL_BOTH));
		tree.setInput(messages);
		((TreeViewer) tree.getViewer()).expandAll();
		setControl(compo);
		setPageComplete(true);
	}

	/**
	 * Termine le block de messages.
	 */
	public void end() {
		MessageManager.endMessageBlock(showParam);
		MessageManager.removeListener(this);
	}

	/*
	 * (non-Javadoc)
	 * @see com.arcadsoftware.aev.core.ui.wizards.ArcadWizardPage#getPageHelpContextId ()
	 */
	@Override
	protected String getPageHelpContextId() {
		return null;
	}

	@Override
	public void messageChanged(final Message message) {
		if (tree != null && !tree.getTree().isDisposed()) {
			tree.refresh();
		}
	}

	@Override
	public void messageDeleted(final Message message) {
		final int i = messages.indexOf(message);
		if (i != -1) {
			messages.remove(i);
			if (tree != null && !tree.getTree().isDisposed()) {
				tree.refresh();
			}
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
	public void newMessageAdded(final Message message, final Throwable e) {
		if (message.isVisibleTo(showParam)) {
			messages.add(message);
			if (tree != null && !tree.getTree().isDisposed()) {
				tree.refresh();
			}
		}
	}
}