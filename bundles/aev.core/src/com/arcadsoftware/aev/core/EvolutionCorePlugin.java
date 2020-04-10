package com.arcadsoftware.aev.core;

import java.io.File;
import java.io.IOException;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.osgi.framework.BundleContext;

import com.arcadsoftware.aev.core.model.ArcadPlugin;
import com.arcadsoftware.aev.core.tools.ArcadCoreUtilsHelper;
import com.arcadsoftware.aev.core.tools.CoreLabels;
import com.arcadsoftware.aev.core.tools.IHelperLabel;
import com.arcadsoftware.aev.core.tools.Utils;
s
/**
 * The main plugin class to be used in the desktop.
 */
public class EvolutionCorePlugin extends ArcadPlugin implements IHelperLabel {

	public static final String COMPLIANT_FILENAME = "compliant.xml"; //$NON-NLS-1$

	// The shared instance.
	private static EvolutionCorePlugin plugin;
	// Resource bundle.
	private ResourceBundle resourceBundle;

	/**
	 * The constructor.
	 */
	public EvolutionCorePlugin() {
		super();
		plugin = this;
		Utils.getInstance().setHelper(new ArcadCoreUtilsHelper());
		CoreLabels.getInstance().setLabelHelper(this);
		try {
			resourceBundle = ResourceBundle.getBundle("com.arcadsoftware.aev.core.EvolutionCorePluginResources");//$NON-NLS-1$
		} catch (MissingResourceException x) {
			resourceBundle = null;
		}
	}

	/**
	 * Returns the shared instance.
	 */
	public static EvolutionCorePlugin getDefault() {
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
		ResourceBundle bundle = EvolutionCorePlugin.getDefault().getResourceBundle();
		try {
			return bundle.getString(key);
		} catch (MissingResourceException e) {
			return key;
		}
	}

	/**
	 * Returns the plugin's resource bundle,
	 */
	public ResourceBundle getResourceBundle() {
		return resourceBundle;
	}

	public static String getCompliantFileName() {
		try {
			return getDefault().getPluginPath() + File.separatorChar + COMPLIANT_FILENAME;
		} catch (IOException e) {
			return COMPLIANT_FILENAME;
		}
	}

	@Override
	public String resString(String key, Object...params) {
		try {
			return String.format(getResourceString(key), params);
		}
		catch(Exception e) {
			return getResourceString(key);
		}
	}

	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
	}
}
