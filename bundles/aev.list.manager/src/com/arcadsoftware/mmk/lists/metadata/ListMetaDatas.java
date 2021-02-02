package com.arcadsoftware.mmk.lists.metadata;

import java.util.ArrayList;
import java.util.HashMap;

import com.arcadsoftware.ae.core.exceptions.ArcadException;

public class ListMetaDatas {

	private final ArrayList<ListColumnDef> cols = new ArrayList<>();
	private int fixedMetadataCount = 0;
	private final HashMap<String, ListColumnDef> hashmap = new HashMap<>();
	private String id = null;
	private String version = null;

	public ListMetaDatas() {
		super();
		createFixedMetaDatas();
	}

	public boolean addColumnDef(final ListColumnDef columnDef) {
		return addColumnDef(columnDef.getId(),
				columnDef.getPropertyName(),
				columnDef.getDatatype(),
				columnDef.isKey());
	}

	public boolean addColumnDef(final String id,
			final String propertyName,
			final ListColumnDataType dataType,
			final boolean key) {
		// int position = cols.size();
		final ListColumnDef cd = new ListColumnDef(id, propertyName, dataType, key);
		hashmap.put(cd.getId(), cd);
		return cols.add(cd);
	}

	// public boolean addColumnDef(String id){
	// return addColumnDef(name, name,name,false);
	// }

	// public boolean addColumnDef(String name,
	// String label,
	// String propertyName,
	// boolean key){
	// int position = cols.size();
	// ListColumnDef cd = new ListColumnDef(position,name, label,propertyName,key);
	// hashmap.put(cd.getId(),cd);
	// return cols.add(cd);
	// }

	public void clear() {
		cols.clear();
		hashmap.clear();
	}

	public int count() {
		return cols.size();
	}

	protected void createFixedMetaDatas() {
		this.addColumnDef("status", "arcad.list.status", ListColumnDataType.STRING, false);
		fixedMetadataCount = 1;
	}

	public ListMetaDatas duplicate() {
		final ListMetaDatas result = new ListMetaDatas();
		for (int i = fixedMetadataCount; i < cols.size(); i++) {
			final ListColumnDef newcd = cols.get(i).duplicate();
			result.addColumnDef(newcd);
		}
		return result;
	}

	public boolean exists(final String id) {
		return hashmap.get(id) != null;
	}

	public ListColumnDef getColumnDefAt(final int index) {
		if (index > -1 && index < cols.size()) {
			return cols.get(index);
		}
		return null;
	}

	public ListColumnDef getColumnFromId(final String id) {
		return hashmap.get(id);
	}

	/**
	 * Renvoit
	 * 
	 * @return the fixedMetadataCount int :
	 */
	public int getFixedMetadataCount() {
		return fixedMetadataCount;
	}

	/**
	 * Renvoit
	 * 
	 * @return the id String :
	 */
	public String getId() {
		if (id == null) {
			return "";
		}
		return id;
	}

	public ListColumnDef[] getKeys() {
		final ArrayList<ListColumnDef> lkeys = new ArrayList<>();
		for (int i = 0; i < cols.size(); i++) {
			final ListColumnDef cd = cols.get(i);
			if (cd.isKey()) {
				lkeys.add(cd);
			}
		}
		final ListColumnDef[] result = new ListColumnDef[lkeys.size()];
		for (int i = 0; i < lkeys.size(); i++) {
			result[i] = lkeys.get(i);
		}
		return result;
	}

	/**
	 * Renvoit
	 * 
	 * @return the version String :
	 */
	public String getVersion() {
		if (version == null) {
			return "";
		}
		return version;
	}

	public int indexOf(final ListColumnDef cd) {
		return cols.indexOf(cd);
	}

	public int indexOf(final String id) {
		final ListColumnDef cd = getColumnFromId(id);
		return indexOf(cd);
	}

	public void print() {
		for (final ListColumnDef newcd : cols) {
			System.out.println(newcd.toString());
		}
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(final String id) {
		this.id = id;
	}

	/**
	 * @param version
	 *            the version to set
	 */
	public void setVersion(final String version) {
		this.version = version;
	}

	public void valid() throws ArcadException {
		// TODO [LM] Validation des métadatas
	}

}
