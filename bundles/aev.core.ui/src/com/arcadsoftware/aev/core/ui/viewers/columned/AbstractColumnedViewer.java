/*******************************************************************************
 * Copyright (c) 2025 ARCAD Software.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     ARCAD Software - initial API and implementation
 *******************************************************************************/
package com.arcadsoftware.aev.core.ui.viewers.columned;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.IBaseLabelProvider;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Item;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.swt.widgets.Widget;

import com.arcadsoftware.aev.core.messages.MessageDetail;
import com.arcadsoftware.aev.core.messages.MessageManager;
import com.arcadsoftware.aev.core.tools.StringTools;
import com.arcadsoftware.aev.core.ui.EvolutionCoreUIPlugin;
import com.arcadsoftware.aev.core.ui.actions.IActionMenuManager;
import com.arcadsoftware.aev.core.ui.actions.columned.ColumnedExportAction;
import com.arcadsoftware.aev.core.ui.columned.model.ArcadColumn;
import com.arcadsoftware.aev.core.ui.columned.model.ArcadColumns;
import com.arcadsoftware.aev.core.ui.columned.model.ColumnedSearchCriteriaList;
import com.arcadsoftware.aev.core.ui.columned.model.ColumnedSortCriteriaList;
import com.arcadsoftware.aev.core.ui.dialogs.columned.ColumnedDisplayDialog;
import com.arcadsoftware.aev.core.ui.dialogs.columned.ColumnedFilterDialog;
import com.arcadsoftware.aev.core.ui.dialogs.columned.ColumnedSearchDialog;
import com.arcadsoftware.aev.core.ui.labelproviders.columned.AbstractColumnedLabelProviderAdapter;
import com.arcadsoftware.aev.core.ui.listeners.columned.IDialogListener;
import com.arcadsoftware.aev.core.ui.mementos.ColumnedViewerMementoTools;
import com.arcadsoftware.aev.core.ui.mementos.ColumnedViewerSettings;
import com.arcadsoftware.aev.core.ui.tools.CoreUILabels;
import com.arcadsoftware.aev.core.ui.viewerfilters.columned.ColumnedViewerFilter;
import com.arcadsoftware.aev.core.ui.viewers.columned.impl.ColumnedSearcher;
import com.arcadsoftware.aev.core.ui.viewers.columned.impl.IColumnResolver;
import com.arcadsoftware.aev.core.ui.viewers.columned.impl.IColumnedSearcher;
import com.arcadsoftware.aev.core.ui.viewers.sorters.ArcadTableViewerColumnSorter;
import com.arcadsoftware.aev.core.ui.viewers.sorters.ColumnedSorter;
import com.arcadsoftware.aev.icons.Icon;

/**
 * @author MD
 */
public abstract class AbstractColumnedViewer implements IColumnResolver, IDialogListener {

	protected class HeaderListener extends SelectionAdapter {
		@Override
		public void widgetSelected(final SelectionEvent e) {
			if (AbstractColumnedViewer.this instanceof AbstractColumnedTableViewer) {
				final TableViewer v = (TableViewer) getViewer();
				final Table table = (Table) v.getControl();
				int column = table.indexOf((TableColumn) e.widget);
				column = getActualIndexFromDisplayedIndex(column);
				ArcadTableViewerColumnSorter oldSorter = null;
				if (v.getSorter() instanceof ArcadTableViewerColumnSorter) {
					oldSorter = (ArcadTableViewerColumnSorter) v.getSorter();
				}

				if (oldSorter != null) {
					if (column == oldSorter.getCurrentColumnIndex()) {
						oldSorter.setReversed(!oldSorter.isReversed());
					} else {
						oldSorter.setReversed(false);
						oldSorter.setCurrentColumnIndex(column);
					}
					v.refresh();
				} else {
					v.setSorter(new ArcadTableViewerColumnSorter(column));
				}
			}
		}
	}

	public class RunFilter implements IRunnableWithProgress {

		public RunFilter() {
			super();
		}

		/**
		 * M�thode d'ex�cution du tri multicrit�re avec message de progression Les �l�ments sont alors tri�s et affich�s
		 * dans le viewer sp�cifique
		 *
		 * @param monitor
		 */
		@Override
		public void run(final IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
			monitor.beginTask(CoreUILabels.resString("Action.FilterAction.Execute"), 4); //$NON-NLS-1$
			monitor.worked(1);
			final Object temp = getInput();
			setInput(null);
			monitor.worked(1);
			getViewer().addFilter(filter);
			saveState();
			monitor.worked(1);
			refreshColumns();
			monitor.worked(1);
			setInput(temp);
			monitor.done();
		}
	}

	/**
	 * // * Classe d'action permettant la gestion de l'export CSV
	 */
	private class ShowExportAction extends Action {
		public ShowExportAction() {
			super();
			setText(CoreUILabels.resString("action.columned.export.text")); //$NON-NLS-1$
			setToolTipText(CoreUILabels.resString("action.columned.export.tooltip")); //$NON-NLS-1$
			setImageDescriptor(Icon.EXPORT.imageDescriptor());
		}

		@Override
		public void run() {
			final ColumnedExportAction action = new ColumnedExportAction(AbstractColumnedViewer.this);
			action.run();
		}
	}

	/**
	 * Classe d'action permettant la gestion du filtre
	 */
	private class ShowFilterEditorAction extends Action {
		public ShowFilterEditorAction() {
			super();
			setText(CoreUILabels.resString("action.columned.filterEditor.text")); //$NON-NLS-1$
			setToolTipText(CoreUILabels.resString("action.columned.filterEditor.tooltip")); //$NON-NLS-1$
			setImageDescriptor(Icon.FILTER.imageDescriptor());
		}

		@Override
		public void run() {
			ColumnedViewerFilter showFilter = getFilter();
			boolean newFilter = false;
			if (showFilter == null) {
				final ColumnedSearchCriteriaList list = new ColumnedSearchCriteriaList(displayedColumns);
				showFilter = new ColumnedViewerFilter(list, AbstractColumnedViewer.this);
				newFilter = true;
			}
			final ColumnedFilterDialog dialog = new ColumnedFilterDialog(EvolutionCoreUIPlugin.getShell(), showFilter
					.getCriteriaList().duplicate(), displayedColumns);
			dialog.create();
			if (dialog.open() == Window.OK) {
				dialog.saveWidgetValues(dialog.getSettings());
				showFilter.setCasse(dialog.isCasse());
				showFilter.setCriteriaList(dialog.getCriteriaList());
				if (newFilter) {
					setFilter(showFilter);
				}
				final IRunnableWithProgress runContainer = new RunFilter();
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

	/*---------------------------------------------------------------------
	 *                            Gestion des actions
	 ---------------------------------------------------------------------*/
	/**
	 * Classe d'action permettant la gestion de l'affichage
	 */
	private class ShowPreferencesAction extends Action {
		public ShowPreferencesAction() {
			super();
			setText(CoreUILabels.resString("action.columned.preferences.text")); //$NON-NLS-1$
			setToolTipText(CoreUILabels.resString("action.columned.preferences.tooltip")); //$NON-NLS-1$
			setImageDescriptor(Icon.PREFERENCES.imageDescriptor());
		}

		@Override
		public void run() {
			displayDuplicate = displayedColumns.duplicate();
			// Affichage du dialog de pr�f�rences
			final ColumnedDisplayDialog dialog = new ColumnedDisplayDialog(EvolutionCoreUIPlugin.getShell(),
					displayDuplicate);
			dialog.create();
			dialog.addListener(AbstractColumnedViewer.this);
			if (dialog.open() == Window.OK) {
				displayedColumns = dialog.getReferenceColumns();
				if (displayedColumns == null) {
					displayedColumns = referenceColumns.duplicate();
				}
				refreshColumns();
			}
			dialog.removeListener(AbstractColumnedViewer.this);
		}
	}

	/**
	 * Classe d'action permettant la gestion de la recherche
	 */
	private class ShowSearchEditorAction extends Action {
		public ShowSearchEditorAction() {
			super();
			setText(CoreUILabels.resString("action.columned.searchEditor.text")); //$NON-NLS-1$
			setToolTipText(CoreUILabels.resString("action.columned.searchEditor.tooltip")); //$NON-NLS-1$
			setImageDescriptor(Icon.SEARCH.imageDescriptor());
		}

		@Override
		public void run() {
			ColumnedSearcher currentSearcher = (ColumnedSearcher) getSearcher();
			boolean newSearcher = false;
			if (currentSearcher == null) {
				final ColumnedSearchCriteriaList list = new ColumnedSearchCriteriaList(displayedColumns, true);
				currentSearcher = new ColumnedSearcher(list);
				newSearcher = true;
			}
			final ColumnedSearchDialog dialog = new ColumnedSearchDialog(EvolutionCoreUIPlugin.getShell(),
					currentSearcher
							.getCriteriaList().duplicate(),
					displayedColumns);
			dialog.create();
			if (dialog.open() == Window.OK) {
				dialog.saveWidgetValues(dialog.getSettings());
				currentSearcher.setCasse(dialog.isCasse());
				currentSearcher.setCriteriaList(dialog.getCriteriaList());
				if (newSearcher) {
					setSearcher(currentSearcher);
				}
				saveState();
				refresh();
			}
		}
	}

	private class ViewerSelectionChangerListener implements ISelectionChangedListener {
		/**
		 * @param viewer
		 */
		public ViewerSelectionChangerListener() {
			super();
		}

		@Override
		public void selectionChanged(final SelectionChangedEvent event) {
			final IStructuredSelection structuredSelection = getSelection();
			doOnSelectionChange(structuredSelection);
		}
	}

	public static final String DIRECTORYNAME = File.separator + "defs"; //$NON-NLS-1$
	public static final String FILENAME = DIRECTORYNAME + File.separator + "columnsDef.xml"; //$NON-NLS-1$

	/**
	 * indicateur si le dispose des colonnes est appel� explicitement par le d�veloppeur. Utile car Eclipse n'effectue
	 * pas le meme traitement dans le cas d'un dispose appel� en interne ou explicitement par le d�veloppeur. C'est le
	 * cas pour la m�thode removeAllColumns().
	 */
	boolean columnsDisposedByDevelopper = false;
	private ArcadColumns displayDuplicate;
	protected ArcadColumns displayedColumns;


	private ColumnedViewerFilter filter = null;
	protected HeaderListener hdl = new HeaderListener();

	private boolean hideDefaultActions;

	protected AbstractInternalColumnedViewer internalViewer;
	protected IMenuManager manager;

	protected Composite parent;

	protected ArcadColumns referenceColumns;

	private IColumnedSearcher searcher = null;

	protected ColumnedSorter sorter;

	protected boolean sortOnColumn = false;

	protected int style;

	private boolean useUserPreference = true;

	private String viewerIdentifier;

	public AbstractColumnedViewer(final Composite parent, final int style) {
		this(parent, style, true);
	}

	public AbstractColumnedViewer(final Composite parent, final int style, final boolean withInit) {
		super();
		this.parent = parent;
		this.style = style;
		if (withInit) {
			init();
		}
	}

	/**
	 * M�thode permettant la cr�ation d'une colonne.<br>
	 * Impl�menter cette m�thode pour cr�er une colonne d'affichage en ad�quation avec le controle d'affichage choisi.
	 *
	 * @param widget
	 *            Widget : widget parent;
	 * @param columnStyle
	 *            int : Style SWT d'affichage
	 * @param index
	 *            int : Index de la colonne � cr�er
	 * @return Item : colonne cr��e.
	 */
	public abstract Item createColumn(Widget widget, int columnStyle, int index);

	/*---------------------------------------------------------------------
	 *                    Gestion de la cr�ation des colonnes
	 ---------------------------------------------------------------------*/
	/**
	 * M�thode de cr�ation des colonnes.<br>
	 */
	public void createColumns(final ArcadColumns columns) {
		if (hasColumns()) {
			if (sortOnColumn) {
				removeSorterOnColumn();
			}
			final Widget widget = getControl();

			Item item;
			int j = 0;
			for (int i = 0; i < columns.count(); i++) {
				final ArcadColumn col = columns.items(i);
				if (col.getVisible() == ArcadColumn.VISIBLE) {
					item = createColumn(widget, col.getStyle(), j);
					item.setText(CoreUILabels.resString(col.getUserName()));
					if (item instanceof TableColumn) {
						final String tooltipText = col.getTooltipText();
						if (!StringTools.isEmpty(tooltipText)) {
							((TableColumn) item).setToolTipText(tooltipText);
						}
					}
					setColumnValues(item, col);
					j++;
				}
			}
			if (sortOnColumn) {
				setSorterOnColumn();
			}
		}
	}

	public abstract IContentProvider createContentProvider();

	public abstract AbstractColumnedLabelProviderAdapter createLabelProvider(AbstractColumnedViewer viewer);

	/**
	 * M�thode de cr�ation du menu contextuel du control de l'afficheur.
	 */
	protected void createMenu() {
		final MenuManager menuManager = new MenuManager("#PopupMenu"); //$NON-NLS-1$
		menuManager.setRemoveAllWhenShown(true);
		menuManager.addMenuListener(AbstractColumnedViewer.this::fillContextMenu);
		getViewer().getControl().setMenu(menuManager.createContextMenu(getViewer().getControl()));
	}

	/**
	 * M�thode de cr�ation d'un viewer sp�cifique.<br>
	 * Ce sont les classes concr�tes filles qui auront la charge de cr�er ce viewer.
	 *
	 * @return AbstractInternalColumnedViewer
	 */
	public abstract AbstractInternalColumnedViewer createViewer(Composite viewerParent, int viewerStyle);

	public boolean doHideDefaultActions() {
		return hideDefaultActions;
	}

	@Override
	public void doOnApply() {
		displayedColumns = displayDuplicate.duplicate();
		saveState();
		refreshColumns();
	}

	/**
	 * M�thode permettant la gestion du double-clic.<br>
	 * Surchargez cette m�thode pour d�finir le comportement du double-clique.
	 *
	 * @param selection
	 *            IStructuredSelection S�lection
	 */
	protected void doOnDoubleClick(final IStructuredSelection selection) {
		// Do nothing
	}

	/**
	 * M�thode permettant la gestion de la s�lection.<br>
	 *
	 * @param selection
	 *            IStructuredSelection S�lection
	 */
	protected void doOnSelectionChange(final IStructuredSelection selection) {
		// Do nothing
	}

	// <FM number="2013/00188" version="08.16.04" date="28 f�vr. 2013 user="md">
	protected void extendSettings(final ColumnedViewerSettings settings) {
		// nothing
	}

	/**
	 * M�thode permettant le remplissage du menu contextuel.<br>
	 * Ce menu est automatiquement alimenter par les actions retourn�es par la m�thode {@link #makeActions()
	 * makeActions()}.
	 *
	 * @param newManager
	 *            IMenuManager Menu manager.
	 */
	protected void fillContextMenu(final IMenuManager newManager) {
		manager = newManager;
		newManager.add(new Separator("Additions"));//$NON-NLS-1$
		final Action[] actionToAdd = makeActions();
		for (final Action element : actionToAdd) {
			if (element == null || element instanceof ColumnedActionSeparator) {
				newManager.add(new Separator());
			} else if (element instanceof IActionMenuManager) {
				newManager.add(((IActionMenuManager) element).getMenuManager());
			} else {
				newManager.add(element);
			}
		}
	}

	/**
	 * M�thode permettant de renvoyer la position de la colonne de r�f�rence en fonction de l'index de la colonne
	 * d'affichage.
	 *
	 * @param displayedIndex
	 *            int : Index de la colonne d'affichage
	 * @return : Position de la colonne de r�f�rence ou -1 si non trouv�.
	 */
	public int getActualIndexFromDisplayedIndex(final int displayedIndex) {
		if (displayedIndex > -1 && displayedIndex < displayedColumns.count()) {
			final ArcadColumn c = displayedColumns.visibleItems(displayedIndex);
			if (c != null) {
				final String identifier = c.getIdentifier();
				final ArcadColumn col = referenceColumns.items(identifier);
				if (col != null) {
					return col.getPosition();
				}
			}
		}
		return -1;
	}

	/**
	 * M�thode permettant de r�cup�rer le Widget d'affichage du viewer.<br>
	 * Impl�menter cette m�thode dans les sous-classes pour renvoyer le widget r�el d'affichage.
	 *
	 * @return Widget Controle d'affichage des donn�es.
	 */
	public abstract Widget getControl();

	/**
	 * @return Returns the displayedColumns.
	 */
	public ArcadColumns getDisplayedColumns() {
		return displayedColumns;
	}

	/**
	 * M�thode permettant de renvoyer la position affich�e de la colonne de en fonction de son index de r�f�rence.
	 *
	 * @param referenceIndex
	 *            int : Index de r�f�rence de la colonne
	 * @return : index d'affichage de la colonne ou -1 si non trouv�.
	 */
	public int getDisplayedIndexFromActualIndex(final int referenceIndex) {
		if (referenceIndex > -1 && referenceIndex < referenceColumns.count()) {
			final ArcadColumn c = referenceColumns.visibleItems(referenceIndex);
			if (c != null) {
				final String identifier = c.getIdentifier();
				final ArcadColumn col = displayedColumns.items(identifier);
				if (col != null) {
					return displayedColumns.getList().indexOf(col);
				}
			}
		}
		return -1;
	}

	/**
	 * @return Returns the filter.
	 */
	public ColumnedViewerFilter getFilter() {
		return filter;
	}

	public Object getFirstSelectedObject() {
		return ((IStructuredSelection) getViewer().getSelection()).getFirstElement();
	}

	/**
	 * Possibilit� de le surcharger pour un traitement sp�cifique, notamment en retournant null pour totalement occulter
	 * la sauvegarde/chargement de la pr�sentation du viewer
	 *
	 * @return String
	 */
	public String getIdentifier() {
		return getClass().getName();
	}

	public Object getInput() {
		return getViewer().getInput();
	}

	/**
	 * M�thode permettant de r�cup�rer les colonnes de r�f�rence.<br>
	 * Les colonnes de r�f�rence sont fournies par le d�veloppeur qui devra, impl�menter la m�thode
	 * {@link #getReferenceColumns() getReferenceColumns()}.
	 *
	 * @return
	 */
	private ArcadColumns getInternalReferenceColumns() {
		return getReferenceColumns();
	}

	protected List<Action> getNextActions() {
		return new ArrayList<>();
	}

	protected List<Action> getPreviousActions() {
		return new ArrayList<>();
	}

	/**
	 * Methode permettant de sp�cifier les colonnes de r�f�rences.<br>
	 * Impl�mentez cette m�thode pour sp�cifier les colonnes de r�f�rence.
	 *
	 * @return : Collection d'ArcadColumn.
	 */
	public abstract ArcadColumns getReferenceColumns();

	/*---------------------------------------------------------------------
	 *                            Gestion des inputs
	 ---------------------------------------------------------------------*/
	/**
	 * @return Returns the searcher.
	 */
	public IColumnedSearcher getSearcher() {
		return searcher;
	}

	public IStructuredSelection getSelection() {
		return (IStructuredSelection) getViewer().getSelection();
	}

	/**
	 * M�thode permettant de renvoyer la valeur r�elle de la colonne (valeur num�rique, date ou chaine) afin de
	 * permettre un tri selon ces valeurs r�elles (et non selon leur affichage en chaine de caract�res)
	 *
	 * @param element
	 *            Object : object dont les valeurs doivent �tre affich�es
	 * @param columnIndex
	 *            int : Index de la colonne d'affichage
	 * @return : valeur de cet objet pour cette colonne
	 */
	public Object getTypedValue(final Object element, final int columnIndex) {
		return getValue(element, columnIndex);
	}

	@Override
	public abstract String getValue(Object element, int columnIndex);

	/**
	 * @return Returns the viewer.
	 */
	public StructuredViewer getViewer() {
		return internalViewer.getViewer();
	}

	// </FM>
	/**
	 * M�thoe permettant de d�finir si oui ou non le view affiche des colonnes.
	 *
	 * @return
	 */
	protected boolean hasColumns() {
		return true;
	}

	/**
	 * M�thode de gestion du double-clic.<br>
	 * Surchargez la m�thode {@link #doOnDoubleClick(IStructuredSelection) OnDoubleClick doOnDoubleClick()} pour d�finir
	 * le comportement du double-clic.
	 */
	private void hookDoubleClickAction() {
		final Action doubleClickAction = new Action() {
			@Override
			public void run() {
				final IStructuredSelection selection = (IStructuredSelection) getViewer().getSelection();
				if (!selection.isEmpty()) {
					doOnDoubleClick(selection);
				}
			}
		};
		getViewer().addDoubleClickListener(event -> doubleClickAction.run());
	}

	public void init() {
		internalViewer = createViewer(parent, style);
		referenceColumns = getInternalReferenceColumns();
		viewerIdentifier = getIdentifier();
		if (viewerIdentifier == null) {
			useUserPreference = false;
		}

		final IBaseLabelProvider labelProvider = createLabelProvider(this);
		initialize();
		final StructuredViewer structuredViewer = internalViewer.getViewer();
		if (labelProvider != null) {
			structuredViewer.setLabelProvider(labelProvider);
		}
		structuredViewer.setContentProvider(createContentProvider());

		setResizeListener();

	}

	/**
	 * M�thode d'initialisation du viewer.<br>
	 */
	public void initialize() {
		// Chargement des param�tres utilisateurs
		restoreState();
		// Cr�ation des colonnes
		createColumns(displayedColumns);
		internalViewer.setColumnProperties(displayedColumns.getIdentifiers());
		// Fixer les options du viewer
		setOptions();
		// Impl�mentation des m�canismes d'interface.
		createMenu();
		getViewer().addSelectionChangedListener(new ViewerSelectionChangerListener());

		hookDoubleClickAction();
	}

	public boolean isFiltered() {
		return filter != null;
	}

	/**
	 * M�thode permettant de d�finir les actions � ajouter dans le menu contextuel.<br>
	 *
	 * @return Action[] : Table des actions � ajouter dans le menu contextuel.
	 */
	protected Action[] makeActions() {
		if (!doHideDefaultActions()) {
			final ShowPreferencesAction showPreferencesAction = new ShowPreferencesAction();
			final ShowSearchEditorAction showSearchEditorAction = new ShowSearchEditorAction();
			final ShowFilterEditorAction showFilterAction = new ShowFilterEditorAction();
			final ShowExportAction showExportAction = new ShowExportAction();
			return new Action[] { showPreferencesAction, showFilterAction, showSearchEditorAction, showExportAction };
		} else {
			return new Action[0];
		}
	}

	public void refresh() {
		getViewer().refresh();
	}

	/**
	 * M�thode permettant la recr�ation des colonnes
	 */
	public void refreshColumns() {
		// Supression de toutes les colonnes
		columnsDisposedByDevelopper = true;
		removeAllColumns();
		columnsDisposedByDevelopper = false;
		// Recr�ation des colonnes
		createColumns(displayedColumns);
		internalViewer.setColumnProperties(displayedColumns.getIdentifiers());
		saveState();
		refresh();
	}

	/**
	 * M�thode permettant la suppression de toutes les colonnes de l'afficheur.<br>
	 */
	public abstract void removeAllColumns();

	protected void removeSorterOnColumn() {
		//Nothing to remove
	}

	private boolean restoreState() {
		ColumnedViewerSettings viewerSettings = null;
		if (useUserPreference) {
			viewerSettings = ColumnedViewerMementoTools.getInstance().getViewerSetting(viewerIdentifier);
		}
		if (viewerSettings != null) {
			displayedColumns = viewerSettings.getColumns();
			if (referenceColumns.count() > displayedColumns.count()) {
				// des colonnes ont �t� rajout�es par le d�veloppeur dans le
				// viewer
				for (int i = 0; i < referenceColumns.count(); i++) {
					ArcadColumn column = displayedColumns.items(referenceColumns.items(i).getIdentifier());
					if (column == null) {
						column = referenceColumns.items(i);
						final int position = column.getPosition();
						boolean isPositionExist = false;
						int maxPosition = -1;
						for (int j = 0; j < displayedColumns.count(); j++) {
							final int displayedPosition = displayedColumns.items(j).getPosition();
							if (displayedPosition == position) {
								isPositionExist = true;
							}
							if (displayedPosition > maxPosition) {
								maxPosition = displayedPosition;
							}
						}
						if (isPositionExist) {
							column.setPosition(maxPosition + 1);
						}
						displayedColumns.add(column);
					}
				}
			} else if (referenceColumns.count() < displayedColumns.count()) {
				// des colonnes ont �t� supprim�es par le d�veloppeur dans le
				// viewer
				for (int i = 0; i < displayedColumns.count(); i++) {
					final ArcadColumn column = referenceColumns.items(displayedColumns.items(i).getIdentifier());
					if (column == null) {
						displayedColumns.delete(i);
					}
				}
			}

			// <FM number="2013/00188" version="08.16.04" date="28 f�vr. 2013 user="md">
			if (viewerSettings.getSortCriteriaList() != null) {
				final ColumnedSortCriteriaList list = new ColumnedSortCriteriaList(displayedColumns);
				for (int i = 0; i < viewerSettings.getSortCriteriaList().getSize(); i++) {
					list.add(viewerSettings.getSortCriteriaList().getItems(i));
				}
				sorter = new ColumnedSorter(list, this);
				getViewer().setSorter(sorter);
			}
			// </FM>

		} else {
			displayedColumns = referenceColumns.duplicate();
			if (useUserPreference) {
				viewerSettings = new ColumnedViewerSettings(viewerIdentifier, displayedColumns);
				ColumnedViewerMementoTools.getInstance().setCurrentSettings(viewerSettings);
			}
		}
		return true;
	}

	/**
	 * M�thode permettant la sauvegarde des pr�f�rences utilisateurs.
	 *
	 * @return boolean true si l'op�ration s'est bien pass�e, false sinon.
	 */
	public boolean saveState() {
		if (useUserPreference) {
			setColumnsWidth();
			// <FM number="2013/00188" version="08.16.04" date="28 f�vr. 2013 user="md">
			final ColumnedViewerSettings settings = new ColumnedViewerSettings(
					AbstractColumnedViewer.this.viewerIdentifier,
					AbstractColumnedViewer.this.displayedColumns);
			extendSettings(settings);
			ColumnedViewerMementoTools.getInstance().setCurrentSettings(settings);
			// </FM>
			ColumnedViewerMementoTools.getInstance().save();
		}
		return true;
	}

	public void setCellEditors(final CellEditor[] editors) {
		internalViewer.setCellEditors(editors);
	}

	public void setCellModifier(final ICellModifier modifier) {
		internalViewer.setCellModifier(modifier);
	}

	/*----------------------------------------------------------------------
	 *	 				inplace editor
	 ----------------------------------------------------------------------*/
	public void setColumnProperties(final String[] columnProperties) {
		internalViewer.setColumnProperties(columnProperties);
	}

	/**
	 * M�thode permettant d'assigner la largeur des colonnes affichees � l'attribut width de l'objet ArcadColumn
	 */
	public void setColumnsWidth() {
		if (getViewer() instanceof TableViewer) {
			final TableColumn[] cols = ((Table) ((TableViewer) getViewer()).getControl()).getColumns();
			final Object[] colsProps = internalViewer.getColumnProperties();
			if (cols != null) {
				for (int i = 0; i < cols.length; i++) {
					if (colsProps[i] != null && !cols[i].isDisposed() && cols[i].getWidth() > 0) {
						final ArcadColumn currentColumn = displayedColumns.items(colsProps[i].toString());
						if (currentColumn != null) {
							currentColumn.setWidth(cols[i].getWidth());
						}
					}
				}
			}
		} else if (getViewer() instanceof TreeViewer) {
			final TreeColumn[] cols = ((Tree) ((TreeViewer) AbstractColumnedViewer.this.getViewer()).getControl())
					.getColumns();
			final Object[] colsProps = internalViewer.getColumnProperties();
			if (cols != null) {
				for (int i = 0; i < cols.length; i++) {
					if (!cols[i].isDisposed()) {
						final ArcadColumn currentColumn = displayedColumns.items(colsProps[i].toString());
						if (currentColumn != null) {
							currentColumn.setWidth(cols[i].getWidth());
						}

					}
				}
			}
		}
	}

	/**
	 * M�thode permettant l'affectation des propri�t�s de la colonne � l'aide des informations de description.<br>
	 *
	 * @param item
	 *            Item : Objet colonne d'affichage
	 * @param c
	 *            ArcadColumn : objet de descritpion
	 */
	public abstract void setColumnValues(Item item, ArcadColumn c);

	/**
	 * @param displayedColumns
	 *            The displayedColumns to set.
	 */
	public void setDisplayedColumns(final ArcadColumns displayedColumns) {
		this.displayedColumns = displayedColumns;
	}

	/**
	 * @param filter
	 *            The filter to set.
	 */
	public void setFilter(final ColumnedViewerFilter filter) {
		this.filter = filter;
	}

	public void setHideDefaultActions(final boolean hideDefaultActions) {
		this.hideDefaultActions = hideDefaultActions;
	}

	/*---------------------------------------------------------------------
	 *                            Gestion des inputs
	 ---------------------------------------------------------------------*/
	public void setInput(final Object input) {
		getViewer().setInput(input);
	}

	/**
	 * M�thode de param�trage de l'aspect du viewer.<br>
	 */
	protected void setOptions() {
		internalViewer.setHeaderVisible(true);
		internalViewer.setLinesVisible(true);
	}

	/**
	 * @param definitionColumns
	 *            The definitionColumns to set.
	 */
	public void setReferenceColumns(final ArcadColumns referenceColumns) {
		this.referenceColumns = referenceColumns;
	}

	private void setResizeListener() {
		final ControlListener cl = new ControlAdapter() {
			@Override
			public void controlResized(final ControlEvent e) {
				saveState();
			}
		};

		if (getViewer() instanceof TableViewer) {
			final TableColumn[] cols = ((Table) ((TableViewer) getViewer()).getControl()).getColumns();
			for (final TableColumn tc : cols) {
				tc.addControlListener(cl);
			}
		} else if (getViewer() instanceof TreeViewer) {
			final TreeColumn[] treeCols = ((Tree) ((TreeViewer) AbstractColumnedViewer.this.getViewer()).getControl())
					.getColumns();
			for (final TreeColumn tc : treeCols) {
				tc.addControlListener(cl);
			}

		}
	}

	public void setSearcher(final IColumnedSearcher searcher) {
		this.searcher = searcher;
		refresh();
	}

	protected void setSorterOnColumn() {
		//to be implemented 
	}

	public void setSortOnColumn(final boolean b) {
		if (b != sortOnColumn) {
			sortOnColumn = b;
			if (sortOnColumn) {
				setSorterOnColumn();
			} else {
				removeSorterOnColumn();
			}
		}
	}

	public void setUseUserPreference(final boolean useUserPreference) {
		this.useUserPreference = useUserPreference;
	}

	public boolean useUserPreference() {
		return useUserPreference;
	}

}