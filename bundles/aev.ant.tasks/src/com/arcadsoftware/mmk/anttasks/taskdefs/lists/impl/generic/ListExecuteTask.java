package com.arcadsoftware.mmk.anttasks.taskdefs.lists.impl.generic;

import java.util.Iterator;
import java.util.Vector;

import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.TaskContainer;

import com.arcadsoftware.mmk.anttasks.taskdefs.lists.AbstractXmlFileListCountedTask;
import com.arcadsoftware.mmk.lists.IListBrowseListener;
import com.arcadsoftware.mmk.lists.metadata.ListColumnDef;
import com.arcadsoftware.mmk.lists.metadata.ListMetaDatas;
import com.arcadsoftware.mmk.lists.metadata.StoreItem;

public class ListExecuteTask extends AbstractXmlFileListCountedTask 
implements IListBrowseListener,TaskContainer{

	private boolean failonerror  = false;
	private String filter  = null;
	private String okCountProperty  = null;
	private String nokCountProperty  = null;	
	
	private boolean initStatusBeforeProcessing   = false;
	private boolean startOnLastProcessedElement  = false;
	private boolean holdIfFailed  = false;	
	
	private boolean searchingForLastProcessedElement =  false;
	
    protected Vector<Task> nestedTasks = new Vector<Task>();
	int okCount = 0;
	int nokCount = 0;    
	
	int itemCount = 0;
	
	
	public void addTask(Task task) {
		nestedTasks.addElement(task);
	}
    
	@Override
	public int processExecutionWithCount() {
		list.addBrowseListener(this);
		try {
			if (initStatusBeforeProcessing) {
				list.reinitializeValue("status",StoreItem.STATUS_NONE);
			}
			if (startOnLastProcessedElement) {
				searchingForLastProcessedElement = true;
			}			
			list.load(false,true);
			int result =  list.browse(filter);
	    	if ((okCountProperty!=null) && (!okCountProperty.equals(""))) {        	
	    		getProject().setNewProperty(okCountProperty, String.valueOf(okCount));
	    	}   
	    	//Mise … jour des propri‚t‚s de retour
	    	if ((nokCountProperty!=null) && (!nokCountProperty.equals(""))) {   
	    		getProject().setNewProperty(nokCountProperty, String.valueOf(nokCount));
	    	} 	
	    	return result;
		} finally {
			list.removeBrowseListener(this);
		}
	}	
	
	
	public void executeTasks(StoreItem item){
        for (Iterator i = nestedTasks.iterator(); i.hasNext();) { 
        	Task nestedTask = (Task)i.next();
            nestedTask.perform();	            
        }		
	}
	
	public void elementBrowsed(StoreItem item) {
		itemCount++;
		String status = item.getValues()[0];
		if (searchingForLastProcessedElement) {
			//Si l'‚l‚ment a un status NOK et qu'il n'est pas suspendu
			if ((status.equalsIgnoreCase(StoreItem.STATUS_NOK)) && 
			   (!status.equals(StoreItem.STATUS_HOLD))){
				searchingForLastProcessedElement = false;
			} else
				return;
		}
		if (!status.equals(StoreItem.STATUS_HOLD)) {
			//Affectation des valeurs du projet
			ListMetaDatas md = item.getMetadatas();
			for (int i=0;i<md.count();i++) {
				ListColumnDef cd = md.getColumnDefAt(i);
				getProject().setProperty(cd.getPropertyName(),item.getValue(i));			
			}	
			getProject().setProperty("arcad.item.count",String.valueOf(itemCount));
	    	try {
	    		executeTasks(item);
		        okCount++;
		        item.setValue(0,StoreItem.STATUS_OK);
		        list.updateItems(item);
	    	} catch (Exception e) {
	    		nokCount++;
	    		if (holdIfFailed)
	    			item.setValue(0,StoreItem.STATUS_HOLD);
	    		else
	    			item.setValue(0,StoreItem.STATUS_NOK);
		        list.updateItems(item);    		
	    		if (failonerror) {
	    			list.setFollowBrowsing(false);
	    		}
	    		getProject().log(this,e.getMessage(),Project.MSG_ERR);    		
			}		
		}
	}

	/**
	 * @param failonerror the failonerror to set
	 */
	public void setFailonerror(boolean failonerror) {
		this.failonerror = failonerror;
	}

	/**
	 * @param filter the filter to set
	 */
	public void setFilter(String filter) {
		this.filter = filter;
	}

	/**
	 * @param initStatusBeforeProcessing the initStatusBeforeProcessing to set
	 */
	public void setInitStatusBeforeProcessing(boolean initStatusBeforeProcessing) {
		this.initStatusBeforeProcessing = initStatusBeforeProcessing;
	}

	/**
	 * @param startOnLastProcessedElement the startOnLastProcessedElement to set
	 */
	public void setStartOnLastProcessedElement(boolean startOnLastProcessedElement) {
		this.startOnLastProcessedElement = startOnLastProcessedElement;
	}

	/**
	 * @param holdIfFailed the holdIfFailed to set
	 */
	public void setHoldIfFailed(boolean holdIfFailed) {
		this.holdIfFailed = holdIfFailed;
	}





}
