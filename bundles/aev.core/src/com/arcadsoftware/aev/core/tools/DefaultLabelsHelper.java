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
	public String resString(String res, Object...params) {
        try {
        	return String.format(res, params);
        }
        catch(Exception e) {
        	e.printStackTrace();
        	return res;
        }
    }
}
