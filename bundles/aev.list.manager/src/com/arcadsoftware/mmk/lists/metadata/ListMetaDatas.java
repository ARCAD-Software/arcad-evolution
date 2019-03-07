package com.arcadsoftware.mmk.lists.metadata;

import java.util.ArrayList;
import java.util.HashMap;

import com.arcadsoftware.ae.core.exceptions.ArcadException;

public class ListMetaDatas  {

	private int fixedMetadataCount = 0;
	private ArrayList<ListColumnDef> cols = new  ArrayList<ListColumnDef>();
	private HashMap<String,ListColumnDef> hashmap = new HashMap<String,ListColumnDef>(); 
	private String id = null;
	private String version = null;	
	
	public ListMetaDatas(){
		super();
		createFixedMetaDatas();
	}
	
	protected void createFixedMetaDatas(){		
		this.addColumnDef("status","arcad.list.status",ListColumnDataType.STRING, false);
		fixedMetadataCount = 1;
	}
	
	public boolean addColumnDef(ListColumnDef columnDef) {
		return addColumnDef(columnDef.getId(),
				            columnDef.getPropertyName(),
				            columnDef.getDatatype(),
				            columnDef.isKey());		
	}
	
//	public boolean addColumnDef(String id){
//		return addColumnDef(name, name,name,false);
//	}	
	
//	public boolean addColumnDef(String name,
//			                    String label,
//			                    String propertyName,
//			                    boolean key){
//		int position = cols.size();
//		ListColumnDef cd = new ListColumnDef(position,name, label,propertyName,key);
//		hashmap.put(cd.getId(),cd);
//		return cols.add(cd);
//	}	
	
	public boolean addColumnDef(String id,
			                    String propertyName,
			                    ListColumnDataType dataType,			                    
			                    boolean key){
		int position = cols.size();
		ListColumnDef cd = new ListColumnDef(id,propertyName, dataType,key);
		hashmap.put(cd.getId(),cd);
		return cols.add(cd);
	}			
	
	
	
	public ListColumnDef getColumnDefAt(int index) {
		if ((index>-1) && (index<cols.size())) {
			return cols.get(index);
		}
		return null;		
	}
	
	public ListColumnDef getColumnFromId(String id) {
		return hashmap.get(id);
	}	
	
	public int indexOf(ListColumnDef cd) {
		return cols.indexOf(cd);
	}	
	
	public int indexOf(String id) {
		ListColumnDef cd = getColumnFromId(id);
		return indexOf(cd);
	}	
	
	
	
	public boolean exists(String id) {
		return (hashmap.get(id)!=null);
	}		
	
	public int count() {
		return cols.size();
	}
	
	public void clear() {
		cols.clear();
		hashmap.clear();		
	}	

	public ListMetaDatas duplicate() {
		ListMetaDatas result = new ListMetaDatas();
		for (int i=fixedMetadataCount;i<cols.size();i++) {
			ListColumnDef newcd = cols.get(i).duplicate();
			result.addColumnDef(newcd);
		}		
		return result;
	}		
	
	public void print() {
		for (int i=0;i<cols.size();i++) {
			ListColumnDef newcd = cols.get(i);
			System.out.println(newcd.toString());			
		}		
	}	
	
	/**
	 * Renvoit 
	 * @return the fixedMetadataCount int : 
	 */
	public int getFixedMetadataCount() {
		return fixedMetadataCount;
	}
	
	public ListColumnDef[] getKeys(){
		ArrayList<ListColumnDef> lkeys = 
			new  ArrayList<ListColumnDef>();
		for (int i=0;i<cols.size();i++) {
			ListColumnDef cd = cols.get(i);
			if (cd.isKey()) {
				lkeys.add(cd);
			}
		}
		ListColumnDef[] result = new ListColumnDef[lkeys.size()]; 
		for (int i=0;i<lkeys.size();i++) {
			result[i]=lkeys.get(i);
		}
		return result;
	}

	/**
	 * Renvoit 
	 * @return the id String : 
	 */
	public String getId() {
		if (id==null)
			return "";
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * Renvoit 
	 * @return the version String : 
	 */
	public String getVersion() {
		if (version==null)
			return "";		
		return version;
	}

	/**
	 * @param version the version to set
	 */
	public void setVersion(String version) {
		this.version = version;
	}

	public void valid () throws ArcadException {
		//TODO [LM] Validation des métadatas  		
	}
	
	
}
