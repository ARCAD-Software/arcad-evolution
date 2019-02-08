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
 * 
 * @author mlafon
 * @version 1.0.0
 */
public class MessagesWizardPage extends ArcadWizardPage implements IMessagesListener {

	private int showParam = 0;
	private ArrayList<Message> messages = new ArrayList<Message>();
	private MessagesTreeViewer tree;

	/**
	 * @param pageName
	 */
	public MessagesWizardPage(String pageName, int showParam) {
		super(pageName);
		this.showParam = showParam;
	}

	/**
	 * @param pageName
	 * @param title
	 * @param titleImage
	 */
	public MessagesWizardPage(String pageName, String title, int showParam, ImageDescriptor titleImage) {
		super(pageName, title, titleImage);
		this.showParam = showParam;
	}

	/**
	 * @param pageName
	 * @param title
	 */
	public MessagesWizardPage(String pageName, String title, int showParam) {
		super(pageName, title);
		this.showParam = showParam;
	}

	@Override
	public void createControl(Composite parent) {
		Composite compo = GuiFormatTools.createComposite(parent, 1, false);
		tree = new MessagesTreeViewer(compo, SWT.NONE | SWT.FULL_SELECTION, showParam);
		tree.getTree().setLayoutData(new GridData(GridData.FILL_BOTH));
		tree.setInput(messages);
		((TreeViewer) tree.getViewer()).expandAll();
		setControl(compo);
		setPageComplete(true);
	}

	/**
	 * Débute le block de messages.
	 * 
	 * @param plugin
	 *            le plugin responsable du block de message (peut être null).
	 * @param blockContext
	 *            message englobant les messages jusqu'à terminaison du block
	 *            (peut être null).
	 * @return Message
	 */
	public Message begin(String blockContext) {
		MessageManager.addListener(this);
		return MessageManager.beginMessageBlock(blockContext);
	}

	/**
	 * Termine le block de messages.
	 * 
	 */
	public void end() {
		MessageManager.endMessageBlock(showParam);
		MessageManager.removeListener(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.arcadsoftware.aev.core.messages.IMessagesListener#newMessageAdded
	 * (com.arcadsoftware.aev.core.messages.Message)
	 */
	@Override
	public void newMessageAdded(Message message, Throwable e) {
		if (message.isVisibleTo(showParam)) {
			messages.add(message);
			if ((tree != null) && !tree.getTree().isDisposed()) {
				tree.refresh();
			}
		}
	}
	@Override
	public void newMessageAdded(Message newMessage) {
		newMessageAdded(newMessage, null);
	}

	@Override
	public void messageDeleted(Message message) {
		int i = messages.indexOf(message);
		if (i != -1) {
			messages.remove(i);
			if ((tree != null) && !tree.getTree().isDisposed()) {
				tree.refresh();
			}
		}
	}

	@Override
	public void messageChanged(Message message) {
		if ((tree != null) && !tree.getTree().isDisposed()) {
			tree.refresh();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.arcadsoftware.aev.core.ui.wizards.ArcadWizardPage#getPageHelpContextId
	 * ()
	 */
	@Override
	protected String getPageHelpContextId() {
		return null;
	}
}