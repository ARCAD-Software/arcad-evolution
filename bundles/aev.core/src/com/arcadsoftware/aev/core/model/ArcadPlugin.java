package com.arcadsoftware.aev.core.model;

import java.io.IOException;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Plugin;

/**
 * @author MD
 * 
 */
public abstract class ArcadPlugin extends Plugin implements IArcadPlugin {
	public ArcadPlugin() {
		super();
	}

	public String getPluginPath() throws IOException {
		IPath p = new Path(FileLocator.resolve(getBundle().getEntry("/")).getPath()); //$NON-NLS-1$
		return p.toOSString();
	}

	public String getVersion() {
		return (String) getBundle().getHeaders().get(org.osgi.framework.Constants.BUNDLE_VERSION);
	}	
}