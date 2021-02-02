/*
 * Créé le 26 mai 04
 *
 * Pour changer le modèle de ce fichier généré, allez à :
 * Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
package com.arcadsoftware.aev.core.ui.views;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.ViewPart;

import com.arcadsoftware.aev.core.ui.EvolutionCoreUIPlugin;
import com.arcadsoftware.aev.core.ui.actions.ArcadPropertyDialogAction;
import com.arcadsoftware.aev.core.ui.tools.CoreUILabels;
import com.arcadsoftware.aev.core.ui.viewers.columned.AbstractColumnedViewer;
import com.arcadsoftware.documentation.icons.Icon;

/**
 * @author MD
 */
public class ArcadTableView extends ViewPart implements ISelectionChangedListener {

	public static final int ACTION_NONE = 0;
	public static final int ACTION_PROMPT = 2;
	public static final int ACTION_PROPERTIES = 1;
	public static final int ACTION_SYNCHRONIZE = 4;

	protected boolean autoSearch = true;
	private Cursor cursor;
	Action doubleClickAction = null;
	protected boolean hookTableMenu = true;
	protected String initialTitle;
	private boolean showActionProperties = false;
	protected ArcadPropertyDialogAction showPropertiesAction = null;
	private boolean showSynchronizeAction = false;
	Action synchronizeAction;
	protected AbstractColumnedViewer viewer;
	protected IMemento viewMemento;

	private String ViewName = "ArcadTableView"; //$NON-NLS-1$

	public ArcadTableView() {
		super();
	}

	public ArcadTableView(final int style) {
		this();
		showActionProperties = (style & ACTION_PROPERTIES) == ACTION_PROPERTIES;
		showSynchronizeAction = (style & ACTION_SYNCHRONIZE) == ACTION_SYNCHRONIZE;
	}

	protected String basicTranslate(final String key) {
		final String res = CoreUILabels.resString("ArcadTableView." + key); //$NON-NLS-1$
		if (res.equals("ArcadTableView." + key)) {
			return key;
		}
		return res;
	}

	public void beginAction() {
		final Shell shell = EvolutionCoreUIPlugin.getDefault().getPluginShell();
		if (shell.getDisplay() != null) {
			final Display display = shell.getDisplay();
			cursor = new Cursor(display, SWT.CURSOR_WAIT);
			shell.setCursor(cursor);
		}
	}

	protected void contributeToActionBars() {
		final IActionBars bars = getViewSite().getActionBars();
		fillLocalPullDown(bars.getMenuManager());
		fillLocalToolBar(bars.getToolBarManager());
		bars.updateActionBars();
		bars.getToolBarManager().update(true);
	}

	@Override
	public void createPartControl(final Composite parent) {
		setInterface();
		initialTitle = getTitle();
		hookContextMenu();
		hookDoubleClickAction();
	}

	protected void defineActions() {
		// Do nothing
	}

	/**
	 * @param manager
	 */
	protected void defineLocalContextMenu(final IMenuManager manager) {
		// Do nothing
	}

	/**
	 * @param manager
	 */
	protected void defineLocalPullDown(final IMenuManager manager) {
		// Do nothing
	}

	/**
	 * @param manager
	 */
	protected void defineLocalToolbar(final IToolBarManager manager) {
		// Do nothing
	}

	protected void doOnDoubleClick() {
		// Do nothing
	}

	public void doOnSelect() {
		// Do nothing
	}

	public void endAction() {
		final Shell shell = EvolutionCoreUIPlugin.getDefault().getPluginShell();
		shell.setCursor(null);
		cursor.dispose();
	}

	protected void fillContextMenu(final IMenuManager manager) {
		defineLocalContextMenu(manager);
		if (showActionProperties) {
			manager.add(new Separator());
			manager.add(showPropertiesAction);
		}
		manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
	}

	protected void fillLocalPullDown(final IMenuManager manager) {
		defineLocalPullDown(manager);
		manager.update(true);
	}

	protected void fillLocalToolBar(final IToolBarManager manager) {
		if (showActionProperties) {
			manager.add(showPropertiesAction);
			manager.add(new Separator());
		}
		if (showSynchronizeAction) {
			manager.add(synchronizeAction);
			manager.add(new Separator());
		}
		defineLocalToolbar(manager);
	}

	protected StructuredViewer getStructuredViewer() {
		return viewer.getViewer();
	}

	public AbstractColumnedViewer getViewer() {
		return viewer;
	}

	public IMemento getViewMemento() {
		return viewMemento;
	}

	public String getViewName() {
		return ViewName;
	}

	protected void hookContextMenu() {
		final MenuManager menuMgr = new MenuManager("#PopupMenu"); //$NON-NLS-1$
		menuMgr.setRemoveAllWhenShown(true);
		menuMgr.addMenuListener(manager -> ArcadTableView.this.fillContextMenu(manager));
		if (hookTableMenu) {
			final Menu menu = menuMgr.createContextMenu(getStructuredViewer().getControl());
			getStructuredViewer().getControl().setMenu(menu);
			// [ML] Le menu popup devrait pouvoir être surclassé par les
			// Descendants.
			getSite().registerContextMenu(menuMgr, getStructuredViewer());
		}
	}

	protected void hookDoubleClickAction() {
		getStructuredViewer().addDoubleClickListener(event -> doubleClickAction.run());
	}

	@Override
	public void init(final IViewSite site, final IMemento memento) throws PartInitException {
		super.init(site, memento);
		viewMemento = memento;
	}

	protected void makeAction() {
		if (showActionProperties) {
			if (showPropertiesAction == null) {
				showPropertiesAction = new ArcadPropertyDialogAction(EvolutionCoreUIPlugin.getDefault()
						.getShellProvider(), viewer.getViewer());
				showPropertiesAction.setText(CoreUILabels.resString("ArcadTableView.properties.Text")); //$NON-NLS-1$
				showPropertiesAction.setToolTipText(CoreUILabels.resString("ArcadTableView.properties.Tooltip")); //$NON-NLS-1$
				showPropertiesAction.setImageDescriptor(Icon.PROPERTIES.imageDescriptor());
			}
		}
		if (doubleClickAction == null) {
			doubleClickAction = new Action() {
				@Override
				public void run() {
					doOnDoubleClick();
				}
			};
		}

		if (showSynchronizeAction) {
			synchronizeAction = new Action(CoreUILabels.resString("Action.synchronizeAction.Text"), //$NON-NLS-1$
					IAction.AS_CHECK_BOX) {
				@Override
				public void run() {
					autoSearch = synchronizeAction.isChecked();
					if (autoSearch) {
						processSelectionChanged();
					}
					synchronizationChanged();
				}
			};
			synchronizeAction.setToolTipText(CoreUILabels.resString("Action.synchronizeAction.Tooltip"));//$NON-NLS-1$
			synchronizeAction.setImageDescriptor(Icon.SYNCHRONIZE.imageDescriptor());
			synchronizeAction.setChecked(true);
		}
		defineActions();
	}

	public void onActivation() {
		// Do nothing
	}

	protected void processSelectionChanged() {
		// Do nothing
	}

	protected void restoreState() {
		// Do nothing
	}

	@Override
	public void selectionChanged(final SelectionChangedEvent event) {
		// Do nothing
	}

	@Override
	public void setFocus() {
		try {
			if (viewer != null && getStructuredViewer() != null && viewer.getControl() != null) {
				getStructuredViewer().getControl().setFocus();
			}
		} catch (final Exception e) {
			// Do nothing
		}
	}

	public void setInterface() {
		makeAction();
		contributeToActionBars();
	}

	public void setViewName(final String string) {
		ViewName = string;
	}

	protected void synchronizationChanged() {
		// Do nothing
	}
}
