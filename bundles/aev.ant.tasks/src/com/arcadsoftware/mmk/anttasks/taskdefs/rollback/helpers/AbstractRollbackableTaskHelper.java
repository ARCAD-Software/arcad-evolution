package com.arcadsoftware.mmk.anttasks.taskdefs.rollback.helpers;

import static com.arcadsoftware.mmk.anttasks.taskdefs.rollback.helpers.ERollbackStringConstants.*;

import java.io.File;

import org.apache.tools.ant.BuildException;
import org.dom4j.Element;

import com.arcadsoftware.mmk.anttasks.taskdefs.rollback.IRollbackableTask;

public abstract class AbstractRollbackableTaskHelper 
extends AbstractRollbackableHelper {
	
	
	
	public AbstractRollbackableTaskHelper(IRollbackableTask task) {
		super(task);
	}
	
	public AbstractRollbackableTaskHelper() {
		super();
	}	

	public String createNewActionDirectory(){
		String basedir = getBackupRoot();
		String baseFile = basedir+File.separator+task.getActionCode();
		int i = 0;
		File f;
		do {
			i++;
			f = new File(baseFile+i);			
		} while(f.exists());
		
		task.getTask().getProject().log("--> "+f.getAbsolutePath());
		File parent = f.getParentFile();
		task.getTask().getProject().log("--> parent "+parent.getAbsolutePath());
		if (!parent.exists()) {
			task.getTask().getProject().log("--> parent : create directory");
			if (f.mkdirs()){
				return f.getAbsolutePath();
			} else {
				task.getTask().getProject().log("--> error creating directory");
				return null;
			}
		} else {
			return f.getAbsolutePath();
		}
	}	
	
	public void doBeforeExecuting(){
		dataDirectory = createNewActionDirectory();
		if (dataDirectory==null) {
			throw new BuildException("Unable to create rollback directory!", task.getTask().getLocation());
		}			
	}
		
	@Override
	public Element createRollbackData(Element e) {		
		Element action = e.addElement(RB_TAG_ACTION.getValue());
		action.addAttribute(RB_TAG_CODE.getValue(),task.getActionCode());		
		action.addAttribute(RB_TAG_VERSION.getValue(),task.getVersion());		
		return action;
	}		
	
	
}
