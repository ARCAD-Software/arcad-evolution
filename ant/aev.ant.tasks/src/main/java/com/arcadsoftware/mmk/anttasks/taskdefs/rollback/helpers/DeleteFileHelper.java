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
		if (deletedDir.indexOf(directory)==-1) {
			deletedDir.add(directory);
		}			
	}
	
	public void backup(File  fileToDelete) {
		if (fileToDelete.isFile()) {
			try {				
				String[] pathes = FileUtils.getFileUtils().dissect(fileToDelete.getAbsolutePath());
				String backupFileName = dataDirectory+File.separator+ pathes[1];
				File backupFile = new File(backupFileName);			
				if (!backupFile.exists())
					FileUtils.getFileUtils().createNewFile(backupFile,true);
				FileUtils.getFileUtils().copyFile(fileToDelete,backupFile,null,true);

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
		Element deleteAction = XMLUtils.addElement(document, action, "backup");
		deleteAction.setAttribute("src",fileName);
		String path = 
			FileUtils.getFileUtils().removeLeadingPath(					  
			  	   new File(getBackupRoot()),
			  	   new File(dataDirectory));
		deleteAction.setAttribute("datadir",path);						
		deleteAction.setAttribute("type",type);			
	}
	
	
	@Override
	public Element createRollbackData(Element e) {		
		Element action = super.createRollbackData(e);
		//Traitement des fichiers supprimés
		for (int i=0;i<deletedfiles.size();i++) {
			insertDeleteElement(action,deletedfiles.get(i),TYPE_FILE);
		}			
		//Traitement des directory supprimés
		for (int i=0;i<deletedDir.size();i++) {
			insertDeleteElement(action,deletedDir.get(i),TYPE_DIR);			
		}			
		return action;
	}		
	
	public boolean rollback(ArcadRollbackTask rollbackTask,Element e) {
		final NodeList backupActions = e.getElementsByTagName("backup");
        for ( int i = 0; i < backupActions.getLength(); i++ ) {
            final Element backupAction = (Element) backupActions.item(i);
            final String fileName = backupAction.getAttribute("src");
            final String datadir = backupAction.getAttribute("datadir");
            final String type = backupAction.getAttribute("type");                        
            if (fileName!=null) {            	
	            restore(datadir,fileName,type);
            } else {
            	throw new BuildException("Invalid Rollback Data!",task.getTask().getLocation());            	
            }
            	
        }		
		return true;	
	}
}
