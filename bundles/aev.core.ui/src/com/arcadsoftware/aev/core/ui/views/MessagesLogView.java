/*
 * Créé le 10 juin 2004
 * Projet : ARCAD Plugin Core UI
 * <i> Copyright 2004, Arcad-Software.</i>
 *
 */
package com.arcadsoftware.aev.core.ui.views;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

import com.arcadsoftware.aev.core.messages.IMessagesListener;
import com.arcadsoftware.aev.core.messages.Message;
import com.arcadsoftware.aev.core.messages.MessageDetail;
import com.arcadsoftware.aev.core.messages.MessageManager;
import com.arcadsoftware.aev.core.ui.EvolutionCoreUIPlugin;
import com.arcadsoftware.aev.core.ui.IDocProvider;
import com.arcadsoftware.aev.core.ui.tools.CoreUILabels;
import com.arcadsoftware.aev.core.ui.tools.GuiFormatTools;
import com.arcadsoftware.aev.core.ui.treeviewers.MessagesTreeViewer;
import com.arcadsoftware.documentation.icons.Icon;

/**
 * @author mlafon
 * @version 1.0.0
 */
// TODO [DL] A revoir pour le rendre plus complet
public class MessagesLogView extends ViewPart implements IMessagesListener {

	private static class FilterAction extends Action {

		private final int level;
		private final MessagesTreeViewer tree;

		public FilterAction(final String text, final MessagesTreeViewer tree, final int level) {
			super(text, IAction.AS_CHECK_BOX);
			this.level = level;
			this.tree = tree;
			setChecked((level & tree.getFilterLevel()) != 0);
		}

		/*
		 * (non-Javadoc)
		 * @see org.eclipse.jface.action.IAction#run()
		 */
		@Override
		public void run() {
			if (!isChecked()) {
				tree.setFilterLevel(tree.getFilterLevel() & ~level);
			} else {
				tree.setFilterLevel(tree.getFilterLevel() | level);
			}
		}

	}

	private Action clearAllAction;
	private Action clearSelectionAction;
	private Action exportAction;

	public MessagesTreeViewer tree;

	public MessagesLogView() {
		super();
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.IWorkbenchPart#createPartControl(org.eclipse.swt.widgets .Composite)
	 */
	@Override
	public void createPartControl(final Composite parent) {
		tree = new MessagesTreeViewer(parent, SWT.NONE | SWT.FULL_SELECTION, MessageDetail.COMPLETION
				| MessageDetail.ERROR | MessageDetail.DIAGNOSTIC | MessageDetail.EXCEPTION | MessageDetail.WARNING);
		tree.getTree().setHeaderVisible(false);
		tree.getTree().setLinesVisible(false);
		tree.setInput(MessageManager.getMessagesList());
		GuiFormatTools.setHelp(tree.getViewer().getControl(), IDocProvider.HLP_MESSAGESLOG);
		tree.getViewer().addDoubleClickListener(event -> tree.showDetails());

		clearSelectionAction = new Action() {
			@Override
			public void run() {
				tree.clearSelection();
			}
		};
		clearSelectionAction.setText(CoreUILabels.resString("MessagesLogView.ClearSelection")); //$NON-NLS-1$
		clearSelectionAction.setToolTipText(CoreUILabels.resString("MessagesLogView.ClearSelectionHint")); //$NON-NLS-1$
		clearSelectionAction.setImageDescriptor(Icon.REMOVE.imageDescriptor());
		clearAllAction = new Action() {
			@Override
			public void run() {
				tree.clear();
			}
		};
		clearAllAction.setText(CoreUILabels.resString("MessagesLogView.Clear")); //$NON-NLS-1$
		clearAllAction.setToolTipText(CoreUILabels.resString("MessagesLogView.ClearHint")); //$NON-NLS-1$
		clearAllAction.setImageDescriptor(Icon.CLEANUP.imageDescriptor());
		exportAction = new Action() {
			@Override
			public void run() {
				tree.exportMessages();
			}
		};
		exportAction.setText(CoreUILabels.resString("MessagesLogView.Export")); //$NON-NLS-1$
		exportAction.setToolTipText(CoreUILabels.resString("MessagesLogView.ExportHint")); //$NON-NLS-1$
		exportAction.setImageDescriptor(Icon.SAVE.imageDescriptor());

		final IActionBars bars = getViewSite().getActionBars();

		final IToolBarManager toolBar = bars.getToolBarManager();
		toolBar.removeAll();
		toolBar.add(clearSelectionAction);
		toolBar.add(clearAllAction);
		toolBar.add(exportAction);
		toolBar.add(new Separator());

		final IMenuManager menu = bars.getMenuManager();
		menu.removeAll();
		menu.add(new FilterAction(
				CoreUILabels.resString("MessagesLogView.Completion"), tree, MessageManager.SHOW_COMPLETION)); //$NON-NLS-1$
		menu.add(new FilterAction(
				CoreUILabels.resString("MessagesLogView.Diagnostic"), tree, MessageManager.SHOW_DIAGNOSTIC)); //$NON-NLS-1$
		menu
				.add(new FilterAction(
						CoreUILabels.resString("MessagesLogView.Warning"), tree, MessageManager.SHOW_WARNING)); //$NON-NLS-1$
		menu.add(new FilterAction(CoreUILabels.resString("MessagesLogView.Error"), tree, MessageManager.SHOW_ERROR)); //$NON-NLS-1$
		menu.add(new FilterAction(
				CoreUILabels.resString("MessagesLogView.Exception"), tree, MessageManager.SHOW_EXCEPTION)); //$NON-NLS-1$
		// Other plug-ins can contribute there actions here
		menu.add(new Separator("Additions")); //$NON-NLS-1$

		toolBar.update(false);
		menu.update(false);
		bars.updateActionBars();

		PlatformUI.getWorkbench().getHelpSystem().setHelp(parent, IDocProvider.MESSAGE_LOG_VIEW);
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.IWorkbenchPart#dispose()
	 */
	@Override
	public void dispose() {
		EvolutionCoreUIPlugin.getDefault().setMessageLevelFilter(tree.getFilterLevel());
		MessageManager.removeListener(this);
		super.dispose();
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.IViewPart#init(org.eclipse.ui.IViewSite)
	 */
	@Override
	public void init(final IViewSite site) throws PartInitException {
		super.init(site);
		MessageManager.addListener(this);
	}

	@Override
	public void messageChanged(final Message message) {
		newMessageAdded(message);
	}

	@Override
	public void messageDeleted(final Message message) {
		newMessageAdded(message);
	}

	@Override
	public void newMessageAdded(final Message newMessage) {
		newMessageAdded(newMessage, null);
	}

	@Override
	public void newMessageAdded(final Message message, final Throwable e) {
		if (tree != null) {

			// Evite un appel à la méthode refresh depuis un Thread qui n'y
			// aurais pas accès.
			try {
				tree.getViewer().getControl().isVisible();
			} catch (final SWTException e1) {
				return;
			}

			tree.refresh();
			// tree.collapseAll();
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.IWorkbenchPart#setFocus()
	 */
	@Override
	public void setFocus() {
		if (tree != null) {
			tree.getTree().setFocus();
		}
	}

}
