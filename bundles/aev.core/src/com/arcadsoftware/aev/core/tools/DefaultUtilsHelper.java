/*
 * Created on Sep 21, 2006
 */
package com.arcadsoftware.aev.core.tools;

import java.io.File;

/**
 * @author MD
 */
public class DefaultUtilsHelper implements IHelper, IHelperLabel {
	private static String COMPLIANT_FILENAME = "compliant.xml"; //$NON-NLS-1$
	private final DefaultLabelsHelper labelsHelper;

	public DefaultUtilsHelper() {
		super();
		labelsHelper = new DefaultLabelsHelper();
	}

	@Override
	public void beginAction() {
		// Do nothing
	}

	@Override
	public void endAction() {
		// Do nothing
	}

	@Override
	public String getBasedLocation() {
		return System.getProperty("user.dir"); //$NON-NLS-1$
	}

	@Override
	public String getCompliantFileName() {
		return getBasedLocation() + File.separator + COMPLIANT_FILENAME;
	}

	public boolean isLib250() {
		return true;
	}

	@Override
	public String resString(final String res, final Object... params) {
		return labelsHelper.resString(res, params);
	}

}
