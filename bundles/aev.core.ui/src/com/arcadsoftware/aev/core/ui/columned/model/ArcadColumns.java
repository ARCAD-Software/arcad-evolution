/*
 * Created on 13 févr. 2006
 */
package com.arcadsoftware.aev.core.ui.columned.model;

import java.util.ArrayList;

import com.arcadsoftware.aev.core.ui.mementos.TableMementoTools;

/**
 * @author MD
 */
public class ArcadColumns {
	private ArrayList<ArcadColumn> list = new ArrayList<ArcadColumn>();

	/**
	 * 
	 */
	public ArcadColumns() {
		super();
	}

	public ArcadColumns(String fileName) {
		this();
		if (fileName != null) {
			load(fileName);
		}
	}

	public String[] getIdentifiers() {
		String[] result = new String[list.size()];
		for (int i = 0; i < list.size(); i++) {
			ArcadColumn c = items(i);
			result[i] = c.getIdentifier();
		}
		return result;
	}

	public int count() {
		return list.size();
	}

	public boolean add(ArcadColumn c) {
		return list.add(c);
	}

	public void delete(int index) {
		list.remove(index);
	}

	public void clear() {
		list.clear();
	}

	public String[] getUserNameValues() {
		String[] result = new String[list.size()];
		for (int i = 0; i < list.size(); i++) {
			ArcadColumn c = items(i);
			result[i] = c.getUserName();
		}
		return result;
	}

	public void swap(ArcadColumn item1, ArcadColumn item2) {
		ArcadColumn temp = new ArcadColumn();
		item2.assignTo(temp);
		item1.assignTo(item2);
		temp.assignTo(item1);
	}

	public void swap(int index1, int index2) {
		ArcadColumn item1 = items(index1);
		ArcadColumn item2 = items(index2);
		swap(item1, item2);
	}

	/**
	 * Renvoit une colonne en fonction de la valeur passée en paramétre
	 * 
	 * @param index
	 *            index de la colonne que l'on désire relire
	 * @return Colonne correspondant à l'index ou null si la valeur passée est
	 *         en dehaors des plages.
	 */
	public ArcadColumn items(int index) {
		if ((index > -1) && (index < list.size()))
			return (ArcadColumn) list.get(index);
		return null;
	}

	/**
	 * Renvoit une colonne en fonction de la valeur passée en paramétre
	 * 
	 * @param index
	 *            index de la colonne que l'on désire relire
	 * @return Colonne correspondant à l'index ou null si la valeur passée est
	 *         en dehaors des plages.
	 */
	public ArcadColumn visibleItems(int index) {
		if ((index > -1) && (index < list.size())) {
			int j = 0;
			for (int i = 0; i < list.size(); i++) {
				if (((ArcadColumn) list.get(i)).getVisible() == ArcadColumn.VISIBLE) {
					if (j == index)
						return (ArcadColumn) list.get(i);
					j++;
				}
			}
		}
		return null;
	}

	/**
	 * Renvoit une colonne en fonction de la valeur passée en paramétre
	 * 
	 * @param property
	 *            Identifiant de la colonne
	 * @return Colonne correspondant à l'identifiant ou null si la valeur passée
	 *         ne correspond à aucune colonne.
	 */
	public ArcadColumn items(String property) {
		for (int i = 0; i < list.size(); i++) {
			ArcadColumn c = items(i);
			if (c.getIdentifier().equalsIgnoreCase(property)) {
				return c;
			}
		}
		return null;
	}

	/**
	 * Renvoit une colonne en fonction de la valeur passée en paramétre
	 * 
	 * @param property
	 *            Identifiant de la colonne
	 * @return Colonne correspondant à l'identifiant ou null si la valeur passée
	 *         ne correspond à aucune colonne.
	 */
	public ArcadColumn itemsByUserNameValue(String valueName) {
		for (int i = 0; i < list.size(); i++) {
			ArcadColumn c = items(i);
			if (c.getUserName().equalsIgnoreCase(valueName)) {
				return c;
			}
		}
		return null;
	}

	/**
	 * Renvoit une colonne en fonction de la valeur de sa position dans
	 * l'ordonnancement des colonnes de références
	 * 
	 * @param positon
	 *            position d'affichage
	 * @return Colonne correspondant à l'identifiant ou null si la valeur passée
	 *         ne correspond à aucune colonne.
	 */
	public ArcadColumn itemsFromPosition(int columnIndex) {
		for (int i = 0; i < list.size(); i++) {
			ArcadColumn c = items(i);
			if (c.getPosition() == columnIndex) {
				return c;
			}
		}
		return null;
	}

	/**
	 * Renvoit une colonne en fonction de la valeur de sa position d'affichage
	 * 
	 * @param poisiton
	 *            position d'affichage
	 * @return Colonne correspondant à l'identifiant ou null si la valeur passée
	 *         ne correspond à aucune colonne.
	 */
	public ArcadColumn itemsFromActualPosition(int columnIndex) {
		for (int i = 0; i < list.size(); i++) {
			ArcadColumn c = items(i);
			if (c.getActualIndex() == columnIndex) {
				return c;
			}
		}
		return null;
	}

	/**
	 * @return Returns the list.
	 */
	public ArrayList<ArcadColumn> getList() {
		return list;
	}

	/**
	 * @param list
	 *            The list to set.
	 */
	public void setList(ArrayList<ArcadColumn> list) {
		this.list = list;
	}

	public boolean load(String FileName) {
		return TableMementoTools.getViewerDef(FileName, this);
	}

	public boolean save(String FileName) {
		return TableMementoTools.setViewerDef(FileName, this);
	}

	public ArcadColumns duplicate() {
		ArcadColumns result = new ArcadColumns();
		for (int i = 0; i < count(); i++) {
			ArcadColumn c = items(i);
			result.add(c.duplicate());
		}
		return result;
	}
}
