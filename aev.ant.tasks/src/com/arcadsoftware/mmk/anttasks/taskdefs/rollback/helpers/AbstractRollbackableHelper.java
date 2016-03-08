package com.arcadsoftware.mmk.anttasks.taskdefs.rollback.helpers;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.XMLWriter;
import org.dom4j.io.XPP3Reader;
import org.xmlpull.v1.XmlPullParserException;


import com.arcadsoftware.ae.core.logger.MessageLogger;
import com.arcadsoftware.mmk.anttasks.AntFactory;
import com.arcadsoftware.mmk.anttasks.taskdefs.rollback.IRollbackableTask;
import com.arcadsoftware.mmk.anttasks.taskdefs.rollback.impl.ArcadRollbackTask;
import com.arcadsoftware.mmk.anttasks.taskdefs.rollback.settings.RollbackSettings;

import static com.arcadsoftware.mmk.anttasks.taskdefs.rollback.helpers.ERollbackStringConstants.*;

public abstract class AbstractRollbackableHelper {
	
	//TODO [RB] G‚r‚r un nø de version

	
	protected String dataDirectory = null;
	Document document;
	

	
	//Racine du r‚pertoire de gestion des rollback.
	private String rollbackDir = "";
	
	//Identificateur de la transaction de rollback;
	private String rollbackId = "";	
	
	private String rollbackIdProperty = null;
		
	
	protected IRollbackableTask task = null;	

	public AbstractRollbackableHelper() {
		this(null);
	}	
	
	
	public AbstractRollbackableHelper (IRollbackableTask task){
		super();
		rollbackId = "ID".concat(computeId());
		this.task = task;
	}
		

	private synchronized String computeId(){
		SimpleDateFormat fd = new SimpleDateFormat("yyyyMMddhhmmssSSS");
		try {
			Thread.sleep(2);
		} catch (InterruptedException e) {}		
		return fd.format(new Date());		
	}		
	
	public Element createRoot(){
		document = DocumentHelper.createDocument();
		document.addDocType(RB_TAG_ROOT.getValue(),null,null);
		//Cr‚ation du noeud root
		Element root = document.addElement(RB_TAG_ROOT.getValue());
		return root;
		
	}	
	
	public Element createHeader(Element root){		
		Element header = root.addElement(RB_TAG_HEADER.getValue());
		header.addAttribute("date",new SimpleDateFormat("yyyyMMdd-HHmmssSSSS").format(new Date()));
		header.addAttribute("user",System.getProperty("user.name"));		
		header.addAttribute("rollback.dir",getBackupRoot());
		return header;
	}
	
	public String getTransactionId() {
		return rollbackId;
	}
	
	public String getBackupRoot(){
		if ((rollbackDir==null) || rollbackDir.equals("")) {
			if (task!=null) {
				Project p = task.getTask().getProject();
				String s = p.getProperty(RB_PROP_DIR.getValue());
				if ((s!=null) && (!s.equals(""))) {
					rollbackDir = s;
				} else {
					//Si aucune valeur n'est pass‚, on recherche si il n'y a pas
					// de d‚finition globale
					String value = AntFactory.getRollbackSettings().getBackupEnvironment();
					System.out.println("VALUE : "+value);
					if ((value==null) || (value.equals("")))
						throw new BuildException("Rollback Directory is required!");
					else {
						rollbackDir = value;
					}										
				}
			} else {
				//Si aucune valeur n'est pass‚, on recherche si il n'y a pas
				// de d‚finition globale
				
				String value = RollbackSettings.getInstance().getBackupEnvironment();
				System.out.println("VALUE : "+value);
				if ((value==null) || (value.equals("")))
					throw new BuildException("Rollback Directory is required!");
				else {
					rollbackDir = value;
				}					
			}
		}		
		return rollbackDir+File.separator+getTransactionId();
	}
	
	public boolean writeDocument() {	
		File f = new File(getBackupRoot());
		if (!f.exists())
			f.mkdirs();
		String fileName = getBackupRoot()+File.separator+RB_SETTING_FILENAME.getValue();
        // lets write to a file
        XMLWriter writer;
		try {
			writer = 
				new XMLWriter(new FileWriter( fileName));
	        writer.write( document );
	        writer.close();	
	        return true;
		} catch (IOException e) {
			MessageLogger.sendErrorMessage(AntFactory.MODULE_NAME,e);
			return false;
		}			
	}
	
	
	public void setRollbackDir(String rollbackDir) {
		this.rollbackDir = rollbackDir;		
	}
	
	public void setRollbackIdProperty(String rollbackIdProperty) {
		this.rollbackIdProperty = rollbackIdProperty;	
	}	
	
	public void doAfterExecuting(){
		//Affectation de la propri‚t‚ retour
        if (rollbackIdProperty != null) {
        	if (task.getTask()!=null){
        		task.getTask().getProject().setNewProperty(rollbackIdProperty, rollbackId);
        	}
        }			
		if (!task.isInTransaction()){
			//<FM number="2010/00474" version="01.00.00" date="Nov 25, 2011 user="FPO">
			boolean result = false;
			String fileName = getBackupRoot()+File.separator+RB_SETTING_FILENAME.getValue();
			File in = new File(fileName);
			if (in.exists())
				result = updateRollbackFile();
			else 				
				result = createRoolbackFile();
			//</FM>
			if (!result) {
				throw new BuildException("Unable to create rollback file info!", task.getTask().getLocation());
			}		
		}
	}		
	
	//<FM number="2010/00474" version="01.00.00" date="Nov 25, 2011 user="FPO">
	public boolean updateRollbackFile(){
		String fileName = getBackupRoot()+File.separator+RB_SETTING_FILENAME.getValue();
		File in = new File(fileName);
		XPP3Reader reader;
		try {
			reader = 
				new XPP3Reader();
			document = reader.read(in);
		} catch (DocumentException e) {
			MessageLogger.sendErrorMessage(AntFactory.MODULE_NAME,e);
		} catch (XmlPullParserException e) {
			MessageLogger.sendErrorMessage(AntFactory.MODULE_NAME,e);
		} catch (IOException e) {
			MessageLogger.sendErrorMessage(AntFactory.MODULE_NAME,e);
		}
		Element root = document.getRootElement();
		createRollbackData(root);
		return writeDocument();		
	}
	//</FM>
	
	public boolean createRoolbackFile(){
		Element root = createRoot();
		createHeader(root);
		createRollbackData(root);
		return writeDocument();
		
	}	
	
	public String getRollbackDir() {
		return rollbackDir;
	}

	public void setRollbackId(String rollbackId) {
		this.rollbackId = rollbackId;
	}

	public String getRollbackId() {
		return rollbackId;
	}	
	
	public abstract Element createRollbackData(Element e);
	public abstract void doBeforeExecuting();			
	public abstract boolean rollback(ArcadRollbackTask rollbackTask,Element e);


	
	




	
	

	

}

