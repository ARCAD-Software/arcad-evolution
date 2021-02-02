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

	protected ArrayList<IArcadCollectionItem> list = new ArrayList<>();

	public ArcadCollection() {
		super();
	}

	@Override
	public void add(final IArcadCollectionItem c) {
		list.add(c);
		c.setParent(this);
	}

	@Override
	public void clear() {
		for (final IArcadCollectionItem item : list) {
			if (item != null) {
				item.unsetParent(this);
			}
		}
		list.clear();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object clone() throws CloneNotSupportedException {
		final ArcadCollection result = (ArcadCollection) super.clone();
		result.list = (ArrayList<IArcadCollectionItem>) list.clone();
		return result;
	}

	@Override
	public void copyAndAdd(final IArcadCollectionItem c) {
		add(c.duplicate());
	}

	@Override
	public void copyAndInsert(final int index, final IArcadCollectionItem c) {
		insert(index, c.duplicate());
	}

	@Override
	public int count() {
		return list.size();
	}

	public void delete(final IArcadCollectionItem c) {
		list.remove(c);
		c.unsetParent(this);
		doAfterItemsRemoved(c);
	}

	@Override
	public void delete(final int index) {
		final IArcadCollectionItem item = list.remove(index);
		if (item != null) {
			item.unsetParent(this);
		}
	}

	protected void doAfterItemsRemoved(final IArcadCollectionItem... items) {
	}

	@Override
	public int find(final IArcadCollectionItem c, final int startpos) {
		final int size = list.size();
		for (int i = startpos; i < size; i++) {
			if (list.get(i).equalsItem(c)) {
				return i;
			}
		}
		return -1;
	}

	@Override
	public int findByInstance(final IArcadCollectionItem c) {
		final int size = list.size();
		for (int i = 0; i < size; i++) {
			final IArcadCollectionItem item = list.get(i);
			if (item == c) {
				return i;
			}
		}
		return -1;
	}

	@Override
	public int findFirst(final IArcadCollectionItem c) {
		return find(c, 0);
	}

	@Override
	public int findFirstWithLevel(final IArcadCollectionItem c) {
		return findWithLevel(c, 0);
	}

	@Override
	public int findWithLevel(final IArcadCollectionItem c, final int startpos) {
		final int size = list.size();
		for (int i = startpos; i < size; i++) {
			if (list.get(i).equalsWithLevel(c)) {
				return i;
			}
		}
		return -1;
	}

	@Override
	public Object[] getChildren(final IArcadCollectionItem item) {
		final int index = list.indexOf(item);
		final int lvl = item.getLevel();
		final ArrayList<IArcadCollectionItem> array = new ArrayList<>();
		if (hasChildren(item)) {
			for (int i = index + 1; i < list.size(); i++) {
				if (items(i).getLevel() <= lvl) {
					break;
				} else if (items(i).getLevel() == lvl + 1) {
					array.add(items(i));
				}
			}
			return array.toArray();
		} else {
			return new Object[0];
		}
	}

	public Object[] getElementsByLevel(final int level) {
		final ArrayList<IArcadCollectionItem> array = new ArrayList<>();

		for (int i = 0; i < list.size(); i++) {
			if (items(i).getLevel() == level) {
				array.add(items(i));
			}
		}

		return array.toArray();
	}

	@SuppressWarnings("rawtypes")
	public ArrayList getList() {
		return list;
	}

	@Override
	public IArcadCollectionItem getParent(final IArcadCollectionItem item) {
		final int index = list.indexOf(item);
		final int lvl = item.getLevel();
		if (index == 0) {
			return null;
		}
		for (int i = index; i >= 0; i--) {
			if (items(i).getLevel() == lvl - 1) {
				return items(i);
			}
		}
		return null;
	}

	@Override
	public boolean hasChildren(final IArcadCollectionItem item) {
		final int index = list.indexOf(item);
		final int lvl = item.getLevel();
		if (index == list.size() - 1) {
			return false;
		}
		return items(index + 1).getLevel() > lvl;
	}

	@Override
	public void insert(final int index, final IArcadCollectionItem c) {
		list.add(index, c);
		c.setParent(this);
	}

	@Override
	public IArcadCollectionItem items(final int index) {
		return list.get(index);
	}

	@Override
	public void removeBranch(final IArcadCollectionItem item, final boolean removeParent) {
		int index = findByInstance(item);
		if (index > -1) {
			final int lvl = item.getLevel();

			int lastIndex = index;
			final boolean hasChildren = index < list.size() - 1 && items(index + 1).getLevel() > lvl;
			if (hasChildren) {

				for (int i = index + 1; i < list.size(); i++) {
					if (items(i).getLevel() <= lvl) {
						break;
					}
					lastIndex = i;
				}
			}
			if (index == lastIndex && !removeParent) {
				return;
			}
			if (!removeParent) {
				index++;
			}
			for (int i = lastIndex; i >= index; i--) {
				list.remove(i);
			}
		}
	}

	@Override
	public Object[] toArray() {
		/*
		 * int size = list.size(); Object[] aoc = new Object[size]; for (int i=0;i<size;i++) { aoc[i]=items(i); } return
		 * aoc;
		 */
		return list.toArray();
	}

	public Object[] toArray(final Object[] array) {
		return list.toArray(array);
	}
}
