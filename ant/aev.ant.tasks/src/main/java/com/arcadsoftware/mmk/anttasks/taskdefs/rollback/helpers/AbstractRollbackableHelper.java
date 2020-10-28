package com.arcadsoftware.mmk.anttasks.taskdefs.rollback.helpers;

import static com.arcadsoftware.mmk.anttasks.taskdefs.rollback.helpers.ERollbackStringConstants.RB_PROP_DIR;
import static com.arcadsoftware.mmk.anttasks.taskdefs.rollback.helpers.ERollbackStringConstants.RB_SETTING_FILENAME;
import static com.arcadsoftware.mmk.anttasks.taskdefs.rollback.helpers.ERollbackStringConstants.RB_TAG_HEADER;
import static com.arcadsoftware.mmk.anttasks.taskdefs.rollback.helpers.ERollbackStringConstants.RB_TAG_ROOT;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.arcadsoftware.ae.core.utils.XMLUtils;
import com.arcadsoftware.mmk.anttasks.AntFactory;
import com.arcadsoftware.mmk.anttasks.taskdefs.rollback.IRollbackableTask;
import com.arcadsoftware.mmk.anttasks.taskdefs.rollback.impl.ArcadRollbackTask;
import com.arcadsoftware.mmk.anttasks.taskdefs.rollback.settings.RollbackSettings;

public abstract class AbstractRollbackableHelper {
	protected String dataDirectory = null;
	protected Document document;	
	private String rollbackDir = "";
	private String rollbackId = "";	
	private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd-HHmmssSSSS");
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
		

	private String computeId(){				
		return UUID.randomUUID().toString();		
	}		
	
	public Element createRoot(){
		try {
			document = XMLUtils.createNewXMLDocument();
			final Element root = document.createElement(RB_TAG_ROOT.getValue());
			document.appendChild(root);
			return root;
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
			return null;
		}			
	}	
	
	public Element createHeader(Element root){
		final Element header = document.createElement(RB_TAG_HEADER.getValue());
		root.appendChild(header);
		header.setAttribute("date",dateFormat.format(new Date()));
		header.setAttribute("user",System.getProperty("user.name"));		
		header.setAttribute("rollback.dir",getBackupRoot());
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
					String value = AntFactory.getRollbackSettings().getBackupEnvironment();
					System.out.println("VALUE : "+value);
					if ((value==null) || (value.equals("")))
						throw new BuildException("Rollback Directory is required!");
					else {
						rollbackDir = value;
					}										
				}
			} else {
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
		try {
			File f = new File(getBackupRoot(), RB_SETTING_FILENAME.getValue());
			f.getParentFile().mkdirs();
			XMLUtils.writeXMLDocumentToFile(document, f, StandardCharsets.UTF_8.name());	
	        return true;
		}
		catch (Exception e) {
			if (task != null) {
				task.getTask().getProject().log(e.getLocalizedMessage(),e,Project.MSG_ERR);
			}
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
		File in = new File( getBackupRoot(), RB_SETTING_FILENAME.getValue());
		try {
			document = XMLUtils.loadXMLFromFile(in);		
		}
		catch (Exception e) {
			if (task!=null) {
				task.getTask().getProject().log(e.getLocalizedMessage(),e,Project.MSG_ERR);
			}

		}
		final Element root = XMLUtils.getRoot(document);
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

