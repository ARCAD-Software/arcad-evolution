package com.arcadsoftware.mmk.anttasks.taskdefs.lists.impl.generic;

import java.io.File;
import java.util.Iterator;
import java.util.Vector;

import org.apache.tools.ant.BuildException;

import com.arcadsoftware.ae.core.exceptions.ArcadException;
import com.arcadsoftware.mmk.anttasks.taskdefs.lists.AbstractXmlFileListWithItem;
import com.arcadsoftware.mmk.anttasks.taskdefs.lists.impl.item.Item;
import com.arcadsoftware.mmk.anttasks.taskdefs.lists.impl.item.ItemValue;

import com.arcadsoftware.mmk.lists.impl.lists.GenericList;
import com.arcadsoftware.mmk.lists.metadata.ListColumnDef;
import com.arcadsoftware.mmk.lists.metadata.ListMetaDatas;
import com.arcadsoftware.mmk.lists.metadata.StoreItem;

public class ListCreationTask extends AbstractXmlFileListWithItem {

    private boolean replaceFileIfExists = false;
    private boolean checkDuplication = false;
    
    private String description = null;
    private String comment = null;
    
   
	Vector<Metadata> metadatas = new Vector<Metadata>();  
	
	
	public class ItemDef {
		ListColumnDef cd;

		public ItemDef(){
			cd = new ListColumnDef();
		}
		
		public ListColumnDef getColumnDef() {
			return cd;
		}		
		public void setKey(String key) {
			cd.setKey(key.equalsIgnoreCase("true"));
		}
		public void setId(String name) {
			cd.setId(name);
		}
		public void setPropertyName(String propertyName) {
			cd.setPropertyName(propertyName);
		}	
		public void setDataType(String datatype) {
			cd.setDatatypeFromText(datatype);
		}		
	}	
		
	
	
	public class Metadata {
		Vector<ItemDef> defs = new Vector<ItemDef>();
		
		
		public Metadata(){
			super();
		}

		public ItemDef createItemDef(){
			ItemDef itemDef = new ItemDef();
			defs.add(itemDef);			
	        return itemDef;
		}
		/**
		 * Renvoit 
		 * @return the cols ListMetaDatas : 
		 */
		public ListMetaDatas getCols() {
			ListMetaDatas cols = new ListMetaDatas();
			for (int i=0;i<defs.size();i++) {
				cols.addColumnDef(defs.elementAt(i).getColumnDef());
			}
			return cols;
		}    		
		
	}	
	
	
	public Metadata createMetadata(){
		Metadata metadata = new Metadata();
		metadatas.add(metadata);
        return metadata;
	}  	
	
	/* (non-Javadoc)
	 * @see com.arcadsoftware.mmk.anttasks.taskdefs.AbstractArcadAntTask#validateAttributes()
	 */
	@Override
	public void validateAttributes() {
		super.validateAttributes();
		//Le controle de saisie est réalisé dans le super
		File f = new File(getFilename());
		if (f.exists()) {
			//Si l'utilisateur n'a pas demandé le remplacement
			//on envoit un exception
			if (!replaceFileIfExists)
				throw new BuildException("File already exists!");
			else {
				//Sinon on supprime le fichier
				if (!f.delete()) 
					throw new BuildException("File Replacement failed!");
			}
		}	
		
		//Si une liste est passé en paramètre, les métadatas ne seront 
		//prises dans la définition de la liste.
		if ((fromListFileName==null) || fromListFileName.equals("")){
			switch (metadatas.size()) {
			case 0:
				throw new BuildException("Metadata Information must be set!");
			case 1:
				for (Iterator<Metadata> it=metadatas.iterator(); it.hasNext(); ) {
					Metadata md = it.next();
					ListMetaDatas cols =  md.getCols();
					if (cols.count()==0) {
						throw new BuildException("At least one ItemDef must be set!");
					} else {						
						try {
							cols.valid();
						} catch (ArcadException e) {
							throw new BuildException(e.getMessage());
						}
					}					
				}	
				break;
			default:
				throw new BuildException("Only one Metadata Tag could be used!");
			}
		} else {
			
		}

	}	
	
	@Override
	public int processExecutionWithCount() {
		if ((fromListFileName==null) || fromListFileName.equals("")){
			Metadata m = metadatas.elementAt(0);		
			list.setMetadatas(m.getCols());
		}
		//Traitement de l'ajout à partir d'une liste
		int count =0;
		if ((fromListFileName!=null) && !fromListFileName.equals("")){
			//Déclaration de la liste        
			GenericList fromlist = (GenericList)list.cloneList();
			fromlist.setXmlFileName(fromListFileName);	
			fromlist.load(false,true);
			list.setMetadatas(fromlist.getMetadatas());	
			list.initStoreItem();
			if (checkDuplication) {
				count = list.addItems(fromlist,checkIfExists,replaceIfExists);
			} else {
				fromlist.duplicate(list);
				count = list.count("");
			}
		}		
		int count2 =0;
		//Ajout des données
        for (Iterator<Item> it=items.iterator(); it.hasNext(); ) {
        	Item c = it.next();
        	Vector<ItemValue> v = c.getValues();
        	StoreItem storeItem = new StoreItem();
        	storeItem.setMetadatas(list.getMetadatas());
        	boolean toadd = false;
            for (Iterator<ItemValue> it2=v.iterator(); it2.hasNext(); ) {            	
            	ItemValue val = it2.next();
            	if (list.getMetadatas()!=null) {
	            	ListColumnDef cd = list.getMetadatas().getColumnFromId(val.getId());
	            	if (cd!=null) {
	            		storeItem.setValue(cd.getId(),val.getValue());
	            		toadd = true;
	            	}   
            	}
            } 
            if (toadd) {
            	int i = list.addItems(storeItem,checkIfExists,replaceIfExists);
            	count2+=i;
            }
        }		
        updateHeaderInfo(description,comment);
        
		return count+count2;
	}
	
	/**
	 * @param replaceFileIfExists the replaceFileIfExists to set
	 */
	public void setReplaceFileIfExists(boolean replaceFileIfExists) {
		this.replaceFileIfExists = replaceFileIfExists;
	}
	/**
	 * @param checkDuplication the checkDuplication to set
	 */
	public void setCheckDuplication(boolean checkDuplication) {
		this.checkDuplication = checkDuplication;
	}

	/**
	 * @param comment the comment to set
	 */
	public void setComment(String comment) {
		this.comment = comment;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}	

}
