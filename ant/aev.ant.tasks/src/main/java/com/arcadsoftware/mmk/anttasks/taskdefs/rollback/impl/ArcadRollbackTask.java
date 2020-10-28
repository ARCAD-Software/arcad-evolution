package com.arcadsoftware.mmk.anttasks.taskdefs.rollback.impl;

import static com.arcadsoftware.mmk.anttasks.taskdefs.rollback.helpers.ERollbackStringConstants.RB_ACTIONCODE_COPY;
import static com.arcadsoftware.mmk.anttasks.taskdefs.rollback.helpers.ERollbackStringConstants.RB_ACTIONCODE_DELETE;
import static com.arcadsoftware.mmk.anttasks.taskdefs.rollback.helpers.ERollbackStringConstants.RB_PROP_DIR;
import static com.arcadsoftware.mmk.anttasks.taskdefs.rollback.helpers.ERollbackStringConstants.RB_PROP_ID;
import static com.arcadsoftware.mmk.anttasks.taskdefs.rollback.helpers.ERollbackStringConstants.RB_SETTING_FILENAME;
import static com.arcadsoftware.mmk.anttasks.taskdefs.rollback.helpers.ERollbackStringConstants.RB_TAG_ACTION;
import static com.arcadsoftware.mmk.anttasks.taskdefs.rollback.helpers.ERollbackStringConstants.RB_TAG_CODE;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.util.FileUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.arcadsoftware.ae.core.utils.XMLUtils;
import com.arcadsoftware.mmk.anttasks.taskdefs.AbstractArcadAntTask;
import com.arcadsoftware.mmk.anttasks.taskdefs.rollback.helpers.CopyFileHelper;
import com.arcadsoftware.mmk.anttasks.taskdefs.rollback.helpers.DeleteFileHelper;
import com.arcadsoftware.mmk.anttasks.taskdefs.rollback.settings.RollbackSettings;

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
			//Si la valeur passée dans la tache est vide, on recherche dans 
			//le projet
			rollbackId = getProject().getProperty(RB_PROP_ID.getValue());
		}
		if ((rollbackDir==null) || (rollbackDir.equals(""))) {
			//Si la valeur passée dans la tache est vide, on recherche dans 
			//le projet
			rollbackDir = getProject().getProperty(RB_PROP_DIR.getValue());
			if ((rollbackDir==null) || (rollbackDir.equals(""))){
				//Si aucune valeur n'est passé, on recherche si il n'y a pas
				// de définition globale
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
				/* Si le rollbakid n'est pas passé, on scanne tous répertoires
				 * a la recherche d'un fichier "rollback-settings.xml" et on
				 * enregistre le répertoire pre comme étant un root directory
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
		File f = new File(getSettingFileName());	    
	    try {
			document = XMLUtils.loadXMLFromFile(f);
			return true;
		} catch (Exception e) {
			throw new BuildException(e.getMessage(),e,getLocation());
		}
	}
	
	private boolean restoreAction() {			
		Element root = XMLUtils.getRoot(document);
        // iterate through child elements of root with element name "foo"
		final NodeList nodes = root.getElementsByTagName(RB_TAG_ACTION.getValue());
        for ( int i = 0; i < nodes.getLength(); i++ ) {        	
            Element action = (Element) nodes.item(i);
            String code = action.getAttribute(RB_TAG_CODE.getValue());
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
		//TODO [ANT] Trier le traitement en fonction +récent -> +vieux
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
