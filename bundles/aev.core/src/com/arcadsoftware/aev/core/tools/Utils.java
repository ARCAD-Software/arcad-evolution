/*
 * Created on Sep 21, 2006
 */
package com.arcadsoftware.aev.core.tools;

/**
 * @author MD
 */
public class Utils {

    private static Utils instance;
    private IHelper _helper = null;        
    
	//private static Cursor cursor;    
    
    /**
     * 
     */
    public Utils() {
        super();
    }
    
    public static Utils getInstance() {
        if (instance==null){
            instance = new Utils();
        }
        return instance;
    }
    
    public void setHelper(IHelper helper) {
        _helper = helper;        
    }
    
    public IHelper getHelper() {
        if (_helper==null) {
            _helper = new DefaultUtilsHelper();
        }
        return _helper;
    }
    
	public static void beginAction(){
	    getInstance().getHelper().beginAction();
	}
	
	public static void endAction(){
	    getInstance().getHelper().endAction();
	}	
 
	public static String getCompliantFileName(){
		return getInstance().getHelper().getCompliantFileName();
	}
	
	public static String getBasedLocation(){
	    return getInstance().getHelper().getBasedLocation();	    
	}
}
