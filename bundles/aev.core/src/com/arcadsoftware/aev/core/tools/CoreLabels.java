/*
 * Created on Sep 12, 2006
 */
package com.arcadsoftware.aev.core.tools;

/**
 * @author MD
 */
public class CoreLabels {
    private static CoreLabels instance;
    private IHelperLabel helperLabel = null;          

    public CoreLabels() {
        super();
    }
    
    public static CoreLabels getInstance() {
        if (instance==null)
            instance = new CoreLabels();
        return instance;
    }
    
    public void setLabelHelper(IHelperLabel helperLabel) {
    	this.helperLabel = helperLabel;        
    }
    
    public IHelperLabel getHelper() {
        if (helperLabel == null)
        	helperLabel = new DefaultLabelsHelper();
        return helperLabel;
    }
    
    public static String resString(String res, Object...params) {
        return getInstance().getHelper().resString(res, params);
    }
}
