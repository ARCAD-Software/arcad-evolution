package com.arcadsoftware.aev.core.ui.treeviewers;

import java.util.ArrayList;
import java.util.StringTokenizer;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerDropAdapter;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DragSourceListener;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.dnd.TransferData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.part.ViewPart;

import com.arcadsoftware.aev.core.tools.StringTools;
import com.arcadsoftware.aev.core.ui.EvolutionCoreUIPlugin;
import com.arcadsoftware.aev.core.ui.columned.model.ArcadColumn;
import com.arcadsoftware.aev.core.ui.columned.model.ArcadColumns;
import com.arcadsoftware.aev.core.ui.container.Container;
import com.arcadsoftware.aev.core.ui.container.ContainerProvider;
import com.arcadsoftware.aev.core.ui.container.IContainer;
import com.arcadsoftware.aev.core.ui.container.RootContainer;
import com.arcadsoftware.aev.core.ui.contentproviders.ContainerTreeContentProvider;
import com.arcadsoftware.aev.core.ui.labelproviders.ContainerLabelProvider;
import com.arcadsoftware.aev.core.ui.labelproviders.columned.AbstractColumnedTreeLabelProvider;
import com.arcadsoftware.aev.core.ui.listeners.IContainerSelectListener;
import com.arcadsoftware.aev.core.ui.viewers.columned.AbstractColumnedTreeViewer;
import com.arcadsoftware.aev.core.ui.viewers.columned.AbstractColumnedViewer;

public class ContainerTreeViewer extends AbstractColumnedTreeViewer {

	protected class ContainerFilter extends ViewerFilter {
		@Override
		public Object[] filter(final Viewer arg0, final Object arg1, final Object[] arg2) {
			return super.filter(arg0, arg1, arg2);
		}

		@Override
		public boolean select(final Viewer viewer, final Object parentElement, final Object element) {
			if (element instanceof RootContainer || element instanceof ContainerProvider) {
				return true;
			}
			if (element instanceof Container) {
				final Container c = (Container) element;
				if (include(c.getIdentifier())) {
					return true;
				}
			}
			return false;
		}
	}

	/*
	 * @author MD Pour changer le modèle de ce commentaire de type généré, allez à :
	 * Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
	 */
	private class ContainerTreeDragSourceListener implements DragSourceListener {
		/**
		 * @param viewer
		 */
		public ContainerTreeDragSourceListener(final StructuredViewer viewer) {
			// Do nothing
		}

		@Override
		public void dragFinished(final DragSourceEvent event) {
			// Do nothing
		}

		@Override
		public void dragSetData(final DragSourceEvent event) {
			final IContainer c = getSelectedElement();
			if (c != null) {
				event.data = c.getUniqueKey();
			} else {
				event.data = null;
			}
		}

		@Override
		public void dragStart(final DragSourceEvent event) {
			final IContainer c = getSelectedElement();
			dropSource = c;
			if (c != null) {
				event.doit = c.isDragable();
			} else {
				event.doit = false;
			}
		}
	}

	private class ContainerTreeDropTargetListener extends ViewerDropAdapter {
		protected ContainerTreeDropTargetListener(final Viewer viewer) {
			super(viewer);
		}

		@Override
		public boolean performDrop(final Object data) {
			if (getCurrentTarget() instanceof IContainer) {
				final IContainer c = getContainerFromKey(((IContainer) getCurrentTarget()).getUniqueKey());
				if (c != null) {
					if (dropSource != null) {
						return c.performDrop(dropSource);
						// Drag à partir d'une vue exterieur
					}
					// en fonction du type d'élément droppé, faire le traitement
					// adequat
					// else if (componentDropped){
					// ComponentWithKey cd = (ComponentWithKey)data;
					// if (getCurrentTarget() instanceof
					// IAcceptDroppedComponents){
					// if (deleguateComponentAction)
					// return doComponentAction(c,cd.getComponents());
					// else
					// return
					// ((IAcceptDroppedComponents)c).performDropComponents(cd.getComponents());
					// }
				}
			}
			return false;
		}

		@Override
		public boolean validateDrop(final Object target, final int operation, final TransferData transferType) {
			// ComponentDropped = false;
			if (target != null) {
				if (dropSource != null) {
					return ((IContainer) target).valideDrop(dropSource);
				}
				// ComponentDropped =
				// ComponentWithKeyTransfer.getInstance().isSupportedType(transferType);
				return ((IContainer) target).valideDrop(transferType);
			}
			return false;
			// return true;
		}
	}

	protected class ContainerTreeSelectionChangedListener implements ISelectionChangedListener {
		@Override
		public void selectionChanged(final SelectionChangedEvent event) {
			if (event.getSelection().isEmpty()) {
				return;
			}
			if (event.getSelection() instanceof IStructuredSelection) {
				final IStructuredSelection sel = (IStructuredSelection) event.getSelection();
				doOnSelect(sel.getFirstElement());
				EvolutionCoreUIPlugin.getChangedProvider().fireSelectionChanged(event);
			}
		}
	}

	// on détermine quel est le plus grand id pour cet arbre
	public static final int MAXCONTAINER_NUMBER = 0;

	private final ArrayList<Integer> containerList = new ArrayList<>();

	Action doubleClickAction = null;

	IContainer dropSource;

	private ViewPart view;

	public ContainerTreeViewer(final Composite parent, final int style, final boolean withInit,
			final int[] containers) {
		super(parent, style, withInit);
		init(containers);
	}

	public ContainerTreeViewer(final Composite parent, final int style, final int[] containers) {
		super(parent, style);
		init(containers);
	}

	@Override
	public IContentProvider createContentProvider() {
		return new ContainerTreeContentProvider();
	}

	@Override
	public AbstractColumnedTreeLabelProvider createTreeLabelProvider(final AbstractColumnedViewer viewer) {
		return new ContainerLabelProvider(viewer);
	}

	@Override
	protected void doOnDoubleClick(final IStructuredSelection selection) {
		// Do nothing
	}

	protected void doOnSelect(final Object selected) {
		if (view instanceof IContainerSelectListener) {
			((IContainerSelectListener) view).doOnSelectContainer(selected);
		}
	}

	private TreeItem expandFromAbsoluteKey(final String key) {
		final Tree tree = getTree();
		final TreeItem[] rootnodes = tree.getItems();
		for (final TreeItem rootnode : rootnodes) {
			final TreeItem node = getNodeFromKey(key, rootnode);
			if (node != null) {
				node.setExpanded(true);
				getTreeViewer().expandToLevel(node.getData(), 1);
				return node;
			}
		}
		return null;
	}

	public void expandFromKey(final String key) {
		TreeItem node = null;
		// Découpage de la clé en sous clé
		final StringTokenizer st = new StringTokenizer(key, "/", false); //$NON-NLS-1$
		String s = StringTools.EMPTY;
		while (st.hasMoreElements()) {
			s = s.concat("/").concat(st.nextToken()); //$NON-NLS-1$
			node = expandFromAbsoluteKey(s);
		}
		if (node != null) {
			getTree().setSelection(new TreeItem[] { node });
		}
	}

	IContainer getContainerFromKey(final String key) {
		final Tree tree = getTree();
		final TreeItem[] rootnodes = tree.getItems();
		for (final TreeItem rootnode : rootnodes) {
			final IContainer c = getContainerFromKey(key, rootnode);
			if (c != null) {
				return c;
			}
		}
		return null;
	}

	private IContainer getContainerFromKey(final String key, final TreeItem node) {
		IContainer container = null;
		final Object o = node.getData();
		if (o instanceof IContainer && ((IContainer) o).getUniqueKey().equals(key)) {
			return (IContainer) o;
		}
		final TreeItem[] nodes = node.getItems();
		for (final TreeItem node2 : nodes) {
			if (node2.getData() instanceof IContainer) {
				final IContainer c = (IContainer) node2.getData();
				if (c.getUniqueKey().equals(key)) {
					return c;
				}
				container = getContainerFromKey(key, node2);
				if (container != null) {
					return container;
				}
			}
		}
		return container;
	}

	// <FM number="2006/00221" version v 2.0.01">
	public TreeItem getNodeFromAbsoluteKey(final String key) {
		final Tree tree = getTree();
		final TreeItem[] rootnodes = tree.getItems();
		for (final TreeItem rootnode : rootnodes) {
			final TreeItem node = getNodeFromKey(key, rootnode);
			if (node != null) {
				return node;
			}
		}
		return null;
	}

	private TreeItem getNodeFromKey(final String key, final TreeItem node) {
		TreeItem resultNode = null;
		final Object o = node.getData();
		if (o instanceof IContainer && ((IContainer) o).getUniqueKey().equals(key)) {
			return node;
		}
		final TreeItem[] nodes = node.getItems();
		for (final TreeItem node2 : nodes) {
			if (node2.getData() instanceof IContainer) {
				final IContainer c = (IContainer) node2.getData();
				if (c.getUniqueKey().equals(key)) {
					return node2;
				}
				resultNode = getNodeFromKey(key, node2);
				if (resultNode != null) {
					return resultNode;
				}
			}
		}
		return resultNode;
	}

	@Override
	public ArcadColumns getReferenceColumns() {
		final ArcadColumns cols = new ArcadColumns();
		cols.add(new ArcadColumn("container", StringTools.EMPTY, StringTools.EMPTY, ArcadColumn.VISIBLE, 0, 800)); //$NON-NLS-1$
		return cols;
	}

	public IContainer getSelectedElement() {
		final IStructuredSelection selection = getSelection();
		final Object o = selection.getFirstElement();
		if (o instanceof IContainer) {
			return (IContainer) selection.getFirstElement();
		}
		return null;
	}

	@Override
	public String getValue(final Object element, final int columnIndex) {
		if (element instanceof IContainer) {
			return ((IContainer) element).getLabel();
		}
		return StringTools.EMPTY;
	}

	public ViewPart getView() {
		return view;
	}

	private void hookDoubleClickAction() {
		doubleClickAction = new Action() {
			@Override
			public void run() {
				if (!getSelection().isEmpty()) {
					final IStructuredSelection selection = getSelection();
					doOnDoubleClick(selection);
				}
			}
		};
		getViewer().addDoubleClickListener(event -> doubleClickAction.run());
	}

	boolean include(final int value) {
		for (final Integer element : containerList) {
			if (element.intValue() == value) {
				return true;
			}
		}
		return false;
	}

	protected void init(final int[] containers) {
		for (final int container : containers) {
			containerList.add(new Integer(container));
		}
		getViewer().addFilter(new ContainerFilter());
		getViewer().addSelectionChangedListener(new ContainerTreeSelectionChangedListener());
		initializeDragAndDropManagement();
		hookDoubleClickAction();
	}

	protected void initializeDragAndDropManagement() {
		if (getViewer() != null) {
			final int ops = DND.DROP_COPY | DND.DROP_MOVE;
			final Transfer[] transfers = new Transfer[] { TextTransfer.getInstance() };
			getViewer().addDragSupport(ops, transfers, new ContainerTreeDragSourceListener(getViewer()));
			final int ops2 = DND.DROP_COPY | DND.DROP_MOVE;
			final Transfer[] transfers2 = new Transfer[] { TextTransfer.getInstance() };
			getViewer().addDropSupport(ops2, transfers2, new ContainerTreeDropTargetListener(getViewer()));
		}
	}

	@Override
	protected Action[] makeActions() {
		return new Action[0];
	}

	public void refresh(final Object element) {
		if (element != null && element instanceof Container) {
			((Container) element).refresh();
		}
		getViewer().refresh(element);
	}

	// <FM>

	@Override
	protected void setOptions() {
		getTree().setHeaderVisible(false);
		getTree().setLinesVisible(false);
	}

	public void setView(final ViewPart part) {
		view = part;
	}
}
