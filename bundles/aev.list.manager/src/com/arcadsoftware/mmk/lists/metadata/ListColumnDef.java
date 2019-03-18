package com.arcadsoftware.mmk.lists.metadata;

public class ListColumnDef {
	private String id = null;	
	private String propertyName = null;
	private boolean key =false;
	private ListColumnDataType datatype;
	
	private String label = null;
	private int position = -1;	
	
	public ListColumnDef(){
		super();
	}
	
//	public ListColumnDef(int position, String name){
//		this(position, name, name,name,false);
//	}	
	
//	public ListColumnDef(int position, String id,String label,String propertyName,boolean key){
//		super();
//		this.position = position;
//		this.id = id;
//		this.label = label;
//		this.propertyName = propertyName;
//		this.key = key;		
//	}
	
	
	
	public ListColumnDef(String id,String propertyName,ListColumnDataType datatype,boolean key){
		super();
		this.id = id;
		this.propertyName = propertyName;
		this.key = key;
		this.datatype = datatype;
	}	

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof ListColumnDef) {
			return ((ListColumnDef)obj).getId().equals(getId());
		}
		return false;
	}

	public ListColumnDef duplicate() {
		//ListColumnDef cd = new ListColumnDef(position,id,label,propertyName,key);
		ListColumnDef cd = new ListColumnDef(id,propertyName,datatype,key);
		return cd;
	}
	
	public String toString() {
		return "id="+id+
		       " position="+position+
		       " label="+label+
		       " propertyname="+propertyName+
		       " key"+key;
	}
	
	/**
	 * Renvoit 
	 * @return the key boolean : 
	 */
	public boolean isKey() {
		return key;
	}

	/**
	 * @param key the key to set
	 */
	public void setKey(boolean key) {
		this.key = key;
	}

//	/**
//	 * Renvoit 
//	 * @return the label String : 
//	 */
//	public String getLabel() {
//		return label;
//	}
//
//	/**
//	 * @param label the label to set
//	 */
//	public void setLabel(String label) {
//		this.label = label;
//	}

	/**
	 * Renvoit 
	 * @return the id String : 
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String name) {
		this.id = name;
	}

//	/**
//	 * Renvoit 
//	 * @return the position int : 
//	 */
//	public int getPosition() {
//		return position;
//	}	
//
//	/**
//	 * @param position the position to set
//	 */
//	public void setPosition(int position) {
//		this.position = position;
//	}

	/**
	 * Renvoit 
	 * @return the propertyName String : 
	 */
	public String getPropertyName() {
		return propertyName;
	}

	/**
	 * @param propertyName the propertyName to set
	 */
	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}

	/**
	 * Renvoit 
	 * @return the datatype ListColumnDataType : 
	 */
	public ListColumnDataType getDatatype() {
		return datatype;
	}

	/**
	 * @param datatype the datatype to set
	 */
	public void setDatatype(ListColumnDataType datatype) {
		this.datatype = datatype;
	}	
	
	/**
	 * @param datatype the datatype to set
	 */
	public void setDatatypeFromText(String stringDataType) {
		if (stringDataType.equals("string"))
			this.datatype = ListColumnDataType.STRING;
		else if (stringDataType.equals("integer"))		
			this.datatype = ListColumnDataType.INTEGER;
		else if (stringDataType.equals("float"))		
			this.datatype = ListColumnDataType.FLOAT;		
	}		
	
	
	
}

