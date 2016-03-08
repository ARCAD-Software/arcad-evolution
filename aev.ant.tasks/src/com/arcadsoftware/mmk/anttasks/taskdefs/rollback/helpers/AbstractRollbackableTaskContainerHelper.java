package com.arcadsoftware.mmk.anttasks.taskdefs.rollback.helpers;


import java.util.Iterator;

//import org.apache.tools.ant.BuildException;
//import org.apache.tools.ant.Task;
import org.dom4j.Element;

import com.arcadsoftware.mmk.anttasks.taskdefs.rollback.IRollbackableTask;
import com.arcadsoftware.mmk.anttasks.taskdefs.rollback.impl.ArcadRollbackTask;

public abstract class AbstractRollbackableTaskContainerHelper 
extends AbstractRollbackableHelper {

	public AbstractRollbackableTaskContainerHelper() {
		super();
	}

	public AbstractRollbackableTaskContainerHelper(IRollbackableTask task) {
		super(task);
	}

	public void doBeforeExecuting(){
//		if (dataDirectory==null) {
//			if (task instanceof Task)
//				throw new BuildException("Unable to create rollback directory!", ((Task)task).getLocation());
//			else
//				throw new BuildException("Unable to create rollback directory!");				
//		}			
	}	
	
	
	@Override
	public boolean rollback(ArcadRollbackTask rollbackTask, Element e) {
		return false;
	}

	@Override
	public Element createRollbackData(Element e) {	
        for (Iterator i = getTasks(); i.hasNext();) {
            Object o =  i.next();
            if (o instanceof IRollbackableTask) {
            	IRollbackableTask t = (IRollbackableTask)o ;
            	if (t.getHelper() instanceof AbstractRollbackableTaskHelper){
            		AbstractRollbackableTaskHelper thelper = 
            			(AbstractRollbackableTaskHelper)t.getHelper();
            		thelper.createRollbackData(e);            		
            	}            
            }
        }		        
		return e;
	}		
	
	

	
	public abstract Iterator getTasks(); 

}
