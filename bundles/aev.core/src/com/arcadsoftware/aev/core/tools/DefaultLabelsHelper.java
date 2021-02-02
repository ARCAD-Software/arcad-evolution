/*
 * Created on Sep 21, 2006
 */
package com.arcadsoftware.aev.core.tools;

/**
 * @author MD
 */
public class DefaultLabelsHelper implements IHelperLabel {

	public DefaultLabelsHelper() {
		super();
	}

	@Override
	public String resString(final String res, final Object... params) {
		try {
			return String.format(res, params);
		} catch (final Exception e) {
			return res;
		}
	}
}
