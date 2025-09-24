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

/**
 * The main plugin class to be used in the desktop.
 */
public class EvolutionCorePlugin extends ArcadPlugin implements IHelperLabel {

	public static final String COMPLIANT_FILENAME = "compliant.xml"; //$NON-NLS-1$

	// The shared instance.
	private static EvolutionCorePlugin plugin;

	public static String getCompliantFileName() {
		try {
			return getDefault().getPluginPath() + File.separatorChar + COMPLIANT_FILENAME;
		} catch (final IOException e) {
			return COMPLIANT_FILENAME;
		}
	}

	/**
	 * Returns the shared instance.
	 */
	public static EvolutionCorePlugin getDefault() {
		return plugin;
	}

	/**
	 * Returns the string from the plugin's resource bundle, or 'key' if not found.
	 */
	public static String getResourceString(final String key) {
		final ResourceBundle bundle = EvolutionCorePlugin.getDefault().getResourceBundle();
		try {
			return bundle.getString(key);
		} catch (final MissingResourceException e) {
			return key;
		}
	}

	/**
	 * Returns the workspace instance.
	 */
	public static IWorkspace getWorkspace() {
		return ResourcesPlugin.getWorkspace();
	}

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
		} catch (final MissingResourceException x) {
			resourceBundle = null;
		}
	}

	/**
	 * Returns the plugin's resource bundle,
	 */
	public ResourceBundle getResourceBundle() {
		return resourceBundle;
	}

	@Override
	public String resString(final String key, final Object... params) {
		try {
			return String.format(getResourceString(key), params);
		} catch (final Exception e) {
			return getResourceString(key);
		}
	}

	@Override
	public void start(final BundleContext context) throws Exception {
		super.start(context);
	}
}
