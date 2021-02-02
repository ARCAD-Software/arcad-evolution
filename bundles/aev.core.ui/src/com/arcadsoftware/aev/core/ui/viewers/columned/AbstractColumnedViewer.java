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
import com.arcadsoftware.documentation.icons.Icon;

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
		 * Méthode d'exécution du tri multicritère avec message de progression Les éléments sont alors triés et affichés
		 * dans le viewer spécifique
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
				showFilter = new ColumnedViewerFilter(list, displayedColumns, AbstractColumnedViewer.this);
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
			// Affichage du dialog de préférences
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
	 * indicateur si le dispose des colonnes est appelé explicitement par le développeur. Utile car Eclipse n'effectue
	 * pas le meme traitement dans le cas d'un dispose appelé en interne ou explicitement par le développeur. C'est le
	 * cas pour la méthode removeAllColumns().
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
	 * Méthode permettant la création d'une colonne.<br>
	 * Implémenter cette méthode pour créer une colonne d'affichage en adéquation avec le controle d'affichage choisi.
	 *
	 * @param widget
	 *            Widget : widget parent;
	 * @param columnStyle
	 *            int : Style SWT d'affichage
	 * @param index
	 *            int : Index de la colonne à créer
	 * @return Item : colonne créée.
	 */
	public abstract Item createColumn(Widget widget, int columnStyle, int index);

	/*---------------------------------------------------------------------
	 *                    Gestion de la création des colonnes
	 ---------------------------------------------------------------------*/
	/**
	 * Méthode de création des colonnes.<br>
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
	 * Méthode de création du menu contextuel du control de l'afficheur.
	 */
	protected void createMenu() {
		final MenuManager menuManager = new MenuManager("#PopupMenu"); //$NON-NLS-1$
		menuManager.setRemoveAllWhenShown(true);
		menuManager.addMenuListener(AbstractColumnedViewer.this::fillContextMenu);
		getViewer().getControl().setMenu(menuManager.createContextMenu(getViewer().getControl()));
	}

	/**
	 * Méthode de création d'un viewer spécifique.<br>
	 * Ce sont les classes concrètes filles qui auront la charge de créer ce viewer.
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
	 * Méthode permettant la gestion du double-clic.<br>
	 * Surchargez cette méthode pour définir le comportement du double-clique.
	 *
	 * @param selection
	 *            IStructuredSelection Sélection
	 */
	protected void doOnDoubleClick(final IStructuredSelection selection) {
		// Do nothing
	}

	/**
	 * Méthode permettant la gestion de la sélection.<br>
	 *
	 * @param selection
	 *            IStructuredSelection Sélection
	 */
	protected void doOnSelectionChange(final IStructuredSelection selection) {
		// Do nothing
	}

	// <FM number="2013/00188" version="08.16.04" date="28 févr. 2013 user="md">
	protected void extendSettings(final ColumnedViewerSettings settings) {
		// nothing
	}

	/**
	 * Méthode permettant le remplissage du menu contextuel.<br>
	 * Ce menu est automatiquement alimenter par les actions retournées par la méthode {@link #makeActions()
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
	 * Méthode permettant de renvoyer la position de la colonne de référence en fonction de l'index de la colonne
	 * d'affichage.
	 *
	 * @param displayedIndex
	 *            int : Index de la colonne d'affichage
	 * @return : Position de la colonne de référence ou -1 si non trouvé.
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
	 * Méthode permettant de récupérer le Widget d'affichage du viewer.<br>
	 * Implémenter cette méthode dans les sous-classes pour renvoyer le widget réel d'affichage.
	 *
	 * @return Widget Controle d'affichage des données.
	 */
	public abstract Widget getControl();

	/**
	 * @return Returns the displayedColumns.
	 */
	public ArcadColumns getDisplayedColumns() {
		return displayedColumns;
	}

	/**
	 * Méthode permettant de renvoyer la position affichée de la colonne de en fonction de son index de référence.
	 *
	 * @param referenceIndex
	 *            int : Index de référence de la colonne
	 * @return : index d'affichage de la colonne ou -1 si non trouvé.
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
	 * Possibilité de le surcharger pour un traitement spécifique, notamment en retournant null pour totalement occulter
	 * la sauvegarde/chargement de la présentation du viewer
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
	 * Méthode permettant de récupérer les colonnes de référence.<br>
	 * Les colonnes de référence sont fournies par le développeur qui devra, implémenter la méthode
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
	 * Methode permettant de spécifier les colonnes de références.<br>
	 * Implémentez cette méthode pour spécifier les colonnes de référence.
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
	 * Méthode permettant de renvoyer la valeur réelle de la colonne (valeur numérique, date ou chaine) afin de
	 * permettre un tri selon ces valeurs réelles (et non selon leur affichage en chaine de caractères)
	 *
	 * @param element
	 *            Object : object dont les valeurs doivent être affichées
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
	 * Méthoe permettant de définir si oui ou non le view affiche des colonnes.
	 *
	 * @return
	 */
	protected boolean hasColumns() {
		return true;
	}

	/**
	 * Méthode de gestion du double-clic.<br>
	 * Surchargez la méthode {@link #doOnDoubleClick(IStructuredSelection) OnDoubleClick doOnDoubleClick()} pour définir
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
	 * Méthode d'initialisation du viewer.<br>
	 */
	public void initialize() {
		// Chargement des paramétres utilisateurs
		restoreState();
		// Création des colonnes
		createColumns(displayedColumns);
		internalViewer.setColumnProperties(displayedColumns.getIdentifiers());
		// Fixer les options du viewer
		setOptions();
		// Implémentation des mécanismes d'interface.
		createMenu();
		getViewer().addSelectionChangedListener(new ViewerSelectionChangerListener());

		hookDoubleClickAction();
	}

	public boolean isFiltered() {
		return filter != null;
	}

	/**
	 * Méthode permettant de définir les actions à ajouter dans le menu contextuel.<br>
	 *
	 * @return Action[] : Table des actions à ajouter dans le menu contextuel.
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
	 * Méthode permettant la recréation des colonnes
	 */
	public void refreshColumns() {
		// Supression de toutes les colonnes
		columnsDisposedByDevelopper = true;
		removeAllColumns();
		columnsDisposedByDevelopper = false;
		// Recréation des colonnes
		createColumns(displayedColumns);
		internalViewer.setColumnProperties(displayedColumns.getIdentifiers());
		saveState();
		refresh();
	}

	/**
	 * Méthode permettant la suppression de toutes les colonnes de l'afficheur.<br>
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
				// des colonnes ont été rajoutées par le développeur dans le
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
				// des colonnes ont été supprimées par le développeur dans le
				// viewer
				for (int i = 0; i < displayedColumns.count(); i++) {
					final ArcadColumn column = referenceColumns.items(displayedColumns.items(i).getIdentifier());
					if (column == null) {
						displayedColumns.delete(i);
					}
				}
			}

			// <FM number="2013/00188" version="08.16.04" date="28 févr. 2013 user="md">
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
	 * Méthode permettant la sauvegarde des préférences utilisateurs.
	 *
	 * @return boolean true si l'opération s'est bien passée, false sinon.
	 */
	public boolean saveState() {
		if (useUserPreference) {
			setColumnsWidth();
			// <FM number="2013/00188" version="08.16.04" date="28 févr. 2013 user="md">
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
	 * Méthode permettant d'assigner la largeur des colonnes affichees à l'attribut width de l'objet ArcadColumn
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
	 * Méthode permettant l'affectation des propriétés de la colonne à l'aide des informations de description.<br>
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
	 * Méthode de paramétrage de l'aspect du viewer.<br>
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