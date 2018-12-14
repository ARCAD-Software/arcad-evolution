/*
 * Created on Sep 21, 2006
 */
package com.arcadsoftware.aev.core.tools;

import java.io.File;

/**
 * @author MD
 */
public class DefaultUtilsHelper implements IHelper {
	private static String LIBLIST_FILENAME = "liblist.xml"; //$NON-NLS-1$
	private static String COMPLIANT_FILENAME = "compliant.xml"; //$NON-NLS-1$

	/**
     * 
     */
	public DefaultUtilsHelper() {
		super();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.arcadsoftware.aev.core.IHelper#resString(java.lang.String)
	 */
	public String resString(String res) {
		return res;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.arcadsoftware.aev.core.IHelper#beginAction()
	 */
	public void beginAction() {
		// Do nothing
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.arcadsoftware.aev.core.IHelper#endAction()
	 */
	public void endAction() {
		// Do nothing
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.arcadsoftware.aev.core.IHelper#getLibListFileName()
	 */
	public String getLibListFileName() {
		return getBasedLocation() + File.separator + LIBLIST_FILENAME;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.arcadsoftware.aev.core.IHelper#getCompliantFileName()
	 */
	public String getCompliantFileName() {
		return getBasedLocation() + File.separator + COMPLIANT_FILENAME;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.arcadsoftware.aev.core.IHelper#getBasedLocation()
	 */
	public String getBasedLocation() {
		return System.getProperty("user.dir"); //$NON-NLS-1$
	}

	public boolean isLib250() {
		return true;
	}
}
