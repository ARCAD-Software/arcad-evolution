/*******************************************************************************
 * Copyright (c) 2025 ARCAD Software.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     ARCAD Software - initial API and implementation
 *******************************************************************************/
package com.arcadsoftware.mmk.lists.metadata;

import java.util.ArrayList;

public class StoreItem {

	public static final String STATUS_HOLD = "H";
	public static final String STATUS_NOK = "1";
	public static final String STATUS_NONE = "";
	// TODO [LM] définir les status un peu mieux que ca!!!
	public static final String STATUS_OK = "0";

	private ListMetaDatas metadatas;
	private int rowId = -1;
	private String[] values;

	public StoreItem() {
		super();
	}

	public StoreItem(final ListMetaDatas metadata) {
		super();
		setMetadatas(metadata);
	}

	public String getKey() {
		final StringBuilder key = new StringBuilder();
		final int count = metadatas.count();
		for (int i = 0; i < count; i++) {
			final ListColumnDef cd = metadatas.getColumnDefAt(i);
			if (cd.isKey()) {
				key.append(values[i]).append((char) 1);
			}
		}
		return key.toString();
	}

	/**
	 * Renvoit
	 * 
	 * @return the values String[] :
	 */
	public String[] getKeyValues() {
		final ArrayList<String> l = new ArrayList<>();
		for (int i = 0; i < metadatas.count(); i++) {
			final ListColumnDef cd = metadatas.getColumnDefAt(i);
			if (cd.isKey()) {
				final String value = getValue(i);
				l.add(value);
			}
		}
		final String[] a = new String[l.size()];
		for (int i = 0; i < l.size(); i++) {
			a[i] = l.get(i);
		}
		return a;
	}

	/**
	 * Renvoit
	 * 
	 * @return the metadatas ListMetaDatas :
	 */
	public ListMetaDatas getMetadatas() {
		return metadatas;
	}

	/**
	 * Renvoit
	 * 
	 * @return the rowId int :
	 */
	public int getRowId() {
		return rowId;
	}

	/**
	 * Renvoit
	 * 
	 * @return the values String[] :
	 */
	public String[] getUpdateValues() {
		final ArrayList<String> l = new ArrayList<>();
		for (int i = 0; i < metadatas.count(); i++) {
			final ListColumnDef cd = metadatas.getColumnDefAt(i);
			if (!cd.isKey()) {
				final String value = getValue(i);
				l.add(value);
			}
		}
		for (int i = 0; i < metadatas.count(); i++) {
			final ListColumnDef cd = metadatas.getColumnDefAt(i);
			if (cd.isKey()) {
				final String value = getValue(i);
				l.add(value);
			}
		}
		final String[] a = new String[l.size()];
		for (int i = 0; i < l.size(); i++) {
			a[i] = l.get(i);
		}

		return a;
	}

	public String getUserValue(final int columnIndex) {
		return values[columnIndex + metadatas.getFixedMetadataCount()];
	}

	public String getValue(final int columnIndex) {
		String result = null;
		if (columnIndex < values.length) {
			result = values[columnIndex];
		}
		if (result == null) {
			return "";
		}
		return result;
	}

	public String getValue(final String id) {
		final int columnIndex = metadatas.indexOf(id);
		return getValue(columnIndex);
	}

	/**
	 * Renvoit
	 * 
	 * @return the values String[] :
	 */
	public String[] getValues() {
		return values;
	}

	/**
	 * @param metadatas
	 *            the metadatas to set
	 */
	public void setMetadatas(final ListMetaDatas metadata) {
		metadatas = metadata;
		values = new String[metadata.count()];
	}

	/**
	 * @param rowId
	 *            the rowId to set
	 */
	public void setRowId(final int rowId) {
		this.rowId = rowId;
	}

	public void setUserValue(final int columnIndex, final String value) {
		values[columnIndex + metadatas.getFixedMetadataCount()] = value;
	}

	public void setUserValue(final String[] stringValues) {
		for (int i = 0; i < stringValues.length; i++) {
			values[i + metadatas.getFixedMetadataCount()] = stringValues[i];
		}
	}

	public void setValue(final int columnIndex, final String value) {
		values[columnIndex] = value;
	}

	public void setValue(final String id, final String value) {
		final int columnIndex = metadatas.indexOf(id);
		values[columnIndex] = value;
	}

}
