/*
 * Created on Sep 12, 2006
 */
package com.arcadsoftware.aev.core.tools;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;



/**
 * @author MD
 */
public class CoreLabels {
    private static CoreLabels instance;
    private IHelperRessource _helper = null;          

    public CoreLabels() {
        super();
    }
    
    public static CoreLabels getInstance() {
        if (instance==null)
            instance = new CoreLabels();
        return instance;
    }
    
    public void setHelper(IHelperRessource helper) {
        _helper = helper;        
    }
    
    public IHelperRessource getHelper() {
        if (_helper==null)
            _helper = new DefaultLabelsHelper();
        return _helper;
    }
    
    public static String resString(String res) {
        return getInstance().getHelper().resString(res);
    }
    
    public static ImageDescriptor getImageDescriptor(String key){
    	return getInstance().getHelper().getImageDescriptor(key);
    }
    
    public static Image getImage(String key) {
		return getInstance().getHelper().getImage(key);
	}
    
    public static Image getCompositeImage(String key,String decoKey){
    	return getInstance().getHelper().getCompositeImage(key, decoKey);
    }
    
    public static ImageDescriptor getCompositeImageDescriptor(String key,String decoKey){
    	return getInstance().getHelper().getCompositeImageDescriptor(key, decoKey);
    }
}
