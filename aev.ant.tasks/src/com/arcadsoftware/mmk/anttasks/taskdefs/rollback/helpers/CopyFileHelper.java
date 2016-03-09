package com.arcadsoftware.mmk.anttasks.taskdefs.rollback.helpers;



import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.util.FileUtils;
import org.dom4j.Element;


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
			//Cr�ation d'une copie de secours dans le r�pertoire de 
			//sauvegarde
			try {
				//Traitement du mode overwrite.
				//Si le fichier de destination existe et qu'il est
				//plus r�cent que le fichier source, on ne fait rien
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
			} catch (IOException e) {
				throw new BuildException("Unable to create backup file!",e ,task.getTask().getLocation());
			}			
		} else {
			//Si le fichier de destination n'existe pas, on l'ajoute
			//dans la collection traitant les nouveaux fichiers.
			createdfiles.add(fileToBackup);
		}
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
		} else if (status.equals(CREATED)){
			//Si le fichier � restaurer existe mais qu'aucun fichier correspondant
			//n'existe dans le r�pertoire de backup, on supprime le fichier
			//de destination.
			//TODO [RB] Possibilit� de param�trer cela
			File f = new File(fileToRestore);			
			if (f.exists())
				f.delete();
		}
	}		
	
	@Override
	public Element createRollbackData(Element e) {		
		Element action = super.createRollbackData(e);
		//Traitement des fichiers modifi�s
		for (int i=0;i<updatedfiles.size();i++) {
			Element copyAction = action.addElement("backup");
			copyAction.addAttribute("src",updatedfiles.get(i));
			String path = 
				FileUtils.getFileUtils().removeLeadingPath(					  
				  	   new File(getBackupRoot()),
				  	   new File(dataDirectory));
			copyAction.addAttribute("datadir",path);			
			copyAction.addAttribute("status",UPDATED);			
		}			
		//Traitement des fichiers ajout�s	
		for (int i=0;i<createdfiles.size();i++) {
			Element copyAction = action.addElement("backup");
			copyAction.addAttribute("src",createdfiles.get(i));
			copyAction.addAttribute("datadir","");			
			copyAction.addAttribute("status",CREATED);
			
		}			
		return action;
	}		
	
	public boolean rollback(ArcadRollbackTask rollbackTask,Element e) {
        for ( Iterator i = e.elementIterator("backup"); i.hasNext(); ) {
            Element backupAction = (Element) i.next();
            String fileName = backupAction.attributeValue("src");
            String status = backupAction.attributeValue("status");
            String datadir = backupAction.attributeValue("datadir");            
            if (fileName!=null) {            	
	            restoreFile(datadir,fileName,status);
            }
            	
        }		
		return true;	
	}
	
	
}