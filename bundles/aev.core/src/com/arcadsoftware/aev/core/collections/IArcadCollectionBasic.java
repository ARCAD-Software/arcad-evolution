/*
 * Créé le 26 avr. 04
 *
 */
package com.arcadsoftware.aev.core.collections;

/**
 * @author MD
 *
 */
public interface IArcadCollectionBasic {
	
	public int count();	
	public void add(IArcadCollectionItem c);
	public void copyAndAdd(IArcadCollectionItem c);	
	public void copyAndInsert(int index,IArcadCollectionItem c);		
	public void insert(int index,IArcadCollectionItem c);
	public IArcadCollectionItem items(int index);		
	public void delete(int index);
	public void clear();		
	public Object[] toArray();
	
}
