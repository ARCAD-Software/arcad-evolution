package com.arcadsoftware.aev.core.ui;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;

import com.arcadsoftware.aev.core.model.ArcadPlugin;
import com.arcadsoftware.aev.core.tools.IHelperRessource;
import com.arcadsoftware.aev.core.ui.selectionproviders.ArcadItemSelectionChangedProvider;
import com.arcadsoftware.aev.core.ui.tools.CoreUILabels;
import com.arcadsoftware.aev.core.ui.tools.IFileManagerProvider;

/**
 * The main plugin class to be used in the desktop.
 */
public class EvolutionCoreUIPlugin extends ArcadPlugin implements IHelperRessource {

	private static ArcadItemSelectionChangedProvider changedProvider = new ArcadItemSelectionChangedProvider();
	public static final String PREF_MESSAGE_LEVELVIEW = "ARCADCORE_Message_LevelViewFilter"; //$NON-NLS-1$
	public static final String PREF_MESSAGES_LEVEL = "ACP_MESSAGES_LEVEL_PREF"; //$NON-NLS-1$

	// ICONES
	public static final String ACT_REFRESH = "refresh.gif"; //$NON-NLS-1$
	public static final String ICO_ARCAD = "arcad16.gif"; //$NON-NLS-1$
	public static final String ICO_CLEAR = "removeAll.gif"; //$NON-NLS-1$
	public static final String ICO_SAVEAS = "saveas.gif"; //$NON-NLS-1$
	public static final String ICO_CLEARSELECTION = "remove.gif"; //$NON-NLS-1$
	public final static String ICO_CLM_ADD = "clm/add.png"; //$NON-NLS-1$
	public final static String ICO_CLM_ADD_ALL = "clm/add_all.png";//$NON-NLS-1$
	public final static String ICO_CLM_REMOVE = "clm/remove.png";//$NON-NLS-1$
	public final static String ICO_CLM_REMOVE_ALL = "clm/remove_all.png";//$NON-NLS-1$
	public final static String ICO_CLM_SELECT_NEXT = "clm/select_next.gif";//$NON-NLS-1$
	public final static String ICO_CLM_SELECT_PREV = "clm/select_prev.gif"; //$NON-NLS-1$
	public static final String ICO_MES_COMPL = "mes/compl.gif"; //$NON-NLS-1$
	public static final String ICO_MES_DIAG = "mes/diag.gif"; //$NON-NLS-1$
	public static final String ICO_MES_ERROR = "mes/error.gif"; //$NON-NLS-1$
	public static final String ICO_MES_EXCEP = "mes/excep.gif"; //$NON-NLS-1$
	public static final String ICO_MES_WARN = "mes/warn.gif"; //$NON-NLS-1$
	public static final String ICO_MESDETAIL_COMPL = "mes/dtl_compl.gif"; //$NON-NLS-1$
	public static final String ICO_MESDETAIL_DIAG = "mes/dtl_diag.gif"; //$NON-NLS-1$
	public static final String ICO_MESDETAIL_ERROR = "mes/dtl_error.gif"; //$NON-NLS-1$
	public static final String ICO_MESDETAIL_EXCEP = "mes/dtl_excep.gif"; //$NON-NLS-1$
	public static final String ICO_MESDETAIL_WARN = "mes/dtl_warn.gif"; //$NON-NLS-1$
	public static final String ACT_PROPERTIES = "properties.png"; //$NON-NLS-1$
	public static final String ACT_SORT = "sort.png"; //$NON-NLS-1$
	public static final String ACT_PREFS = "prefs.png"; //$NON-NLS-1$
	public static final String ACT_SEARCH = "search.png"; //$NON-NLS-1$
	public static final String ACT_FILTER = "filter.png"; //$NON-NLS-1$
	public static final String ACT_FIND = "find.png"; //$NON-NLS-1$
	public static final String ACT_EXPORT = "export.png"; //$NON-NLS-1$
	public static final String ACT_SYNC = "synced.gif"; //$NON-NLS-1$
	public static final String IMG_WIZARD = "wizard.gif"; //$NON-NLS-1$
	public static final String ARROW_COLLAPSED = "arw_col.gif"; //$NON-NLS-1$
	public static final String ARROW_EXPANDED = "arw_exp.gif"; //$NON-NLS-1$
	public static final String ACT_ERASE = "erase.png"; //$NON-NLS-1$
	public static final String ICO_SRVMAN = "srvman.gif"; //$NON-NLS-1$
	public static final String ICO_CHECKED = "checked.gif"; //$NON-NLS-1$
	public static final String ICO_UNCHECKED = "unchecked.gif"; //$NON-NLS-1$
	public static final String CALENDAR_ICON = "calendar.gif"; //$NON-NLS-1$

	private static EvolutionCoreUIPlugin plugin;
	private ResourceBundle resourceBundle;
	
	
	
	IFileManagerProvider fileManagerProvider = null;
	

	/**
	 * [MV3]
	 */
	public EvolutionCoreUIPlugin() {
		super();
		plugin = this;
		CoreUILabels.getInstance().setHelper(this);
		try {
			resourceBundle = ResourceBundle.getBundle(
					"com.arcadsoftware.aev.core.ui.EvolutionResources", Locale.getDefault()); //$NON-NLS-1$
		} catch (MissingResourceException x) {
			resourceBundle = null;
		}
	}

	/**
	 * Returns the shared instance.
	 */
	public static EvolutionCoreUIPlugin getDefault() {
		// If the instance has not been initialized, we will wait.
		// This can occur if multiple threads try to load the plugin at the same
		// time (see bug 33825:
		// http://bugs.eclipse.org/bugs/show_bug.cgi?id=33825)
		while (plugin == null) {
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				// ignore and keep trying
			}
		}
		return plugin;
	}

	/**
	 * Returns the workspace instance.
	 */
	public static IWorkspace getWorkspace() {
		return ResourcesPlugin.getWorkspace();
	}

	/**
	 * Returns the string from the plugin's resource bundle, or 'key' if not
	 * found.
	 */
	public static String getResourceString(String key) {
		ResourceBundle bundle = EvolutionCoreUIPlugin.getDefault().getResourceBundle();
		try {
			return bundle.getString(key);
		} catch (MissingResourceException e) {
			return key;
		}
	}

	/**
	 * Returns the string from the plugin's resource bundle, or 'key' if not
	 * found.
	 */
	public static String getHelpId(String key) {
		return IDocProvider.ID.concat(".").concat(key); //$NON-NLS-1$
	}

	/**
	 * Returns the plugin's resource bundle,
	 */
	public ResourceBundle getResourceBundle() {
		return resourceBundle;
	}

	public int getMessageLevelFilter() {
		return getPreferenceStore().getInt(PREF_MESSAGE_LEVELVIEW);
	}

	public void setMessageLevelFilter(int level) {
		getPreferenceStore().setValue(PREF_MESSAGE_LEVELVIEW, level);
	}

	public static boolean getBooleanPreference(String id) {
		return getDefault().getPreferenceStore().getBoolean(id);
	}

	/**
	 * Obtenir la valeur d'une préférence de type Entier
	 * 
	 * @param id
	 *            l'identifiant de la préférence
	 */
	public static int getIntegerPreference(String id) {
		return getDefault().getPreferenceStore().getInt(id);
	}

	@Override
	protected void initializeImageRegistry() {
		String path = getIconPath();
		putImageInRegistry(ACT_REFRESH, path + ACT_REFRESH);
		putImageInRegistry(ICO_ARCAD, path + ICO_ARCAD);
		putImageInRegistry(ICO_CLEAR, path + ICO_CLEAR);
		putImageInRegistry(ICO_SAVEAS, path + ICO_SAVEAS);
		putImageInRegistry(ICO_CLEARSELECTION, path + ICO_CLEARSELECTION);
		putImageInRegistry(ICO_CLM_ADD, path + ICO_CLM_ADD);
		putImageInRegistry(ICO_CLM_ADD_ALL, path + ICO_CLM_ADD_ALL);
		putImageInRegistry(ICO_CLM_REMOVE, path + ICO_CLM_REMOVE);
		putImageInRegistry(ICO_CLM_REMOVE_ALL, path + ICO_CLM_REMOVE_ALL);
		putImageInRegistry(ICO_CLM_SELECT_NEXT, path + ICO_CLM_SELECT_NEXT);
		putImageInRegistry(ICO_CLM_SELECT_PREV, path + ICO_CLM_SELECT_PREV);
		putImageInRegistry(ICO_MES_COMPL, path + ICO_MES_COMPL);
		putImageInRegistry(ICO_MES_DIAG, path + ICO_MES_DIAG);
		putImageInRegistry(ICO_MES_ERROR, path + ICO_MES_ERROR);
		putImageInRegistry(ICO_MES_EXCEP, path + ICO_MES_EXCEP);
		putImageInRegistry(ICO_MES_WARN, path + ICO_MES_WARN);
		putImageInRegistry(ICO_MESDETAIL_COMPL, path + ICO_MESDETAIL_COMPL);
		putImageInRegistry(ICO_MESDETAIL_DIAG, path + ICO_MESDETAIL_DIAG);
		putImageInRegistry(ICO_MESDETAIL_ERROR, path + ICO_MESDETAIL_ERROR);
		putImageInRegistry(ICO_MESDETAIL_EXCEP, path + ICO_MESDETAIL_EXCEP);
		putImageInRegistry(ICO_MESDETAIL_WARN, path + ICO_MESDETAIL_WARN);
		putImageInRegistry(ACT_PROPERTIES, path + ACT_PROPERTIES);
		putImageInRegistry(ACT_SORT, path + ACT_SORT);
		putImageInRegistry(ACT_PREFS, path + ACT_PREFS);
		putImageInRegistry(ACT_SEARCH, path + ACT_SEARCH);
		putImageInRegistry(ACT_FILTER, path + ACT_FILTER);
		putImageInRegistry(ACT_FIND, path + ACT_FIND);
		putImageInRegistry(ACT_EXPORT, path + ACT_EXPORT);
		putImageInRegistry(ACT_SYNC, path + ACT_SYNC);
		putImageInRegistry(IMG_WIZARD, path + IMG_WIZARD);
		putImageInRegistry(ARROW_COLLAPSED, path + ARROW_COLLAPSED);
		putImageInRegistry(ARROW_EXPANDED, path + ARROW_EXPANDED);
		putImageInRegistry(ACT_ERASE, path + ACT_ERASE);
		putImageInRegistry(ICO_SRVMAN, path + ICO_SRVMAN);
		putImageInRegistry(ICO_CHECKED, path + ICO_CHECKED);
		putImageInRegistry(ICO_UNCHECKED, path + ICO_UNCHECKED);
		putImageInRegistry(CALENDAR_ICON, path + CALENDAR_ICON);
	}

	public static ArcadItemSelectionChangedProvider getChangedProvider() {
		return changedProvider;
	}

	private static IWorkbenchPage getPluginPage() {
		IWorkbenchPage page = null;
		if (getDefault().getWorkbench() != null) {
			if (getDefault().getWorkbench().getActiveWorkbenchWindow() != null)
				page = getDefault().getWorkbench().getActiveWorkbenchWindow().getActivePage();
			else if (getDefault().getWorkbench().getWorkbenchWindows().length > 0)
				page = (getDefault().getWorkbench().getWorkbenchWindows()[0]).getActivePage();
		}
		try {
			if (page != null)
				return page;
			return null;
		} catch (Exception e) {
			return null;
		}
	}

	public static IViewPart openView(String ID) {
		IWorkbenchPage page = getPluginPage();
		if (page != null)
			return page.findView(ID);
		return null;
	}

	public static IViewPart showView(String ID) {
		IWorkbenchPage page = getPluginPage();
		if (page != null)
			try {
				return page.showView(ID, null, IWorkbenchPage.VIEW_ACTIVATE);
			} catch (PartInitException e) {
				e.printStackTrace();
				return null;
			}
		return null;
	}

	public static IViewPart findView(String ID) {
		IWorkbenchPage page = getPluginPage();
		if (page != null)
			return page.findView(ID);
		return null;
	}

	/**
	 * Convenience method for getting the current shell.
	 * 
	 * @return the shell
	 */
	public static Shell getShell() {
		return getDefault().getWorkbench().getActiveWorkbenchWindow().getShell();
	}

	public static IDialogSettings getSettings(String sectionName) {
		if (plugin.getDialogSettings().getSection(sectionName) == null)
			return plugin.getDialogSettings().addNewSection(sectionName);
		return plugin.getDialogSettings().getSection(sectionName);
	}

	@Override
	public String resString(String key) {
		return getResourceString(key);
	}

	public void initializeDefaultPreferences() {
		IPreferenceStore store = EvolutionCoreUIPlugin.getDefault().getPreferenceStore();
		store.setDefault(EvolutionCoreUIPlugin.PREF_MESSAGE_LEVELVIEW, -1);
	}

	/**
	 * Obtenir le niveau de détail des messages à l'affichage.
	 * 
	 * @return int
	 */
	public int getMessagesLevel() {
		return getPreferenceStore().getInt(PREF_MESSAGES_LEVEL);
	}

	/**
	 * Modifie le niveau de détaild es messages à l'affichage.
	 * 
	 * @param value
	 */
	public void setMessagesLevel(int value) {
		getPreferenceStore().setValue(PREF_MESSAGES_LEVEL, value);
	}
	
	

	public IFileManagerProvider getFileManagerProvider(){
		return fileManagerProvider;
	}
	public void setFileManagerProvider(IFileManagerProvider fileManagerProvider){
		this.fileManagerProvider = fileManagerProvider;
	}		
	
}
