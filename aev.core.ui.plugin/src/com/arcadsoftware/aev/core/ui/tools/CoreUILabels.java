package com.arcadsoftware.aev.core.ui.tools;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;

import com.arcadsoftware.aev.core.tools.DefaultLabelsHelper;
import com.arcadsoftware.aev.core.tools.IHelperRessource;

public class CoreUILabels {
	
	private static CoreUILabels instance;
    private IHelperRessource helper = null;            
    
    public CoreUILabels() {
        super();
    }
    
    public static CoreUILabels getInstance() {
        if (instance==null)
            instance = new CoreUILabels();
        return instance;
    }
    
    public void setHelper(IHelperRessource helper) {
        this.helper = helper;        
    }
    
    public IHelperRessource getHelper() {
        if (helper==null)
            helper = new DefaultLabelsHelper();
        return helper;
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
