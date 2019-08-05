/*
 * Created on Sep 21, 2006
 */
package com.arcadsoftware.aev.core.tools;

import com.arcadsoftware.aev.core.EvolutionCorePlugin;

/**
 * @author MD
 */
public class ArcadCoreUtilsHelper implements IHelper {
	public ArcadCoreUtilsHelper() {
        super();
    }

    @Override
	public void beginAction(){
    	//Do nothing
    }
    
    @Override
	public void endAction(){
		//Do nothing
	}	

    @Override
	public String getBasedLocation(){
	  return EvolutionCorePlugin.getDefault().getStateLocation().toString();	    
	}

    @Override
	public String getCompliantFileName() {
		return EvolutionCorePlugin.getCompliantFileName();
	}
}
