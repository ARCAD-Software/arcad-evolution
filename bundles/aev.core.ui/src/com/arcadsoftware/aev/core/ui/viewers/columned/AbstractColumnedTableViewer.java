package com.arcadsoftware.aev.core.ui.viewers.columned;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Item;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Widget;

import com.arcadsoftware.aev.core.messages.MessageDetail;
import com.arcadsoftware.aev.core.messages.MessageManager;
import com.arcadsoftware.aev.core.ui.EvolutionCoreUIPlugin;
import com.arcadsoftware.aev.core.ui.columned.model.ArcadColumn;
import com.arcadsoftware.aev.core.ui.columned.model.ColumnedSortCriteriaList;
import com.arcadsoftware.aev.core.ui.contentproviders.ArcadCollectionItemContentProvider;
import com.arcadsoftware.aev.core.ui.dialogs.columned.ColumnedSortDialog;
import com.arcadsoftware.aev.core.ui.labelproviders.columned.AbstractColumnedLabelProviderAdapter;
import com.arcadsoftware.aev.core.ui.labelproviders.columned.AbstractColumnedTableLabelProvider;
import com.arcadsoftware.aev.core.ui.mementos.ColumnedViewerSettings;
import com.arcadsoftware.aev.core.ui.tools.CoreUILabels;
import com.arcadsoftware.aev.core.ui.viewers.columned.impl.ColumnedInternalTableViewer;
import com.arcadsoftware.aev.core.ui.viewers.columned.impl.ColumnedTableViewer;
import com.arcadsoftware.aev.core.ui.viewers.sorters.ColumnedSorter;
import com.arcadsoftware.documentation.icons.Icon;

/**
 * @author MD
 */
public abstract class AbstractColumnedTableViewer extends AbstractColumnedViewer {

	public class RunMultiSorter implements IRunnableWithProgress {

		public RunMultiSorter() {
			super();
		}

		/**
		 * Méthode d'exécution du tri multicritère avec message de progression Les éléments sont alors triés et affichés
		 * dans le viewer spécifique
		 *
		 * @param monitor
		 */
		@Override
		public void run(final IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
			monitor.beginTask(CoreUILabels.resString("Action.SortAction.Execute"), 3); //$NON-NLS-1$
			monitor.worked(1);
			final Object temp = getInput();
			setInput(null);
			monitor.worked(1);
			refreshColumns();
			monitor.worked(1);
			setInput(temp);
			monitor.done();
		}
	}

	private class ShowSortEditorAction extends Action {
		public ShowSortEditorAction() {
			super();
			setText(CoreUILabels.resString("action.columned.sortEditor.text")); //$NON-NLS-1$
			setToolTipText(CoreUILabels.resString("action.columned.sortEditor.tooltip")); //$NON-NLS-1$
			setImageDescriptor(Icon.SORT.imageDescriptor());
		}

		@Override
		public void run() {
			ColumnedSorter currentSorter = getSorter();
			boolean newSorter = false;
			if (currentSorter == null) {
				final ColumnedSortCriteriaList list = new ColumnedSortCriteriaList(displayedColumns, true);
				currentSorter = new ColumnedSorter(list, AbstractColumnedTableViewer.this);
				newSorter = true;
			}
			final ColumnedSortDialog dialog = new ColumnedSortDialog(EvolutionCoreUIPlugin.getShell(), currentSorter
					.getCriteriaList().duplicate(), displayedColumns);
			dialog.create();
			if (dialog.open() == Window.OK) {
				currentSorter.setCriteriaList(dialog.getCriteriaList());
				if (newSorter) {
					setSorter(currentSorter);
				}
				getViewer().setSorter(sorter);

				final IRunnableWithProgress runContainer = new RunMultiSorter();
				try {
					new ProgressMonitorDialog(EvolutionCoreUIPlugin.getDefault().getPluginShell()).run(false, false,
							runContainer);

				} catch (final InvocationTargetException e) {
					MessageManager.addException(e, MessageManager.LEVEL_PRODUCTION).addDetail(MessageDetail.ERROR,
							this.getClass().toString());
				} catch (final InterruptedException e) {
					MessageManager.addException(e, MessageManager.LEVEL_PRODUCTION).addDetail(MessageDetail.ERROR,
							this.getClass().toString());
					Thread.currentThread().interrupt();
				}
			}
		}
	}

	/**
	 * 
	 */
	public AbstractColumnedTableViewer(final Composite parent, final int style) {
		this(parent, style, true);
	}

	/**
	 * @param parent
	 * @param style
	 * @param withInit
	 */
	public AbstractColumnedTableViewer(final Composite parent, final int style, final boolean withInit) {
		super(parent, style, withInit);
	}

	public void addViewerContextMenu(final IMenuManager cmManager) {
		fillContextMenu(cmManager);
	}

	/*
	 * (non-Javadoc)
	 * @see com.arcadsoftware.aev.core.ui.viewers.columned.AbstractColumnedViewer
	 * #createColumn(org.eclipse.swt.widgets.Widget, int, int)
	 */
	@Override
	public Item createColumn(final Widget widget, final int columnStyle, final int index) {
		return new TableColumn((Table) widget, columnStyle, index);
	}

	/*
	 * (non-Javadoc)
	 * @see com.arcadsoftware.aev.core.ui.viewers.columned.AbstractColumnedViewer #createContentProvider()
	 */
	@Override
	public IContentProvider createContentProvider() {
		return new ArcadCollectionItemContentProvider();
	}

	/*
	 * (non-Javadoc)
	 * @see com.arcadsoftware.aev.core.ui.viewers.columned.AbstractColumnedViewer #createLabelProvider
	 * (com.arcadsoftware.aev.core.ui.viewers.columned.AbstractColumnedViewer)
	 */
	@Override
	public AbstractColumnedLabelProviderAdapter createLabelProvider(final AbstractColumnedViewer viewer) {
		return createTableLabelProvider(viewer);
	}

	public abstract AbstractColumnedTableLabelProvider createTableLabelProvider(AbstractColumnedViewer viewer);

	/*
	 * (non-Javadoc)
	 * @see com.arcadsoftware.aev.core.ui.viewers.columned.AbstractColumnedViewer #createViewer()
	 */
	@Override
	public AbstractInternalColumnedViewer createViewer(final Composite viewerParent, final int viewerStyle) {
		return new ColumnedInternalTableViewer(new ColumnedTableViewer(viewerParent, viewerStyle));
	}

	// <FM number="2013/00188" version="08.16.04" date="28 févr. 2013 user="md">
	@Override
	protected void extendSettings(final ColumnedViewerSettings settings) {
		if (sorter != null) {
			settings.setSortCriteriaList(sorter.getCriteriaList());
		}
	}
	// </FM>

	/*
	 * (non-Javadoc)
	 * @see com.arcadsoftware.aev.core.ui.viewers.columned.AbstractColumnedViewer #getControl()
	 */
	@Override
	public Widget getControl() {
		return getViewer().getControl();
	}

	public ColumnedSorter getSorter() {
		return sorter;
	}

	public Table getTable() {
		return (Table) getViewer().getControl();
	}

	/*
	 * (non-Javadoc)
	 * @see com.arcadsoftware.aev.core.ui.viewers.columned.AbstractColumnedViewer
	 * #init(org.eclipse.swt.widgets.Composite, int)
	 */
	@Override
	public void init() {
		super.init();
		setSortOnColumn(true);
	}

	@Override
	protected Action[] makeActions() {
		final Object[] previous = getPreviousActions().toArray();
		final Action[] makeActions = super.makeActions();
		final Object[] next = getNextActions().toArray();
		final Action[] result = new Action[previous.length + makeActions.length + next.length + 3];
		System.arraycopy(previous, 0, result, 0, previous.length);
		result[previous.length] = new ColumnedActionSeparator();
		System.arraycopy(makeActions, 0, result, previous.length + 1, makeActions.length);
		// Ajout de l'action d'affichage de l'éditeur de tris
		final int size = previous.length + makeActions.length + 1;
		result[size] = new ShowSortEditorAction();
		result[size + 1] = new ColumnedActionSeparator();
		System.arraycopy(next, 0, result, previous.length + makeActions.length + 3, next.length);
		return result;
	}

	@Override
	public void removeAllColumns() {
		final Table table = getTable();
		for (int i = table.getColumnCount() - 1; i >= 0; i--) {
			table.getColumn(i).dispose();
		}
	}

	@Override
	protected void removeSorterOnColumn() {
		final Table table = (Table) getControl();
		for (int i = 0; i < table.getColumnCount(); i++) {
			final TableColumn c = table.getColumn(i);
			c.removeSelectionListener(hdl);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.arcadsoftware.aev.core.ui.viewers.columned.AbstractColumnedViewer
	 * #setColumnValues(org.eclipse.swt.widgets.Item, com.arcadsoftware.aev.core.ui.tableviewers.ArcadColumn)
	 */
	@Override
	public void setColumnValues(final Item item, final ArcadColumn c) {
		((TableColumn) item).setWidth(c.getWidth());
	}

	public void setSorter(final ColumnedSorter sorter) {
		this.sorter = sorter;
	}

	@Override
	protected void setSorterOnColumn() {
		final Table table = (Table) getControl();
		for (int i = 0; i < table.getColumnCount(); i++) {
			final TableColumn c = table.getColumn(i);
			c.addSelectionListener(hdl);
		}
	}
}