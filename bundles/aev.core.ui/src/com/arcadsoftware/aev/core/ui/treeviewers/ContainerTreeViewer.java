package com.arcadsoftware.aev.core.ui.treeviewers;

import java.util.ArrayList;
import java.util.StringTokenizer;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.IDoubleClickListener;
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

	// on détermine quel est le plus grand id pour cet arbre
	public static final int MAXCONTAINER_NUMBER = 0;

	@SuppressWarnings("unchecked")
	private ArrayList containerList = new ArrayList();
	IContainer dropSource;
	Action doubleClickAction = null;
	private ViewPart view;

	public ContainerTreeViewer(Composite parent, int style, boolean withInit, int[] containers) {
		super(parent, style, withInit);
		init(containers);
	}

	public ContainerTreeViewer(Composite parent, int style, int[] containers) {
		super(parent, style);
		init(containers);
	}

	@SuppressWarnings("unchecked")
	protected void init(int[] containers) {
		for (int i = 0; i < containers.length; i++) {
			containerList.add(new Integer(containers[i]));
		}
		getViewer().addFilter(new ContainerFilter());
		getViewer().addSelectionChangedListener(new ContainerTreeSelectionChangedListener());
		initializeDragAndDropManagement();
		hookDoubleClickAction();
	}

	@Override
	protected void setOptions() {
		getTree().setHeaderVisible(false);
		getTree().setLinesVisible(false);
	}

	@Override
	protected void doOnDoubleClick(IStructuredSelection selection) {
		// Do nothing
	}

	@Override
	public AbstractColumnedTreeLabelProvider createTreeLabelProvider(AbstractColumnedViewer viewer) {
		return new ContainerLabelProvider(viewer);
	}

	@Override
	public IContentProvider createContentProvider() {
		return new ContainerTreeContentProvider();
	}

	@Override
	public ArcadColumns getReferenceColumns() {
		ArcadColumns cols = new ArcadColumns();
		cols.add(new ArcadColumn("container", StringTools.EMPTY, StringTools.EMPTY, ArcadColumn.VISIBLE, 0, 800)); //$NON-NLS-1$
		return cols;
	}

	@Override
	public String getValue(Object element, int columnIndex) {
		if (element instanceof IContainer)
			return ((IContainer) element).getLabel();
		return StringTools.EMPTY;
	}

	@Override
	protected Action[] makeActions() {
		return new Action[0];
	}

	private IContainer getContainerFromKey(String key, TreeItem node) {
		IContainer container = null;
		Object o = node.getData();
		if ((o instanceof IContainer) && (((IContainer) o).getUniqueKey().equals(key)))
			return (IContainer) o;
		TreeItem[] nodes = node.getItems();
		for (int i = 0; i < nodes.length; i++) {
			if (nodes[i].getData() instanceof IContainer) {
				IContainer c = (IContainer) nodes[i].getData();
				if (c.getUniqueKey().equals(key))
					return c;
				container = getContainerFromKey(key, nodes[i]);
				if (container != null)
					return container;
			}
		}
		return container;
	}

	IContainer getContainerFromKey(String key) {
		Tree tree = this.getTree();
		TreeItem[] rootnodes = tree.getItems();
		for (int i = 0; i < rootnodes.length; i++) {
			IContainer c = getContainerFromKey(key, rootnodes[i]);
			if (c != null) {
				return c;
			}
		}
		return null;
	}

	boolean include(int value) {
		for (int i = 0; i < containerList.size(); i++) {
			if (((Integer) containerList.get(i)).intValue() == value)
				return true;
		}
		return false;
	}

	/*
	 * 
	 * @author MD
	 * 
	 * Pour changer le modèle de ce commentaire de type généré, allez à :
	 * Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et
	 * commentaires
	 */
	private class ContainerTreeDragSourceListener implements DragSourceListener {
		/**
		 * @param viewer
		 */
		public ContainerTreeDragSourceListener(StructuredViewer viewer) {
			// Do nothing
		}

		public void dragStart(DragSourceEvent event) {
			IContainer c = getSelectedElement();
			dropSource = c;
			if (c != null)
				event.doit = c.isDragable();
			else
				event.doit = false;
		}

		public void dragSetData(DragSourceEvent event) {
			IContainer c = getSelectedElement();
			if (c != null)
				event.data = c.getUniqueKey();
			else
				event.data = null;
		}

		public void dragFinished(DragSourceEvent event) {
			// Do nothing
		}
	}

	private class ContainerTreeDropTargetListener extends ViewerDropAdapter {
		protected ContainerTreeDropTargetListener(Viewer viewer) {
			super(viewer);
		}

		@Override
		public boolean performDrop(Object data) {
			if (getCurrentTarget() instanceof IContainer) {
				IContainer c = getContainerFromKey(((IContainer) getCurrentTarget()).getUniqueKey());
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
		public boolean validateDrop(Object target, int operation, TransferData transferType) {
			// ComponentDropped = false;
			if (target != null) {
				if (dropSource != null)
					return ((IContainer) target).valideDrop(dropSource);
				// ComponentDropped =
				// ComponentWithKeyTransfer.getInstance().isSupportedType(transferType);
				return ((IContainer) target).valideDrop(transferType);
			}
			return false;
			// return true;
		}
	}

	protected class ContainerFilter extends ViewerFilter {
		@Override
		public boolean select(Viewer viewer, Object parentElement, Object element) {
			if (element instanceof RootContainer || element instanceof ContainerProvider)
				return true;
			if (element instanceof Container) {
				Container c = (Container) element;
				if (include(c.getIdentifier()))
					return true;
			}
			return false;
		}

		@Override
		public Object[] filter(Viewer arg0, Object arg1, Object[] arg2) {
			return super.filter(arg0, arg1, arg2);
		}
	}

	protected void doOnSelect(Object selected) {
		if (view instanceof IContainerSelectListener)
			((IContainerSelectListener) view).doOnSelectContainer(selected);
	}

	protected class ContainerTreeSelectionChangedListener implements ISelectionChangedListener {
		public void selectionChanged(SelectionChangedEvent event) {
			if (event.getSelection().isEmpty())
				return;
			if (event.getSelection() instanceof IStructuredSelection) {
				IStructuredSelection sel = (IStructuredSelection) event.getSelection();
				doOnSelect(sel.getFirstElement());
				EvolutionCoreUIPlugin.getChangedProvider().fireSelectionChanged(event);
			}
		}
	}

	protected void initializeDragAndDropManagement() {
		if (getViewer() != null) {
			int ops = DND.DROP_COPY | DND.DROP_MOVE;
			Transfer[] transfers = new Transfer[] { TextTransfer.getInstance() };
			getViewer().addDragSupport(ops, transfers, new ContainerTreeDragSourceListener(getViewer()));
			int ops2 = DND.DROP_COPY | DND.DROP_MOVE;
			Transfer[] transfers2 = new Transfer[] { TextTransfer.getInstance() };
			getViewer().addDropSupport(ops2, transfers2, new ContainerTreeDropTargetListener(getViewer()));
		}
	}

	public IContainer getSelectedElement() {
		IStructuredSelection selection = this.getSelection();
		Object o = selection.getFirstElement();
		if (o instanceof IContainer)
			return (IContainer) selection.getFirstElement();
		return null;
	}

	private void hookDoubleClickAction() {
		doubleClickAction = new Action() {
			@Override
			public void run() {
				if (!getSelection().isEmpty()) {
					IStructuredSelection selection = getSelection();
					doOnDoubleClick(selection);
				}
			}
		};
		getViewer().addDoubleClickListener(new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent event) {
				doubleClickAction.run();
			}
		});
	}

	public void refresh(Object element) {
		if (element != null && element instanceof Container)
			((Container) element).refresh();
		getViewer().refresh(element);
	}

	public void setView(ViewPart part) {
		view = part;
	}

	public ViewPart getView() {
		return view;
	}

	public void expandFromKey(String key) {
		TreeItem node = null;
		// Découpage de la clé en sous clé
		StringTokenizer st = new StringTokenizer(key, "/", false); //$NON-NLS-1$
		String s = StringTools.EMPTY;
		while (st.hasMoreElements()) {
			s = s.concat("/").concat(st.nextToken()); //$NON-NLS-1$
			node = expandFromAbsoluteKey(s);
		}
		if (node != null)
			this.getTree().setSelection(new TreeItem[] { node });
	}

	// <FM number="2006/00221" version v 2.0.01">
	public TreeItem getNodeFromAbsoluteKey(String key) {
		Tree tree = this.getTree();
		TreeItem[] rootnodes = tree.getItems();
		for (int i = 0; i < rootnodes.length; i++) {
			TreeItem node = getNodeFromKey(key, rootnodes[i]);
			if (node != null) {
				return node;
			}
		}
		return null;
	}

	// <FM>

	private TreeItem expandFromAbsoluteKey(String key) {
		Tree tree = this.getTree();
		TreeItem[] rootnodes = tree.getItems();
		for (int i = 0; i < rootnodes.length; i++) {
			TreeItem node = getNodeFromKey(key, rootnodes[i]);
			if (node != null) {
				node.setExpanded(true);
				getTreeViewer().expandToLevel(node.getData(), 1);
				return node;
			}
		}
		return null;
	}

	private TreeItem getNodeFromKey(String key, TreeItem node) {
		TreeItem resultNode = null;
		Object o = node.getData();
		if ((o instanceof IContainer) && (((IContainer) o).getUniqueKey().equals(key)))
			return node;
		TreeItem[] nodes = node.getItems();
		for (int i = 0; i < nodes.length; i++) {
			if (nodes[i].getData() instanceof IContainer) {
				IContainer c = (IContainer) nodes[i].getData();
				if (c.getUniqueKey().equals(key))
					return nodes[i];
				resultNode = getNodeFromKey(key, nodes[i]);
				if (resultNode != null)
					return resultNode;
			}
		}
		return resultNode;
	}
}
