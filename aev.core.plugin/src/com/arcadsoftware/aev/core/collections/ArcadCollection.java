/*
 * Créé le 26 avr. 04
 *
 * Pour changer le modèle de ce fichier généré, allez à :
 * Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
package com.arcadsoftware.aev.core.collections;

import java.util.ArrayList;

/**
 * @author MD
 */
public class ArcadCollection implements IArcadCollectionBasic, IArcadCollectionFinder, IArcadCollectionHierachic,
		Cloneable {

	public static int SORT_ASCENDING = 0;
	public static int SORT_DESCENDING = 1;

	@SuppressWarnings("unchecked")
	private ArrayList list = new ArrayList();

	public ArcadCollection() {
		super();
	}

	@SuppressWarnings("unchecked")
	protected void initList() {
		list = new ArrayList();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.arcadsoftware.aev.core.collections.IArcadCollection#count()
	 */
	public int count() {
		return list.size();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.arcadsoftware.aev.core.collections.IArcadCollection#add(com.arcadsoftware
	 * .aev.core.collections.IArcadCollectionItem)
	 */
	@SuppressWarnings("unchecked")
	public void add(IArcadCollectionItem c) {
		c.setParent(this);
		list.add(c);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.arcadsoftware.aev.core.collections.IArcadCollection#insert(int,
	 * com.arcadsoftware.aev.core.collections.IArcadCollectionItem)
	 */
	@SuppressWarnings("unchecked")
	public void insert(int index, IArcadCollectionItem c) {
		c.setParent(this);
		list.add(index, c);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.arcadsoftware.aev.core.collections.IArcadCollection#copyAndAdd(com
	 * .arcadsoftware.aev.core.collections.IArcadCollectionItem)
	 */
	public void copyAndAdd(IArcadCollectionItem c) {
		this.add(c.duplicate());

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.arcadsoftware.aev.core.collections.IArcadCollection#copyAndInsert
	 * (int, com.arcadsoftware.aev.core.collections.IArcadCollectionItem)
	 */
	public void copyAndInsert(int index, IArcadCollectionItem c) {
		this.insert(index, c.duplicate());

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.arcadsoftware.aev.core.collections.IArcadCollection#items(int)
	 */
	public IArcadCollectionItem items(int index) {
		return (IArcadCollectionItem) list.get(index);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.arcadsoftware.aev.core.collections.IArcadCollection#delete(int)
	 */
	public void delete(int index) {
		list.remove(index);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.arcadsoftware.aev.core.collections.IArcadCollection#delete(int)
	 */
	public void delete(IArcadCollectionItem c) {
		list.remove(c);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.arcadsoftware.aev.core.collections.IArcadCollection#clear()
	 */
	public void clear() {
		list.clear();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.arcadsoftware.aev.core.collections.IArcadCollection#find(com.
	 * arcadsoftware.aev.core.collections.IArcadCollectionItem, int)
	 */
	public int find(IArcadCollectionItem c, int startpos) {
		int size = list.size();
		for (int i = startpos; i < size; i++) {
			if (((IArcadCollectionItem) list.get(i)).equalsItem(c))
				return i;
		}
		return -1;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.arcadsoftware.aev.core.collections.IArcadCollection#find(com.
	 * arcadsoftware.aev.core.collections.IArcadCollectionItem, int)
	 */
	// --------------------------------------------------------------------------
	public int findWithLevel(IArcadCollectionItem c, int startpos) {
		int size = list.size();
		for (int i = startpos; i < size; i++) {
			if (((IArcadCollectionItem) list.get(i)).equalsWithLevel(c))
				return i;
		}
		return -1;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.arcadsoftware.aev.core.collections.IArcadCollection#find(com.
	 * arcadsoftware.aev.core.collections.IArcadCollectionItem, int)
	 */
	// --------------------------------------------------------------------------
	public int findByInstance(IArcadCollectionItem c) {
		int size = list.size();
		for (int i = 0; i < size; i++) {
			IArcadCollectionItem item = (IArcadCollectionItem) list.get(i);
			if (item == c)
				return i;
		}
		return -1;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.arcadsoftware.aev.core.collections.IArcadCollection#findFirst(com
	 * .arcadsoftware.aev.core.collections.IArcadCollectionItem)
	 */
	public int findFirst(IArcadCollectionItem c) {
		return find(c, 0);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.arcadsoftware.aev.core.collections.IArcadCollection#find(com.
	 * arcadsoftware.aev.core.collections.IArcadCollectionItem, int)
	 */
	public int findFirstWithLevel(IArcadCollectionItem c) {
		return findWithLevel(c, 0);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.arcadsoftware.aev.core.collections.IArcadCollection#toArray()
	 */
	public Object[] toArray() {
		/*
		 * int size = list.size(); Object[] aoc = new Object[size]; for (int
		 * i=0;i<size;i++) { aoc[i]=items(i); } return aoc;
		 */
		return list.toArray();
	}

	@SuppressWarnings("unchecked")
	public Object[] toArray(Object[] array) {
		return list.toArray(array);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.arcadsoftware.aev.core.collections.IArcadCollection#getChildren(com
	 * .arcadsoftware.aev.core.collections.IArcadCollectionItem)
	 */
	@SuppressWarnings("unchecked")
	public Object[] getChildren(IArcadCollectionItem item) {
		int index = list.indexOf(item);
		int lvl = item.getLevel();
		ArrayList a = new ArrayList();
		if (hasChildren(item)) {
			for (int i = index + 1; i < list.size(); i++) {
				if (items(i).getLevel() <= lvl)
					break;
				else if (items(i).getLevel() == lvl + 1) {
					a.add(items(i));
				}
			}
			Object[] o = new Object[a.size()];
			for (int i = 0; i < a.size(); i++) {
				o[i] = a.get(i);
			}
			return o;
		}
		return new Object[0];
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.arcadsoftware.aev.core.collections.IArcadCollection#getChildren(com
	 * .arcadsoftware.aev.core.collections.IArcadCollectionItem)
	 */
	public void removeBranch(IArcadCollectionItem item, boolean removeParent) {
		int index = findByInstance(item);
		if (index > -1) {
			int lvl = item.getLevel();

			int lastIndex = index;
			boolean hasChildren = (index < list.size() - 1) && (items(index + 1).getLevel() > lvl);
			if (hasChildren) {

				for (int i = index + 1; i < list.size(); i++) {
					if (items(i).getLevel() <= lvl)
						break;
					lastIndex = i;
				}
			}
			if ((index == lastIndex) && !removeParent)
				return;
			if (!removeParent)
				index++;
			for (int i = lastIndex; i >= index; i--) {
				list.remove(i);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.arcadsoftware.aev.core.collections.IArcadCollection#getParent(com
	 * .arcadsoftware.aev.core.collections.IArcadCollectionItem)
	 */
	public IArcadCollectionItem getParent(IArcadCollectionItem item) {
		int index = list.indexOf(item);
		int lvl = item.getLevel();
		if (index == 0)
			return null;
		for (int i = index; i >= 0; i--) {
			if (items(i).getLevel() == lvl - 1)
				return items(i);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.arcadsoftware.aev.core.collections.IArcadCollection#hasChildren(com
	 * .arcadsoftware.aev.core.collections.IArcadCollectionItem)
	 */
	public boolean hasChildren(IArcadCollectionItem item) {
		int index = list.indexOf(item);
		int lvl = item.getLevel();
		if (index == (list.size() - 1))
			return false;
		return (items(index + 1).getLevel() > lvl);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.arcadsoftware.aev.core.collections.IArcadCollection#hasChildren(com
	 * .arcadsoftware.aev.core.collections.IArcadCollectionItem)
	 */
	@SuppressWarnings("unchecked")
	public Object[] getElementsByLevel(int level) {
		ArrayList a = new ArrayList();

		for (int i = 0; i < list.size(); i++) {
			if (items(i).getLevel() == level)
				a.add(items(i));
		}
		Object[] o = new Object[a.size()];
		for (int i = 0; i < a.size(); i++) {
			o[i] = a.get(i);
		}
		return o;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#clone()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Object clone() throws CloneNotSupportedException {
		ArcadCollection result = (ArcadCollection) super.clone();
		result.list = (ArrayList) list.clone();
		return result;
	}

	@SuppressWarnings("unchecked")
	public ArrayList getList() {
		return list;
	}
}
