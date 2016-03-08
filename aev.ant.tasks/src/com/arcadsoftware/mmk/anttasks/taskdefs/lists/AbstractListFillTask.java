package com.arcadsoftware.mmk.anttasks.taskdefs.lists;

import java.io.File;
import java.util.ArrayList;
import com.arcadsoftware.mmk.lists.AbstractList;
import com.arcadsoftware.mmk.lists.managers.AbstractFiller;
import com.arcadsoftware.mmk.lists.metadata.StoreItem;

public abstract class AbstractListFillTask extends AbstractXmlFileListFromResourcesTask {
	protected MapFiller filler;	
	protected boolean checkIfExists = false; 
	protected boolean replaceIfExists = false; 


    /**
     * Classe de remplissage … partir des ‚l‚ment r‚solus
     */
	public class MapFiller extends AbstractFiller{
		private ArrayList<String> files;
		
		public MapFiller(AbstractList list,ArrayList<String> files) {
			super(list);
			this.files = files;
		}
		@Override
		public int fill() {
			int count = 0;
	    	for(int i=0;i<files.size();i++) {
	    		String toFile = files.get(i);
	    		int  j = processFile(this,toFile);
	    		count +=j;
	    	}
	    	return count;    	
		}		
	}       
    
	public int processFile(AbstractFiller filler,String file) {
		StoreItem storeItem = filler.getList().toStoreItem(new File(file));
		return filler.saveItem(storeItem);		
	}
    	
	/* (non-Javadoc)
	 * @see com.arcadsoftware.mmk.anttasks.taskdefs.lists.AbstractFileListFromResourcesTask#process()
	 */
	@Override
	public int process() {
    	//Remplissage de la liste
    	filler = new MapFiller(list,files);
    	list.setFiller(filler);			
		return  populate();


	}

	public abstract int populate();
		
	/**
	 * @param checkIfExists the checkIfExists to set
	 */
	public void setCheckIfExists(boolean checkIfExists) {
		this.checkIfExists = checkIfExists;
	}

	/**
	 * @param replaceIfExists the replaceIfExists to set
	 */
	public void setReplaceIfExists(boolean replaceIfExists) {
		this.replaceIfExists = replaceIfExists;
	}	


	
	
	
	
	
}
