package com.arcadsoftware.aev.core.ui.rcp;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import com.arcadsoftware.aev.core.ui.EvolutionCoreUIPlugin;
import com.arcadsoftware.aev.core.ui.dialogs.DialogConstantProvider;
import com.arcadsoftware.aev.core.ui.rcp.dialogs.RCPDialogConstantFiller;
import com.arcadsoftware.aev.core.ui.rcp.tools.FileManagerProvider;


/**
 * The activator class controls the plug-in life cycle
 */
public class AEVRCPPlugin extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "com.arcadsoftware.aev.core.ui.rcp"; //$NON-NLS-1$

	// The shared instance
	private static AEVRCPPlugin plugin;
	
	/**
	 * The constructor
	 */
	public AEVRCPPlugin() {
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;		
		DialogConstantProvider.setFiller(new RCPDialogConstantFiller());
		EvolutionCoreUIPlugin.getDefault().setFileManagerProvider(new FileManagerProvider());
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static AEVRCPPlugin getDefault() {
		return plugin;
	}

}
