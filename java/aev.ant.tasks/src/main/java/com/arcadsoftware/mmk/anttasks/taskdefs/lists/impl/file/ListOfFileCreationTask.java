package com.arcadsoftware.mmk.anttasks.taskdefs.lists.impl.file;

import java.io.File;


import org.apache.tools.ant.BuildException;
import com.arcadsoftware.mmk.anttasks.taskdefs.lists.AbstractListFillTask;
import com.arcadsoftware.mmk.lists.impl.lists.FileList;

public class ListOfFileCreationTask extends AbstractListFillTask {

    private boolean replaceFileIfExists = false;
    private boolean checkDuplication = false;
    
  
    private String description = null;
    private String comment = null;    
    
    
	/* (non-Javadoc)
	 * @see com.arcadsoftware.mmk.anttasks.taskdefs.AbstractArcadAntTask#validateAttributes()
	 */
	@Override
	public void validateAttributes() {
		super.validateAttributes();
		//Le controle de saisie est réaliser dans le super
		File f = new File(getFilename());
		if (f.exists()) {
			//Si l'utilisateur n'a pas demandé le remplacement
			//on envoit un exception
			if (!replaceFileIfExists)
				throw new BuildException("File already exists!");
			else {
				//Sinon on supprime le fichier
				if (!f.delete()) 
					throw new BuildException("File Replacement failed!");
			}
		}				
	}
	
	@Override
	public int populate() {
		//Traitement de l'ajout à partir d'une liste
		int count1 =0;
		if ((fromListFileName!=null) && !fromListFileName.equals("")){
			//Déclaration de la liste        
			FileList fromlist = (FileList)list.cloneList();
			fromlist.setXmlFileName(fromListFileName);			
			if (checkDuplication) {
				list.addItems(fromlist,checkIfExists,replaceIfExists);
			} else {
				fromlist.duplicate(list);
				count1 = list.count("");
			}
		}			
		int count2=0;
		//Si aucun élement n'a été ajouté par la duplication
		//on execute un populate simple
		if (!checkDuplication) 
			count2=list.populate();
		//sinon on ajoute les nouveaux éléments
		else
			count2 = list.addItems(filler,checkIfExists,replaceIfExists);
		
		updateHeaderInfo(description,comment);
		
		return count1+count2;
	}  
	
	/**
	 * @param replaceFileIfExists the replaceFileIfExists to set
	 */
	public void setReplaceFileIfExists(boolean replaceFileIfExists) {
		this.replaceFileIfExists = replaceFileIfExists;
	}
	/**
	 * @param checkDuplication the checkDuplication to set
	 */
	public void setCheckDuplication(boolean checkDuplication) {
		this.checkDuplication = checkDuplication;
	}
	
	/**
	 * @param comment the comment to set
	 */
	public void setComment(String comment) {
		this.comment = comment;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}	

}
