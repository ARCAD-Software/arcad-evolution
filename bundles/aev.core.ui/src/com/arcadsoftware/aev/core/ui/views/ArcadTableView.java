/*
 * Créé le 26 mai 04
 *
 * Pour changer le modèle de ce fichier généré, allez à :
 * Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
package com.arcadsoftware.aev.core.ui.views;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
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
	public static final int ACTION_PROPERTIES = 1;
	public static final int ACTION_PROMPT = 2;
	public static final int ACTION_SYNCHRONIZE = 4;

	private boolean showActionProperties = false;
	private String ViewName = "ArcadTableView"; //$NON-NLS-1$
	protected AbstractColumnedViewer viewer;
	protected boolean hookTableMenu = true;
	protected ArcadPropertyDialogAction showPropertiesAction = null;
	Action doubleClickAction = null;
	private boolean showSynchronizeAction = false;
	Action synchronizeAction;
	protected boolean autoSearch = true;
	private Cursor cursor;
	protected String initialTitle;

	protected void defineActions() {
		// Do nothing
	}

	/**
	 * @param manager
	 */
	protected void defineLocalToolbar(IToolBarManager manager) {
		// Do nothing
	}

	/**
	 * @param manager
	 */
	protected void defineLocalPullDown(IMenuManager manager) {
		// Do nothing
	}

	/**
	 * @param manager
	 */
	protected void defineLocalContextMenu(IMenuManager manager) {
		// Do nothing
	}

	protected void doOnDoubleClick() {
		// Do nothing
	}

	protected IMemento viewMemento;

	protected void restoreState() {
		// Do nothing
	}

	public ArcadTableView() {
		super();
	}

	public ArcadTableView(int style) {
		this();
		this.showActionProperties = ((style & ACTION_PROPERTIES) == ACTION_PROPERTIES);
		this.showSynchronizeAction = ((style & ACTION_SYNCHRONIZE) == ACTION_SYNCHRONIZE);
	}

	protected String basicTranslate(String key) {
		String res = CoreUILabels.resString("ArcadTableView." + key); //$NON-NLS-1$
		if (res.equals("ArcadTableView." + key)) //$NON-NLS-1$
			return key;
		return res;
	}

	public void beginAction() {
		Shell shell = EvolutionCoreUIPlugin.getDefault().getPluginShell();
		if (shell.getDisplay() != null) {
			Display display = shell.getDisplay();
			cursor = new Cursor(display, SWT.CURSOR_WAIT);
			shell.setCursor(cursor);
		}
	}

	public void endAction() {
		Shell shell = EvolutionCoreUIPlugin.getDefault().getPluginShell();
		shell.setCursor(null);
		cursor.dispose();
	}

	public void doOnSelect() {
		// Do nothing
	}

	protected void hookContextMenu() {
		MenuManager menuMgr = new MenuManager("#PopupMenu"); //$NON-NLS-1$
		menuMgr.setRemoveAllWhenShown(true);
		menuMgr.addMenuListener(new IMenuListener() {
			public void menuAboutToShow(IMenuManager manager) {
				ArcadTableView.this.fillContextMenu(manager);
			}
		});
		if (hookTableMenu) {
			Menu menu = menuMgr.createContextMenu(getStructuredViewer().getControl());
			getStructuredViewer().getControl().setMenu(menu);
			// [ML] Le menu popup devrait pouvoir être surclassé par les
			// Descendants.
			getSite().registerContextMenu(menuMgr, getStructuredViewer());
		}
	}

	protected StructuredViewer getStructuredViewer(){
		return viewer.getViewer();
	}
	
	
	protected void hookDoubleClickAction() {
		getStructuredViewer().addDoubleClickListener(new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent event) {
				doubleClickAction.run();
			}
		});
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
			synchronizeAction = new Action(CoreUILabels.resString("Action.synchronizeAction.Text"),//$NON-NLS-1$
					IAction.AS_CHECK_BOX) {
				@Override
				public void run() {
					autoSearch = synchronizeAction.isChecked();
					if (autoSearch)
						processSelectionChanged();
					synchronizationChanged();
				}
			};
			synchronizeAction.setToolTipText(CoreUILabels.resString("Action.synchronizeAction.Tooltip"));//$NON-NLS-1$
			synchronizeAction.setImageDescriptor(Icon.SYNCHRONIZE.imageDescriptor());
			synchronizeAction.setChecked(true);
		}
		defineActions();
	}

	protected void contributeToActionBars() {
		IActionBars bars = getViewSite().getActionBars();
		fillLocalPullDown(bars.getMenuManager());
		fillLocalToolBar(bars.getToolBarManager());
		bars.updateActionBars();
		bars.getToolBarManager().update(true);
	}

	protected void fillLocalPullDown(IMenuManager manager) {
		defineLocalPullDown(manager);
		manager.update(true);
	}

	protected void fillContextMenu(IMenuManager manager) {
		defineLocalContextMenu(manager);
		if (showActionProperties) {
			manager.add(new Separator());
			manager.add(showPropertiesAction);
		}
		manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
	}

	protected void fillLocalToolBar(IToolBarManager manager) {
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

	@Override
	public void createPartControl(Composite parent) {
		setInterface();
		initialTitle = this.getTitle();
		hookContextMenu();
		hookDoubleClickAction();
	}

	public void setInterface() {
		makeAction();
		contributeToActionBars();
	}

	@Override
	public void setFocus() {
		try {
			if (viewer != null && getStructuredViewer() != null && viewer.getControl() != null)
				getStructuredViewer().getControl().setFocus();
		} catch (Exception e) {
			// Do nothing
		}
	}

	protected void processSelectionChanged() {
		// Do nothing
	}

	protected void synchronizationChanged() {
		// Do nothing
	}

	public void selectionChanged(SelectionChangedEvent event) {
		// Do nothing
	}

	public String getViewName() {
		return ViewName;
	}

	public void setViewName(String string) {
		ViewName = string;
	}

	@Override
	public void init(IViewSite site, IMemento memento) throws PartInitException {
		super.init(site, memento);
		this.viewMemento = memento;
	}

	public AbstractColumnedViewer getViewer() {
		return viewer;
	}

	public IMemento getViewMemento() {
		return viewMemento;
	}

	public void onActivation() {
		// Do nothing
	}
}
