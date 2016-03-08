package com.arcadsoftware.mmk.anttasks.taskdefs.rollback.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.util.FileUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;


import com.arcadsoftware.mmk.anttasks.taskdefs.AbstractArcadAntTask;
import com.arcadsoftware.mmk.anttasks.taskdefs.rollback.helpers.CopyFileHelper;
import com.arcadsoftware.mmk.anttasks.taskdefs.rollback.helpers.DeleteFileHelper;
import com.arcadsoftware.mmk.anttasks.taskdefs.rollback.settings.RollbackSettings;

import static com.arcadsoftware.mmk.anttasks.taskdefs.rollback.helpers.ERollbackStringConstants.*;

public class ArcadRollbackTask extends AbstractArcadAntTask {
	
	private static final String VERSION="1.0.0.0";
	private Document document;
	private String rootDirectory;
	private ArrayList<String> rootDirectories = new ArrayList<String>();
		
	private String rollbackDir;
	private String rollbackId;
	

	
	
	@Override
	public void validateAttributes() {
		if ((rollbackId==null) || (rollbackId.equals(""))) {
			//Si la valeur pass‚e dans la tache est vide, on recherche dans 
			//le projet
			rollbackId = getProject().getProperty(RB_PROP_ID.getValue());
		}
		if ((rollbackDir==null) || (rollbackDir.equals(""))) {
			//Si la valeur pass‚e dans la tache est vide, on recherche dans 
			//le projet
			rollbackDir = getProject().getProperty(RB_PROP_DIR.getValue());
			if ((rollbackDir==null) || (rollbackDir.equals(""))){
				//Si aucune valeur n'est pass‚, on recherche si il n'y a pas
				// de d‚finition globale
				String value = RollbackSettings.getInstance().getBackupEnvironment();
				if ((value==null) || (value.equals("")))
					throw new BuildException("Rollback Directory is required!");
				else {
					rollbackDir = value;
				}
			}
		} 

		
		File f = new File(rollbackDir);
		if (!f.exists())
			throw new BuildException("Rollback Directory:"+rollbackDir+" not found!");
		else {
			//Si le rollbackId est valide
			if ((rollbackId!=null) && (!rollbackId.equals(""))) {
				File fid = new File(rollbackDir,rollbackId);
				if (!fid.exists())
					throw new BuildException("Transaction Directory:"+rollbackId+" not found!");
				else
					rootDirectories.add(rollbackDir+File.separator+rollbackId); 				
			} else {
				/* Si le rollbakid n'est pas pass‚, on scanne tous r‚pertoires
				 * a la recherche d'un fichier "rollback-settings.xml" et on
				 * enregistre le r‚pertoire p‚re comme ‚tant un root directory
				 */
				DirectoryScanner ds = new DirectoryScanner();
				
				ds.setBasedir(rollbackDir);
				//Parcours recursif pour trouver tous les jars
				ds.setIncludes(new String[]{"**\\"+RB_SETTING_FILENAME.getValue()});
				ds.setCaseSensitive(false);
				ds.setFollowSymlinks(true);
				ds.scan();
				String[] files = ds.getIncludedFiles();
				for (int i= 0;i<files.length;i++){
					File settingFile = new File(rollbackDir+File.separator+files[i]);
					rootDirectories.add(settingFile.getParent());
				}				
				
			}
		}
		
	}
	
	private String getSettingFileName(){
		return rootDirectory+File.separator+RB_SETTING_FILENAME.getValue();
	}	
	
	
	public String getRootDirectory(){
		return rootDirectory;
	}
	
	
	private boolean loadDocument(){
		String fileName = getSettingFileName();
		File f = new File(fileName);
	    SAXReader xmlReader = new SAXReader();
	    try {
			document = xmlReader.read(f);
			return true;
		} catch (DocumentException e) {
			throw new BuildException(e.getMessage(),e,getLocation());
		}
	}
	
	private boolean restoreAction() {			
		Element root = document.getRootElement();
        // iterate through child elements of root with element name "foo"
        for ( Iterator i = root.elementIterator( RB_TAG_ACTION.getValue() ); i.hasNext(); ) {        	
            Element action = (Element) i.next();
            String code = action.attributeValue(RB_TAG_CODE.getValue());
            if (code.equals(RB_ACTIONCODE_COPY.getValue())) {
            	restoreCopy(action);
            } else if (code.equals(RB_ACTIONCODE_DELETE.getValue())) {
            	restoreDelete(action);
            }
        }
		return true;
	}
	
	private void restoreCopy(Element e){
		CopyFileHelper helper = new CopyFileHelper(null);
		helper.setRollbackDir(rollbackDir);
		helper.setRollbackId(rollbackId);		
		helper.rollback(this,e);
	}
	
	private void restoreDelete(Element e){
		DeleteFileHelper helper = new DeleteFileHelper(null);
		helper.setRollbackDir(rollbackDir);
		helper.setRollbackId(rollbackId);			
		helper.rollback(this,e);
	}	
	
	
	@Override
	public void doExecute() throws BuildException {
		//TODO [ANT] Trier le traitement en fonction +r‚cent -> +vieux
		Collections.sort(rootDirectories);
		
		for (int i=0;i<rootDirectories.size();i++) {
			rootDirectory =rootDirectories.get(i); 
			String[] segments = FileUtils.getPathStack(rootDirectory);
			rollbackId = segments[segments.length-1];
			//Chargement du document contenant les informations
			loadDocument();
			restoreAction();
		}
	}

	public void setRollbackId(String rollbackId) {
		this.rollbackId = rollbackId;
	}

	public String getRollbackDir() {
		return rollbackDir;
	}

	public void setRollbackDir(String rollbackDir) {
		this.rollbackDir = rollbackDir;
	}








	
}
