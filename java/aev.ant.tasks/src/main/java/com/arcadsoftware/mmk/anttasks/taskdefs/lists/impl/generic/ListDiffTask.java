package com.arcadsoftware.mmk.anttasks.taskdefs.lists.impl.generic;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;

import org.apache.tools.ant.BuildException;
import com.arcadsoftware.mmk.anttasks.taskdefs.lists.AbstractXmlFileListSetOperationTask;
import com.arcadsoftware.mmk.lists.impl.lists.GenericList;

public class ListDiffTask extends AbstractXmlFileListSetOperationTask {

	Vector<DiffCondition> diffConditions = new Vector<DiffCondition>(); 
	
	public class DiffCondition {
		private String id = "";
		private String operator = "";
		
		public DiffCondition(){
			super();
		}
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
		public String getOperator() {
			return operator;
		}
		public void setOperator(String operator) {
			this.operator = operator;
		}
	}
	
	
	public DiffCondition createDiffCondition(){
		DiffCondition diffCondition = new DiffCondition();
		diffConditions.add(diffCondition);
        return diffCondition;
	}
		
	/* (non-Javadoc)
	 * @see com.arcadsoftware.mmk.anttasks.taskdefs.AbstractArcadAntTask#validateAttributes()
	 */
	@Override
	public void validateAttributes() {
		super.validateAttributes();
		if (resultFileName.equals(operandFileName)) {
			//TODO [ANT] Translation
			throw new BuildException("OperandFileName and resultFileName attributes must not have the same value!");
		} else if (resultFileName.equals(getFilename())){			
			//TODO [ANT] Translation
			throw new BuildException("Filename and resultFileName attributes must not have the same value!");
		}			
	}		
	
	@Override
	public int processExecutionWithCount() {
		//Chargement des métadatas
		list.getMetadatas().clear();		
		list.load(false,true);
		
		GenericList operandList = (GenericList)list.cloneList();		
		operandList.setXmlFileName(operandFileName);
		
		GenericList resultList = (GenericList)list.cloneList();
		resultList.setXmlFileName(resultFileName);	
		
		Hashtable<String,String> conds = new Hashtable<String,String>();
        // handle nested elements
        for (Iterator<DiffCondition> it=diffConditions.iterator(); it.hasNext(); ) {
        	//TODO [ANT] Ajoute un controle sur l'opérateur
        	DiffCondition c = it.next();
        	if (list.getMetadatas().exists(c.getId())) {
        		if (!list.getMetadatas().getColumnFromId(c.getId()).isKey())
        			conds.put(c.getId(),c.getOperator());
        		else
        			throw new BuildException("diffCondition::Id attribute value imust not be a key!");
        	} else
        		throw new BuildException("diffCondition::Id attribute value is invalid!");        		
        }				
		return operandList.intersect(list,resultList.getList(),checkIfExists,replaceIfExists,conds);
	}

}
