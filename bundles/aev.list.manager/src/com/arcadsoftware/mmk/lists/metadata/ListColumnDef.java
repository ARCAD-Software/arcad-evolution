package com.arcadsoftware.mmk.lists.metadata;

import java.text.MessageFormat;

public class ListColumnDef {
	private ListColumnDataType datatype;
	private String id = null;
	private boolean key = false;

	private String propertyName = null;

	public ListColumnDef() {
		super();
	}

	public ListColumnDef(final String id, final String propertyName, final ListColumnDataType datatype,
			final boolean key) {
		super();
		this.id = id;
		this.propertyName = propertyName;
		this.key = key;
		this.datatype = datatype;
	}

	public ListColumnDef duplicate() {
		return new ListColumnDef(id, propertyName, datatype, key);
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(final Object obj) {
		if (obj instanceof ListColumnDef) {
			return ((ListColumnDef) obj).getId().equals(getId());
		}
		return false;
	}

	@Override
	public int hashCode() {
		return getId().hashCode();
	}
	
	/**
	 * Renvoit
	 * 
	 * @return the datatype ListColumnDataType :
	 */
	public ListColumnDataType getDatatype() {
		return datatype;
	}

	/**
	 * Renvoit
	 * 
	 * @return the id String :
	 */
	public String getId() {
		return id;
	}

	/**
	 * Renvoit
	 * 
	 * @return the propertyName String :
	 */
	public String getPropertyName() {
		return propertyName;
	}

	/**
	 * Renvoit
	 * 
	 * @return the key boolean :
	 */
	public boolean isKey() {
		return key;
	}

	/**
	 * @param datatype
	 *            the datatype to set
	 */
	public void setDatatype(final ListColumnDataType datatype) {
		this.datatype = datatype;
	}

	/**
	 * @param datatype
	 *            the datatype to set
	 */
	public void setDatatypeFromText(final String stringDataType) {
		if (stringDataType.equals("string")) {
			datatype = ListColumnDataType.STRING;
		} else if (stringDataType.equals("integer")) {
			datatype = ListColumnDataType.INTEGER;
		} else if (stringDataType.equals("float")) {
			datatype = ListColumnDataType.FLOAT;
		}
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(final String name) {
		id = name;
	}

	/**
	 * @param key
	 *            the key to set
	 */
	public void setKey(final boolean key) {
		this.key = key;
	}

	/**
	 * @param propertyName
	 *            the propertyName to set
	 */
	public void setPropertyName(final String propertyName) {
		this.propertyName = propertyName;
	}

	@Override
	public String toString() {
		return MessageFormat.format("id={0} propertyname={1} key{2}", id, propertyName, key);
	}

}
