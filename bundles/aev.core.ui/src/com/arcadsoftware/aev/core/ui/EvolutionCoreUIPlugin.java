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

import com.arcadsoftware.aev.core.messages.MessageManager;
import com.arcadsoftware.aev.core.osgi.ServiceNotFoundException;
import com.arcadsoftware.aev.core.osgi.ServiceRegistry;
import com.arcadsoftware.aev.core.tools.IHelperLabel;
import com.arcadsoftware.aev.core.ui.model.ArcadUIPlugin;
import com.arcadsoftware.aev.core.ui.model.IHelperImage;
import com.arcadsoftware.aev.core.ui.selectionproviders.ArcadItemSelectionChangedProvider;
import com.arcadsoftware.aev.core.ui.tools.CoreUILabels;
import com.arcadsoftware.aev.core.ui.tools.IFileManagerProvider;

/**
 * The main plugin class to be used in the desktop.
 */
public class EvolutionCoreUIPlugin extends ArcadUIPlugin implements IHelperImage, IHelperLabel {

	private static ArcadItemSelectionChangedProvider changedProvider = new ArcadItemSelectionChangedProvider();
	private static EvolutionCoreUIPlugin plugin;
	public static final String PREF_MESSAGE_LEVELVIEW = "ARCADCORE_Message_LevelViewFilter"; //$NON-NLS-1$

	public static final String PREF_MESSAGES_LEVEL = "ACP_MESSAGES_LEVEL_PREF"; //$NON-NLS-1$

	public static IViewPart findView(final String ID) {
		final IWorkbenchPage page = getPluginPage();
		if (page != null) {
			return page.findView(ID);
		}
		return null;
	}

	public static boolean getBooleanPreference(final String id) {
		return getDefault().getPreferenceStore().getBoolean(id);
	}

	public static ArcadItemSelectionChangedProvider getChangedProvider() {
		return changedProvider;
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
			} catch (final InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		}
		return plugin;
	}

	/**
	 * Returns the string from the plugin's resource bundle, or 'key' if not found.
	 */
	public static String getHelpId(final String key) {
		return IDocProvider.ID.concat(".").concat(key); //$NON-NLS-1$
	}

	/**
	 * Obtenir la valeur d'une préférence de type Entier
	 *
	 * @param id
	 *            l'identifiant de la préférence
	 */
	public static int getIntegerPreference(final String id) {
		return getDefault().getPreferenceStore().getInt(id);
	}

	private static IWorkbenchPage getPluginPage() {
		IWorkbenchPage page = null;
		if (getDefault().getWorkbench() != null) {
			if (getDefault().getWorkbench().getActiveWorkbenchWindow() != null) {
				page = getDefault().getWorkbench().getActiveWorkbenchWindow().getActivePage();
			} else if (getDefault().getWorkbench().getWorkbenchWindows().length > 0) {
				page = getDefault().getWorkbench().getWorkbenchWindows()[0].getActivePage();
			}
		}
		try {
			if (page != null) {
				return page;
			}
			return null;
		} catch (final Exception e) {
			return null;
		}
	}

	/**
	 * Returns the string from the plugin's resource bundle, or 'key' if not found.
	 */
	public static String getResourceString(final String key) {
		final ResourceBundle bundle = EvolutionCoreUIPlugin.getDefault().getResourceBundle();
		try {
			return bundle.getString(key);
		} catch (final MissingResourceException e) {
			return key;
		}
	}

	public static IDialogSettings getSettings(final String sectionName) {
		if (plugin.getDialogSettings().getSection(sectionName) == null) {
			return plugin.getDialogSettings().addNewSection(sectionName);
		}
		return plugin.getDialogSettings().getSection(sectionName);
	}

	/**
	 * Convenience method for getting the current shell.
	 *
	 * @return the shell
	 */
	public static Shell getShell() {
		return getDefault().getWorkbench().getActiveWorkbenchWindow().getShell();
	}

	/**
	 * Returns the workspace instance.
	 */
	public static IWorkspace getWorkspace() {
		return ResourcesPlugin.getWorkspace();
	}

	public static IViewPart openView(final String id) {		
		return findView(id);
	}

	public static IViewPart showView(final String ID) {
		final IWorkbenchPage page = getPluginPage();
		if (page != null) {
			try {
				return page.showView(ID, null, IWorkbenchPage.VIEW_ACTIVATE);
			} catch (final PartInitException e) {
				MessageManager.addAndPrintException(e);
				return null;
			}
		}
		return null;
	}

	IFileManagerProvider fileManagerProvider = null;

	private ResourceBundle resourceBundle;

	/**
	 * [MV3]
	 */
	public EvolutionCoreUIPlugin() {
		super();
		plugin = this;
		CoreUILabels.getInstance().setImageHelper(this);
		CoreUILabels.getInstance().setLabelHelper(this);
		try {
			resourceBundle = ResourceBundle.getBundle(
					"com.arcadsoftware.aev.core.ui.EvolutionResources", Locale.getDefault()); //$NON-NLS-1$
		} catch (final MissingResourceException x) {
			resourceBundle = null;
		}
	}

	public IFileManagerProvider getFileManagerProvider() {
		return ServiceRegistry.lookup(IFileManagerProvider.class)
				.orElseThrow(() -> new ServiceNotFoundException(IFileManagerProvider.class));
	}

	public int getMessageLevelFilter() {
		return getPreferenceStore().getInt(PREF_MESSAGE_LEVELVIEW);
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
	 * Returns the plugin's resource bundle,
	 */
	public ResourceBundle getResourceBundle() {
		return resourceBundle;
	}

	public void initializeDefaultPreferences() {
		final IPreferenceStore store = EvolutionCoreUIPlugin.getDefault().getPreferenceStore();
		store.setDefault(EvolutionCoreUIPlugin.PREF_MESSAGE_LEVELVIEW, -1);
	}

	@Override
	protected void initializeImageRegistry() {
		// Nothing to load
	}

	@Override
	public String resString(final String key, final Object... params) {
		try {
			return String.format(getResourceString(key), params);
		} catch (final Exception e) {
			return getResourceString(key);
		}
	}

	public void setMessageLevelFilter(final int level) {
		getPreferenceStore().setValue(PREF_MESSAGE_LEVELVIEW, level);
	}

	/**
	 * Modifie le niveau de détaild es messages à l'affichage.
	 *
	 * @param value
	 */
	public void setMessagesLevel(final int value) {
		getPreferenceStore().setValue(PREF_MESSAGES_LEVEL, value);
	}
}
