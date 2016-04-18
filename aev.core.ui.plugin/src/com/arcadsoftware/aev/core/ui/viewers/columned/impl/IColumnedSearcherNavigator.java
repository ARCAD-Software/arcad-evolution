package com.arcadsoftware.aev.core.ui.viewers.columned.impl;

import com.arcadsoftware.aev.core.collections.ArcadCollection;
import com.arcadsoftware.aev.core.collections.IArcadCollectionItem;

/**
 * @author dlelong
 */
public interface IColumnedSearcherNavigator {
	
	ArcadCollection getSearchedElements();
	IArcadCollectionItem getFirstItem();
	ArcadCollection getAllItems();
	IArcadCollectionItem getLastItem();
	IArcadCollectionItem getCurrentItem();
	IArcadCollectionItem getPreviousItem();
	IArcadCollectionItem getNextItem();
}
