package com.arcadsoftware.aev.core.ui.viewers.columned.impl;

import com.arcadsoftware.aev.core.collections.ArcadCollection;
import com.arcadsoftware.aev.core.collections.IArcadCollectionItem;

/**
 * @author dlelong
 */
public interface IColumnedSearcherNavigator {

	ArcadCollection getAllItems();

	IArcadCollectionItem getCurrentItem();

	IArcadCollectionItem getFirstItem();

	IArcadCollectionItem getLastItem();

	IArcadCollectionItem getNextItem();

	IArcadCollectionItem getPreviousItem();

	ArcadCollection getSearchedElements();
}
