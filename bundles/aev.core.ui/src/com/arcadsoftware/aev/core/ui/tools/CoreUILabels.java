package com.arcadsoftware.aev.core.ui.tools;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;

import com.arcadsoftware.aev.core.tools.CoreLabels;
import com.arcadsoftware.aev.core.ui.model.IHelperImage;

public class CoreUILabels extends CoreLabels{
	
	private static CoreUILabels instance;
    
	private IHelperImage helperImage;
	
    public CoreUILabels() {
        super();
    }
    
    public static CoreUILabels getInstance() {
        if (instance == null)
            instance = new CoreUILabels();
        return instance;
    }
    
    public void setImageHelper(IHelperImage helperImage) {
        this.helperImage = helperImage;        
    }
    
    public IHelperImage getImageHelper() {
		return helperImage;
	}
    
    public static String resString(String res) {
        return getInstance().getHelper().resString(res); 
    }
    
    public static ImageDescriptor getImageDescriptor(String key){
    	return getInstance().getImageHelper().getImageDescriptor(key);
    }
    
    public static Image getImage(String key) {
		return getInstance().getImageHelper().getImage(key);
	}
    
    public static Image getCompositeImage(String key,String decoKey){
    	return getInstance().getImageHelper().getCompositeImage(key, decoKey);
    }
    
    public static ImageDescriptor getCompositeImageDescriptor(String key,String decoKey){
    	return getInstance().getImageHelper().getCompositeImageDescriptor(key, decoKey);
    }
      
}
