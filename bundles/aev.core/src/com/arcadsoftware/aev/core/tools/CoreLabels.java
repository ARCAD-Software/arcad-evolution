/*
 * Created on Sep 12, 2006
 */
package com.arcadsoftware.aev.core.tools;

/**
 * @author MD
 */
public class CoreLabels {
	private static CoreLabels instance;

	public static CoreLabels getInstance() {
		if (instance == null) {
			instance = new CoreLabels();
		}
		return instance;
	}

	public static String resString(final String res, final Object... params) {
		return getInstance().getHelper().resString(res, params);
	}

	private IHelperLabel helperLabel = null;

	public CoreLabels() {
		super();
	}

	public IHelperLabel getHelper() {
		if (helperLabel == null) {
			helperLabel = new DefaultLabelsHelper();
		}
		return helperLabel;
	}

	public void setLabelHelper(final IHelperLabel helperLabel) {
		this.helperLabel = helperLabel;
	}
}
