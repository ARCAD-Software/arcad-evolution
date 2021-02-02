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
	private ArrayList<ArcadColumn> list = new ArrayList<>();

	/**
	 *
	 */
	public ArcadColumns() {
		super();
	}

	public ArcadColumns(final String fileName) {
		this();
		if (fileName != null) {
			load(fileName);
		}
	}

	public boolean add(final ArcadColumn c) {
		return list.add(c);
	}

	public void clear() {
		list.clear();
	}

	public int count() {
		return list.size();
	}

	public void delete(final int index) {
		list.remove(index);
	}

	public ArcadColumns duplicate() {
		final ArcadColumns result = new ArcadColumns();
		for (int i = 0; i < count(); i++) {
			final ArcadColumn c = items(i);
			result.add(c.duplicate());
		}
		return result;
	}

	public String[] getIdentifiers() {
		final String[] result = new String[list.size()];
		for (int i = 0; i < list.size(); i++) {
			final ArcadColumn c = items(i);
			result[i] = c.getIdentifier();
		}
		return result;
	}

	/**
	 * @return Returns the list.
	 */
	public ArrayList<ArcadColumn> getList() {
		return list;
	}

	public String[] getUserNameValues() {
		final String[] result = new String[list.size()];
		for (int i = 0; i < list.size(); i++) {
			final ArcadColumn c = items(i);
			result[i] = c.getUserName();
		}
		return result;
	}

	/**
	 * Renvoit une colonne en fonction de la valeur passée en paramétre
	 *
	 * @param index
	 *            index de la colonne que l'on désire relire
	 * @return Colonne correspondant à l'index ou null si la valeur passée est en dehaors des plages.
	 */
	public ArcadColumn items(final int index) {
		if (index > -1 && index < list.size()) {
			return list.get(index);
		}
		return null;
	}

	/**
	 * Renvoit une colonne en fonction de la valeur passée en paramétre
	 *
	 * @param property
	 *            Identifiant de la colonne
	 * @return Colonne correspondant à l'identifiant ou null si la valeur passée ne correspond à aucune colonne.
	 */
	public ArcadColumn items(final String property) {
		for (int i = 0; i < list.size(); i++) {
			final ArcadColumn c = items(i);
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
	 * @return Colonne correspondant à l'identifiant ou null si la valeur passée ne correspond à aucune colonne.
	 */
	public ArcadColumn itemsByUserNameValue(final String valueName) {
		for (int i = 0; i < list.size(); i++) {
			final ArcadColumn c = items(i);
			if (c.getUserName().equalsIgnoreCase(valueName)) {
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
	 * @return Colonne correspondant à l'identifiant ou null si la valeur passée ne correspond à aucune colonne.
	 */
	public ArcadColumn itemsFromActualPosition(final int columnIndex) {
		for (int i = 0; i < list.size(); i++) {
			final ArcadColumn c = items(i);
			if (c.getActualIndex() == columnIndex) {
				return c;
			}
		}
		return null;
	}

	/**
	 * Renvoit une colonne en fonction de la valeur de sa position dans l'ordonnancement des colonnes de références
	 *
	 * @param positon
	 *            position d'affichage
	 * @return Colonne correspondant à l'identifiant ou null si la valeur passée ne correspond à aucune colonne.
	 */
	public ArcadColumn itemsFromPosition(final int columnIndex) {
		for (int i = 0; i < list.size(); i++) {
			final ArcadColumn c = items(i);
			if (c.getPosition() == columnIndex) {
				return c;
			}
		}
		return null;
	}

	public boolean load(final String FileName) {
		return TableMementoTools.getViewerDef(FileName, this);
	}

	public boolean save(final String FileName) {
		return TableMementoTools.setViewerDef(FileName, this);
	}

	/**
	 * @param list
	 *            The list to set.
	 */
	public void setList(final ArrayList<ArcadColumn> list) {
		this.list = list;
	}

	public void swap(final ArcadColumn item1, final ArcadColumn item2) {
		final ArcadColumn temp = new ArcadColumn();
		item2.assignTo(temp);
		item1.assignTo(item2);
		temp.assignTo(item1);
	}

	public void swap(final int index1, final int index2) {
		final ArcadColumn item1 = items(index1);
		final ArcadColumn item2 = items(index2);
		swap(item1, item2);
	}

	/**
	 * Renvoit une colonne en fonction de la valeur passée en paramétre
	 *
	 * @param index
	 *            index de la colonne que l'on désire relire
	 * @return Colonne correspondant à l'index ou null si la valeur passée est en dehaors des plages.
	 */
	public ArcadColumn visibleItems(final int index) {
		if (index > -1 && index < list.size()) {
			int j = 0;
			for (final ArcadColumn element : list) {
				if (element.getVisible() == ArcadColumn.VISIBLE) {
					if (j == index) {
						return element;
					}
					j++;
				}
			}
		}
		return null;
	}
}
