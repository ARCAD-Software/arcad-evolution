/*
 * Cr�� le 26 avr. 04
 *
 */
package com.arcadsoftware.aev.core.collections;

/**
 * @author MD
 */
public interface IArcadCollectionBasic {

	void add(IArcadCollectionItem c);

	void clear();

	void copyAndAdd(IArcadCollectionItem c);

	void copyAndInsert(int index, IArcadCollectionItem c);

	int count();

	void delete(int index);

	void insert(int index, IArcadCollectionItem c);

	IArcadCollectionItem items(int index);

	Object[] toArray();

}
