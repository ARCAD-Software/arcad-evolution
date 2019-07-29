/*
 * Cr�� le 26 avr. 04
 *
 * Pour changer le mod�le de ce fichier g�n�r�, allez � :
 * Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
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

	protected ArrayList<IArcadCollectionItem> list = new ArrayList<IArcadCollectionItem>();

	public ArcadCollection() {
		super();
	}

	@Override
	public int count() {
		return list.size();
	}

	@Override
	public void add(IArcadCollectionItem c) {
		list.add(c);
		c.setParent(this);
	}

	@Override
	public void insert(int index, IArcadCollectionItem c) {
		list.add(index, c);
		c.setParent(this);
	}

	@Override
	public void copyAndAdd(IArcadCollectionItem c) {
		this.add(c.duplicate());
	}

	@Override
	public void copyAndInsert(int index, IArcadCollectionItem c) {
		insert(index, c.duplicate());
	}

	@Override
	public IArcadCollectionItem items(int index) {
		return (IArcadCollectionItem) list.get(index);
	}

	protected void doAfterItemsRemoved(IArcadCollectionItem...items) {}
	
	@Override
	public void delete(int index) {
		IArcadCollectionItem item = list.remove(index);
		if(item != null){
			item.unsetParent(this);
		}
	}

	public void delete(IArcadCollectionItem c) {
		list.remove(c);
		c.unsetParent(this);
		doAfterItemsRemoved(c);
	}

	@Override
	public void clear() {
		for(IArcadCollectionItem item : list){
			if(item != null){
				item.unsetParent(this);
			}
		}
		list.clear();
	}

	@Override
	public int find(IArcadCollectionItem c, int startpos) {
		int size = list.size();
		for (int i = startpos; i < size; i++) {
			if (((IArcadCollectionItem) list.get(i)).equalsItem(c))
				return i;
		}
		return -1;
	}

	@Override
	public int findWithLevel(IArcadCollectionItem c, int startpos) {
		int size = list.size();
		for (int i = startpos; i < size; i++) {
			if (list.get(i).equalsWithLevel(c))
				return i;
		}
		return -1;
	}

	@Override
	public int findByInstance(IArcadCollectionItem c) {
		int size = list.size();
		for (int i = 0; i < size; i++) {
			IArcadCollectionItem item = list.get(i);
			if (item == c)
				return i;
		}
		return -1;
	}

	@Override
	public int findFirst(IArcadCollectionItem c) {
		return find(c, 0);
	}

	@Override
	public int findFirstWithLevel(IArcadCollectionItem c) {
		return findWithLevel(c, 0);
	}

	@Override
	public Object[] toArray() {
		/*
		 * int size = list.size(); Object[] aoc = new Object[size]; for (int
		 * i=0;i<size;i++) { aoc[i]=items(i); } return aoc;
		 */
		return list.toArray();
	}

	public Object[] toArray(Object[] array) {
		return list.toArray(array);
	}

	@Override
	public Object[] getChildren(IArcadCollectionItem item) {
		int index = list.indexOf(item);
		int lvl = item.getLevel();	
		ArrayList<IArcadCollectionItem> array = new ArrayList<IArcadCollectionItem>();
		if (hasChildren(item)) {
			for (int i=index+1;i<list.size();i++){
				if (items(i).getLevel()<=lvl)
					break;
				else if (items(i).getLevel()==lvl+1){
					array.add(items(i));						
				}
			}
			return array.toArray();
		}	
		else
			return new Object[0];	
	}

	@Override
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

	@Override
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

	@Override
	public boolean hasChildren(IArcadCollectionItem item) {
		int index = list.indexOf(item);
		int lvl = item.getLevel();
		if (index == (list.size() - 1))
			return false;
		return (items(index + 1).getLevel() > lvl);
	}

	public Object[] getElementsByLevel(int level) {
		ArrayList<IArcadCollectionItem> array = new ArrayList<IArcadCollectionItem>();

		for (int i=0;i<list.size();i++){
			if (items(i).getLevel()==level)
				array.add(items(i));						
		}	
		
		return array.toArray();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object clone() throws CloneNotSupportedException {
		ArcadCollection result = (ArcadCollection) super.clone();
		result.list = (ArrayList<IArcadCollectionItem>) list.clone();
		return result;
	}

	@SuppressWarnings("rawtypes")
	public ArrayList getList() {
		return list;
	}
}
