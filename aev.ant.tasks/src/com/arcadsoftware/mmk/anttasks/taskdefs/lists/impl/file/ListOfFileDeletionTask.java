package com.arcadsoftware.mmk.anttasks.taskdefs.lists.impl.file;

import java.io.File;

import org.apache.tools.ant.BuildException;



import com.arcadsoftware.mmk.anttasks.taskdefs.lists.AbstractXmlFileListFromResourcesTask;
import com.arcadsoftware.mmk.lists.impl.lists.FileList;
import com.arcadsoftware.mmk.lists.metadata.StoreItem;

public class ListOfFileDeletionTask extends AbstractXmlFileListFromResourcesTask {

	private String fromListFileName=null;
	private String deleteQuery=null;	
	
	
	/* (non-Javadoc)
	 * @see com.arcadsoftware.mmk.anttasks.taskdefs.lists.AbstractFileListFromResourcesTask#validateAttributes()
	 */
	@Override
	public void validateAttributes() {
		super.validateAttributes();
		if ((getFilename()==null) || (getFilename().equals(""))) {		
			throw new BuildException("Filename attribute must be set!");
		}		
		//Le controle de saisie est r‚alis‚ dans le super
		File f = new File(getFilename());
		if (!f.exists()) {
			throw new BuildException("File not found!");
		}				
	
	}	
	
	@Override
	public int process() {
		//Suppression … partir d'une liste
		int count1 = 0;
		if ((fromListFileName!=null) && !fromListFileName.equals("")){
			//D‚claration de la liste        
			FileList fromlist = (FileList)list.cloneList();
			fromlist.setXmlFileName(fromListFileName);
			count1 = list.removeItems(fromlist);			
		}
		//Supression … partir d'une requete
		int count2 = 0;
		if ((deleteQuery!=null) && !deleteQuery.equals("")){
			count2 = list.removeItems(deleteQuery);			
		}		
		int count3 = 0;
		//Supression … partir d'‚l‚ment ressources
		StoreItem[] items = new StoreItem[files.size()];
		for (int i=0;i<files.size();i++) {
			String filename = files.get(i);
			items[i] = list.toStoreItem(new File(filename));			
		}
		count3 =list.removeItems(items);
		return count1+count2+count3;
	}




	/**
	 * @param deleteQuery the deleteQuery to set
	 */
	public void setDeleteQuery(String deleteQuery) {
		this.deleteQuery = deleteQuery;
	}




}
