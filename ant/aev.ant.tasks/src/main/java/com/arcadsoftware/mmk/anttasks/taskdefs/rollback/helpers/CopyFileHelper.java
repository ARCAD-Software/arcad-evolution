package com.arcadsoftware.mmk.anttasks.taskdefs.rollback.helpers;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.util.FileUtils;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.arcadsoftware.ae.core.utils.XMLUtils;
import com.arcadsoftware.mmk.anttasks.taskdefs.rollback.IRollbackableTask;
import com.arcadsoftware.mmk.anttasks.taskdefs.rollback.impl.ArcadRollbackTask;

public class CopyFileHelper extends AbstractRollbackableTaskHelper {
	 
	private static final String UPDATED = "updated"; 
	private static final String CREATED = "created";	
	
	protected ArrayList<String> updatedfiles = new ArrayList<String>();
	protected ArrayList<String> createdfiles = new ArrayList<String>();
	
	public CopyFileHelper(IRollbackableTask task) {
		super(task);
	}
	
	public CopyFileHelper() {
		super();
	}	
	
	public void backupFile(File f) {
		backupFile(null,f.getAbsolutePath(),true);		
	}
	
	public void backupFile(String source,String fileToBackup,boolean overwrite) {
		File fileToBackupFile = new File(fileToBackup);
		if (fileToBackupFile.exists()) {
			//Creation d'une copie de secours dans le répertoire de 
			//sauvegarde
			try {
				//Traitement du mode overwrite.
				//Si le fichier de destination existe et qu'il est
				//plus recent que le fichier source, on ne fait rien
				if (source!=null) {					
					if (!overwrite) {
						File sourceFile = new File(source);
			            long slm = sourceFile.lastModified(); 
			            if (slm != 0 && fileToBackupFile.lastModified() > slm) {
			                return;
			            }
					}
				}				
				String[] pathes = FileUtils.getFileUtils().dissect(fileToBackup);
				String backupFile = dataDirectory+File.separator+ pathes[1];
				File f = new File(backupFile);			
				if (!f.exists())
					FileUtils.getFileUtils().createNewFile(f,true);
				FileUtils.getFileUtils().copyFile(fileToBackup,backupFile,null,true);
				
				//Enregistrement du fichier dans les ifnormations de rollback
				updatedfiles.add(fileToBackup);
			}
			catch (IOException e) {
				throw new BuildException("Unable to create backup file!",e ,task.getTask().getLocation());
			}			
		}
		else {
			//Si le répertoire n'existe pas, il sera créé lors de la copie, donc il faudra l'effacer
			//et tout son contenu avec
			if(!pathWillBeCreated(fileToBackupFile)){
				//Si le fichier de destination n'existe pas, on l'ajoute
				//dans la collection traitant les nouveaux fichiers.
				createdfiles.add(fileToBackup);
			}
		}
	}
	
	protected boolean pathWillBeCreated(File aFile){
		boolean created = false;
		
		File parentDirectory = aFile.getParentFile();
		if(parentDirectory != null && !parentDirectory.exists()){
			created = true;
			if(!pathWillBeCreated(parentDirectory) && !createdfiles.contains(parentDirectory.getAbsolutePath())){
				createdfiles.add(parentDirectory.getAbsolutePath());				
			}
		}
		
		return created;
	}
	
	public void restoreFile(String dataDir, String fileToRestore,String status) {
		String[] pathes = FileUtils.getFileUtils().dissect(fileToRestore);
		String backedupFile = getBackupRoot()+File.separator+dataDir+File.separator+ pathes[1];
		System.out.println("restoreFile::backedupFile : "+backedupFile);
		System.out.println("restoreFile::getBackupRoot() : "+getBackupRoot());		
		if (status.equals(UPDATED)){
			System.out.println("Status : Updated");
			if (new File(backedupFile).exists()) {
				System.out.println("-> BackedUp File exists");
				try {
					System.out.println("-> Creation of restored File :" + fileToRestore);
					File f = new File(fileToRestore);			
					if (!f.exists()){
						System.out.println("-> Creation process");
						FileUtils.getFileUtils().createNewFile(f,true);
					}
					System.out.println("-> restoration");
					FileUtils.getFileUtils().copyFile(backedupFile,fileToRestore,null,true);
				} catch (IOException e) {
					System.out.println("***EXCEPTION *** : "+e.getMessage());
					throw new BuildException("Unable to restore file!",e ,task.getTask().getLocation());
				}			
			}
		}
		else if (status.equals(CREATED)){
			System.out.println("-> Delete file " + fileToRestore);
			
			//Si le fichier a restaurer existe mais qu'aucun fichier correspondant
			//n'existe dans le repertoire de backup, on supprime le fichier de destination.
			File file = new File(fileToRestore);			
			if (file.exists()){
				try{
					org.apache.commons.io.FileUtils.forceDelete(file);
				}
				catch (IOException e) {
					System.out.println("***EXCEPTION *** : " + e.getMessage());
					throw new BuildException("Unable to delete file!",e ,task.getTask().getLocation());
				}
			}
		}
	}		
	
	@Override
	public Element createRollbackData(Element e) {		
		Element action = super.createRollbackData(e);
		//Traitement des fichiers modifiés
		for (int i=0;i<updatedfiles.size();i++) {
			final Element copyAction = XMLUtils.addElement(document, action, "backup");
			copyAction.setAttribute("src",updatedfiles.get(i));
			String path = 
				FileUtils.getFileUtils().removeLeadingPath(					  
				  	   new File(getBackupRoot()),
				  	   new File(dataDirectory));
			copyAction.setAttribute("datadir",path);			
			copyAction.setAttribute("status",UPDATED);			
		}			
		//Traitement des fichiers ajoutés	
		for (int i=0;i<createdfiles.size();i++) {
			Element copyAction = XMLUtils.addElement(document, action, "backup");
			copyAction.setAttribute("src",createdfiles.get(i));
			copyAction.setAttribute("datadir","");			
			copyAction.setAttribute("status",CREATED);			
		}			
		return action;
	}		

	public boolean rollback(ArcadRollbackTask rollbackTask,Element e) {
		final NodeList backupActions = e.getElementsByTagName("backup");
        for ( int i = 0; i < backupActions.getLength(); i++ ) {
            final Element backupAction = (Element) backupActions.item(i);
            final String fileName = backupAction.getAttribute("src");
            final String status = backupAction.getAttribute("status");
            final String datadir = backupAction.getAttribute("datadir");            
            if (fileName != null) {            	
	            restoreFile(datadir,fileName,status);
            }
            	
        }		
		return true;	
	}	
}
