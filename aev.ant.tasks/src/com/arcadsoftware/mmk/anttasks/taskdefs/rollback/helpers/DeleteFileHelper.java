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

public class DeleteFileHelper extends AbstractRollbackableTaskHelper {

	private static final String TYPE_FILE = "file"; 
	private static final String TYPE_DIR = "dir";	
	
	protected ArrayList<String> deletedfiles = new ArrayList<String>();
	protected ArrayList<String> deletedDir = new ArrayList<String>();	
	
	public DeleteFileHelper(IRollbackableTask task) {
		super(task);
	}
	
	public DeleteFileHelper() {
		super();
	}	
	
	private void addDirectory(String directory) {
		//Si le directory n'est pas d‚j… r‚f‚renc‚, on l'ajoute
		if (deletedDir.indexOf(directory)==-1) {
			deletedDir.add(directory);
		}			
	}
	
	public void backup(File  fileToDelete) {
		if (fileToDelete.isFile()) {
			//Cr‚ation d'une copie de secours dans le r‚pertoire de 
			//sauvegarde
			try {				
				String[] pathes = FileUtils.getFileUtils().dissect(fileToDelete.getAbsolutePath());
				String backupFileName = dataDirectory+File.separator+ pathes[1];
				File backupFile = new File(backupFileName);			
				if (!backupFile.exists())
					FileUtils.getFileUtils().createNewFile(backupFile,true);
				FileUtils.getFileUtils().copyFile(fileToDelete,backupFile,null,true);
				//Enregistrement du fichier dans les ifnormations de rollback
				deletedfiles.add(fileToDelete.getAbsolutePath());
				addDirectory(fileToDelete.getParent());
			} catch (IOException e) {
				throw new BuildException("Unable to create backup file!",e ,task.getTask().getLocation());
			}				
		} else if (fileToDelete.isDirectory()) {
			addDirectory(fileToDelete.getAbsolutePath());
		}
	}		
	
	public void restore(String dataDir, String fileNameToRestore,String type) {		
		if (type.equals(TYPE_FILE)) {
			String[] pathes = FileUtils.getFileUtils().dissect(fileNameToRestore);
			String backedupFileName = getBackupRoot()+File.separator+dataDir+File.separator+ pathes[1];
			File backedupFile = new File(backedupFileName);
			if (backedupFile.exists()) {
				try {
					File fileToRestore = new File(fileNameToRestore);			
					if (!fileToRestore.exists())
						FileUtils.getFileUtils().createNewFile(fileToRestore,true);
					FileUtils.getFileUtils().copyFile(backedupFile,fileToRestore,null,true);
				} catch (IOException e) {
					throw new BuildException("Unable to restore file!",e ,task.getTask().getLocation());
				}			
			}			
		} else if (type.equals(TYPE_DIR)) {
			File directoryToCreate = new File(fileNameToRestore);
			directoryToCreate.mkdirs();			
		}
	}		
	
	private void insertDeleteElement(Element action,String fileName,String type) {		
		Element deleteAction = action.addElement("backup");
		deleteAction.addAttribute("src",fileName);
		String path = 
			FileUtils.getFileUtils().removeLeadingPath(					  
			  	   new File(getBackupRoot()),
			  	   new File(dataDirectory));
		deleteAction.addAttribute("datadir",path);						
		deleteAction.addAttribute("type",type);			
	}
	
	
	@Override
	public Element createRollbackData(Element e) {		
		Element action = super.createRollbackData(e);
		//Traitement des fichiers supprim‚s
		for (int i=0;i<deletedfiles.size();i++) {
			insertDeleteElement(action,deletedfiles.get(i),TYPE_FILE);
		}			
		//Traitement des directory supprim‚s
		for (int i=0;i<deletedDir.size();i++) {
			insertDeleteElement(action,deletedDir.get(i),TYPE_DIR);			
		}			
		return action;
	}		
	
	public boolean rollback(ArcadRollbackTask rollbackTask,Element e) {
        for ( Iterator i = e.elementIterator("backup"); i.hasNext(); ) {
            Element backupAction = (Element) i.next();
            String fileName = backupAction.attributeValue("src");
            String datadir = backupAction.attributeValue("datadir");
            String type = backupAction.attributeValue("type");                        
            if (fileName!=null) {            	
	            restore(datadir,fileName,type);
            } else {
            	throw new BuildException("Invalid Rollback Data!",task.getTask().getLocation());            	
            }
            	
        }		
		return true;	
	}
}
