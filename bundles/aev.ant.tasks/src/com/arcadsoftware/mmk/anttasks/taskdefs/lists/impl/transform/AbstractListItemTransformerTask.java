package com.arcadsoftware.mmk.anttasks.taskdefs.lists.impl.transform;

import org.apache.tools.ant.BuildException;

import com.arcadsoftware.mmk.anttasks.taskdefs.AbstractArcadAntTask;
import com.arcadsoftware.mmk.lists.AbstractXmlList;
import com.arcadsoftware.mmk.lists.metadata.ListColumnDef;
import com.arcadsoftware.mmk.lists.metadata.ListMetaDatas;
import com.arcadsoftware.mmk.lists.metadata.StoreItem;

public abstract class AbstractListItemTransformerTask extends AbstractArcadAntTask {
	
	private boolean verbose=false;
	
	private String sourceProperty=null; 
	private String resultProperty=null;
	
	private String sourceValue=null; 
	private String resultValue=null;

	private int sourceIndex=-1;
	private int targetIndex=-1;
	
	private StoreItem item=null;
	private AbstractXmlList parentList=null;
	

	@Override
	public void validateAttributes() {
		if ((sourceProperty==null) || (sourceProperty.equals(""))) {		
			throw new BuildException("Source property is required!");
		} 
		if ((resultProperty==null) || (resultProperty.equals(""))) {	
			resultProperty = sourceProperty;			
		} 		
	}

	public int validPropertyId(String property){
		ListMetaDatas md = item.getMetadatas();
		for (int i=0;i<md.count();i++) {
			ListColumnDef cd = md.getColumnDefAt(i);
			if (cd.getId().equalsIgnoreCase(property)) {
				return i;
			}						
		}	
		return -1;
	}
	
	@Override
	public void doExecute() throws BuildException {
		if ((parentList==null) ) {		
			throw new BuildException("List is required!");
		} 		
		if ((item==null) ) {		
			throw new BuildException("Item is required!");
		} 	
		sourceIndex = validPropertyId(sourceProperty);
		if (sourceIndex==-1) {
			throw new BuildException("Invalid Source Property Identifier");
		}
		targetIndex = validPropertyId(resultProperty);
		if (targetIndex==-1) {
			throw new BuildException("Invalid Result Property Identifier");
		}					
		sourceValue = item.getValue(sourceProperty);
		if (verbose){ 
			System.out.println("Source Value = "+sourceValue);
		}
		resultValue = transform(sourceValue);
		if (verbose){ 
			System.out.println("Result Value = "+resultValue);
		}

		item.setValue(targetIndex, resultValue);
		parentList.updateItems(item);
	}	
	
	public String getSourceProperty() {
		return sourceProperty;
	}

	public void setSourceProperty(String sourceProperty) {
		this.sourceProperty = sourceProperty;
	}

	public String getResultProperty() {
		return resultProperty;
	}

	public void setResultProperty(String resultProperty) {
		this.resultProperty = resultProperty;
	}

	public StoreItem getItem() {
		return item;
	}

	public void setItem(StoreItem item) {
		this.item = item;
	}

	public AbstractXmlList getParentList() {
		return parentList;
	}

	public void setParentList(AbstractXmlList parentList) {
		this.parentList = parentList;
	}

	
	
	public boolean isVerbose() {
		return verbose;
	}

	public void setVerbose(boolean verbose) {
		this.verbose = verbose;
	}

	public abstract String transform(String sourceValue);
	
	
	
}
