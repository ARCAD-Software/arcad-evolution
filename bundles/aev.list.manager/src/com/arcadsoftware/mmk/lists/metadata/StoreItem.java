package com.arcadsoftware.mmk.lists.metadata;

import java.util.ArrayList;




public class StoreItem {
	
	//TODO [LM] définir les status un peu mieux que ca!!!
	public static final String STATUS_OK = "0";
	public static final String STATUS_NOK = "1";	
	public static final String STATUS_HOLD = "H";	
	public static final String STATUS_NONE = "";	
	
	
	private int rowId = -1;
	private String[] values;
	private ListMetaDatas metadatas;


	public StoreItem(){
		super();
	}
	public StoreItem(ListMetaDatas metadata){
		super();
		setMetadatas(metadata);
	}	
	
	public void setValue(String id,String value){
		int columnIndex = metadatas.indexOf(id);
		values[columnIndex] = value;
	}
	
	public void setValue(int columnIndex,String value){
		values[columnIndex] = value;
	}	
	
	public String getValue( int columnIndex){
		String result = null;
		if (columnIndex<values.length) {
			result = values[columnIndex];
		}
		if (result==null)
			return "";
		return result;
	}	
	
	public String getValue(String id){
		int columnIndex = metadatas.indexOf(id);
		return getValue(columnIndex);
	}	
	
	
	public void setUserValue(int columnIndex,String value){
		values[columnIndex+metadatas.getFixedMetadataCount()] = value;
	}	
	
	public void setUserValue(String[] stringValues){
		for (int i=0;i<stringValues.length;i++) {
			values[i+metadatas.getFixedMetadataCount()] = stringValues[i];	
		}
	}	

	public String getUserValue(int columnIndex){
		return values[columnIndex+metadatas.getFixedMetadataCount()]; 
	}	
	

	/**
	 * Renvoit 
	 * @return the metadatas ListMetaDatas : 
	 */
	public ListMetaDatas getMetadatas() {
		return metadatas;
	}

	/**
	 * @param metadatas the metadatas to set
	 */
	public void setMetadatas(ListMetaDatas metadata) {
		this.metadatas = metadata;
		values = new String[metadata.count()];
	}
	/**
	 * Renvoit 
	 * @return the rowId int : 
	 */
	public int getRowId() {
		return rowId;
	}
	/**
	 * @param rowId the rowId to set
	 */
	public void setRowId(int rowId) {
		this.rowId = rowId;
	}
	/**
	 * Renvoit 
	 * @return the values String[] : 
	 */
	public String[] getValues() {
		return values;
	}
	
	/**
	 * Renvoit 
	 * @return the values String[] : 
	 */
	public String[] getKeyValues() {
		ArrayList<String> l =new ArrayList<String>();
		for (int i=0;i<metadatas.count();i++) {
			ListColumnDef cd = metadatas.getColumnDefAt(i);
			if (cd.isKey()) {
				String value = getValue(i);
				l.add(value);
			}
		}		
		String[] a = new String[l.size()];
		for (int i=0;i<l.size();i++) {
			a[i] = l.get(i);
		}				
		return a;
	}	
	
	/**
	 * Renvoit 
	 * @return the values String[] : 
	 */
	public String[] getUpdateValues() {
		ArrayList<String> l =new ArrayList<String>();
		for (int i=0;i<metadatas.count();i++) {
			ListColumnDef cd = metadatas.getColumnDefAt(i);
			if (!cd.isKey()) {
				String value = getValue(i);
				l.add(value);
			}
		}			
		for (int i=0;i<metadatas.count();i++) {
			ListColumnDef cd = metadatas.getColumnDefAt(i);
			if (cd.isKey()) {
				String value = getValue(i);
				l.add(value);
			}
		}		
		String[] a = new String[l.size()];
		for (int i=0;i<l.size();i++) {
			a[i] = l.get(i);
		}		
		
		return a;
	}		

	
	public String getKey(){
		StringBuffer key = new StringBuffer();
		int count = metadatas.count();
		for (int i=0;i<count;i++) {
			ListColumnDef cd = metadatas.getColumnDefAt(i);
			if (cd.isKey()) {
				key.append(values[i]).append((char)1);
			}
		}		
		return key.toString();
	}

	
}

