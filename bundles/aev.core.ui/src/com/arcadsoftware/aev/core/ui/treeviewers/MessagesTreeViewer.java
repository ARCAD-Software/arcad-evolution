package com.arcadsoftware.aev.core.ui.treeviewers;

import java.util.AbstractList;
import java.util.Iterator;

import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import com.arcadsoftware.aev.core.messages.Message;
import com.arcadsoftware.aev.core.messages.MessageDetail;
import com.arcadsoftware.aev.core.messages.MessageManager;
import com.arcadsoftware.aev.core.tools.StringTools;
import com.arcadsoftware.aev.core.ui.EvolutionCoreUIPlugin;
import com.arcadsoftware.aev.core.ui.columned.model.ArcadColumn;
import com.arcadsoftware.aev.core.ui.columned.model.ArcadColumns;
import com.arcadsoftware.aev.core.ui.dialogs.MessageDetailDialog;
import com.arcadsoftware.aev.core.ui.labelproviders.MessageLabelProvider;
import com.arcadsoftware.aev.core.ui.labelproviders.columned.AbstractColumnedTreeLabelProvider;
import com.arcadsoftware.aev.core.ui.tools.CoreUILabels;
import com.arcadsoftware.aev.core.ui.viewers.columned.AbstractColumnedTreeViewer;
import com.arcadsoftware.aev.core.ui.viewers.columned.AbstractColumnedViewer;

public class MessagesTreeViewer extends AbstractColumnedTreeViewer {

	public MessagesTreeViewer(Composite parent, int style, int levelFilter) {
		super(parent, style);
		init(levelFilter);
	}

	protected void init(int levelFilter) {
		getViewer().addFilter(new LevelFilter(levelFilter));
	}

	@Override
	public AbstractColumnedTreeLabelProvider createTreeLabelProvider(AbstractColumnedViewer viewer) {
		return new MessageLabelProvider(viewer);
	}

	@Override
	public IContentProvider createContentProvider() {
		return new MessageContentProvider();
	}

	@Override
	public ArcadColumns getReferenceColumns() {
		ArcadColumns cols = new ArcadColumns();
		cols.add(new ArcadColumn("messages", //$NON-NLS-1$
				CoreUILabels.resString("MessagesTreeViewer.header.messages"), //$NON-NLS-1$
				CoreUILabels.resString("MessagesTreeViewer.header.messages"), //$NON-NLS-1$
				ArcadColumn.VISIBLE, 0, 2000));
		return cols;
	}

	@Override
	public String getValue(Object element, int columnIndex) {
		if (element instanceof Message) {
			Message message = (Message) element;
			switch (columnIndex) {
			case 0:
				return message.getCommand();
			}

		} else if (element instanceof MessageDetail) {
			MessageDetail msgDetail = (MessageDetail) element;
			switch (columnIndex) {
			case 0:
				return msgDetail.getDescription();
			}
		}
		return StringTools.EMPTY;
	}

	static protected class MessageContentProvider implements IStructuredContentProvider, ITreeContentProvider {

		@Override
		public Object[] getElements(Object inputElement) {
			return ((AbstractList<?>) inputElement).toArray(new Message[((AbstractList<?>) inputElement).size()]);
		}

		@Override
		public void dispose() {
			// Do nothing
		}

		@Override
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
			// Do nothing
		}

		@Override
		public Object[] getChildren(Object parentElement) {
			if (parentElement instanceof Message) {
				return ((Message) parentElement).toArray();
			}
			return null;
		}

		@Override
		public Object getParent(Object element) {
			if (element instanceof MessageDetail)
				return ((MessageDetail) element).getMessage();
			return null;
		}

		@Override
		public boolean hasChildren(Object element) {
			return ((element instanceof Message) && (!((Message) element).getDetails().isEmpty()));
		}
	}

	static private class LevelFilter extends ViewerFilter {

		private int level = 0;

		public LevelFilter(int levelFilter) {
			super();
			level = levelFilter;
		}

		@Override
		public boolean select(Viewer viewer, Object parentElement, Object element) {
			if (element instanceof MessageDetail) {
				return (((MessageDetail) element).getType() & level) != 0;
			} else if (element instanceof Message) {
				if (((Message) element).getLevel() >= EvolutionCoreUIPlugin.getDefault().getMessagesLevel())
					for (int i = 0; i < ((Message) element).detailCount(); i++) {
						if ((((Message) element).getDetailAt(i).getType() & level) != 0)
							return true;
					}
				return false;
			}
			return true;
		}

		public int getLevel() {
			return level;
		}

		public void setLevel(int i) {
			level = i;
		}
	}

	/**
	 * Mise à jour du niveau de filtrage des messages.
	 * 
	 * @param level
	 */
	public void setFilterLevel(int level) {
		if (getTree().isDisposed())
			return;
		ViewerFilter[] filters = getViewer().getFilters();
		if (filters.length > 0) {
			LevelFilter levelFilter = (LevelFilter) filters[0];
			levelFilter.setLevel(level);
			refresh();
		} else if (level != -1)
			getViewer().addFilter(new LevelFilter(level));
	}

	/**
	 * Retourne le niveau de filtre courant.
	 * 
	 * @return int
	 */
	public int getFilterLevel() {
		if (getTree().isDisposed())
			return -1;
		ViewerFilter[] filters = getViewer().getFilters();
		if (filters.length > 0)
			return ((LevelFilter) filters[0]).getLevel();
		return -1;
	}

	/**
	 * Exporte la liste des messages vers un ficher XML.
	 * 
	 */
	@SuppressWarnings("unchecked")
	public void exportMessages() {
		if (getTree().isDisposed())
			return;
		String fileName = 
			EvolutionCoreUIPlugin.getDefault().getFileManagerProvider().selectFile(
				getTree().getShell(), SWT.SAVE, "", new String[] { "*.xml", "*.*" });//$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
//		FileDialog dialog = new FileDialog(getTree().getShell(), SWT.SAVE);
//		dialog.setFilterExtensions(new String[] { "*.xml", "*.*" }); //$NON-NLS-1$ //$NON-NLS-2$
//		String fileName = dialog.open();
		if (fileName != null)
			MessageManager.exportMessagesToXMLFile(fileName, (AbstractList<Message>) getInput());
	}

	/**
	 * Vide la liste des messages.
	 * 
	 */
	public void clear() {
		if (getInput() != null) {
			Iterator<?> iterator = ((AbstractList<?>) getInput()).iterator();
			while (iterator.hasNext()) {
				((Message) iterator.next()).setLevel(MessageManager.LEVEL_DELETED);
			}
			refresh();
		}
	}

	/**
	 * Supprime les messages sélectionnés de la liste.
	 */
	public void clearSelection() {
		if (getInput() == null)
			return;

		IStructuredSelection selection = getSelection();

		if (selection.isEmpty())
			return;

		Iterator<?> iterator = selection.iterator();

		while (iterator.hasNext()) {
			((Message) iterator.next()).setLevel(MessageManager.LEVEL_DELETED);
		}
		refresh();
	}

	public void showDetails() {
		if (getInput() == null)
			return;

		IStructuredSelection selection = (IStructuredSelection) getViewer().getSelection();

		if (selection.isEmpty())
			return;

		Iterator<?> iterator = selection.iterator();

		if (iterator.hasNext()) {
			MessageDetailDialog dialog = new MessageDetailDialog(getViewer().getControl().getShell());
			Object o = iterator.next();
			if (o instanceof Message)
				dialog.open((Message) o);
			else if (o instanceof MessageDetail)
				dialog.open(((MessageDetail) o).getMessage());
		}
	}
}
