package com.arcadsoftware.aev.core.model;

import java.io.IOException;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Plugin;

/**
 * @author MD
 */
public abstract class ArcadPlugin extends Plugin implements IArcadPlugin {
	public ArcadPlugin() {
		super();
	}

	@Override
	public String getPluginPath() throws IOException {
		final IPath p = new Path(FileLocator.resolve(getBundle().getEntry("/")).getPath()); //$NON-NLS-1$
		return p.toOSString();
	}

	@Override
	public String getVersion() {
		return getBundle().getHeaders().get(org.osgi.framework.Constants.BUNDLE_VERSION);
	}
}