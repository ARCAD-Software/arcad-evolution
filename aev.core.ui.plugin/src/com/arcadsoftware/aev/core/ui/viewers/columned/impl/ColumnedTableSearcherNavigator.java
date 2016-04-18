package com.arcadsoftware.aev.core.ui.viewers.columned.impl;

import com.arcadsoftware.aev.core.collections.ArcadCollection;
import com.arcadsoftware.aev.core.collections.IArcadCollectionItem;
import com.arcadsoftware.aev.core.ui.labelproviders.columned.AbstractColumnedTableLabelProvider;
import com.arcadsoftware.aev.core.ui.viewers.columned.AbstractColumnedViewer;

/**
 * @author dlelong
 */
public class ColumnedTableSearcherNavigator implements IColumnedSearcherNavigator {

	private AbstractColumnedViewer viewer;
	private IColumnedSearcher searcher;
	private AbstractColumnedTableLabelProvider labelProvider;
	private ArcadCollection searchedElements;
	private IArcadCollectionItem currentElement = null;

	public ColumnedTableSearcherNavigator(AbstractColumnedViewer viewer) {
		super();
		this.viewer = viewer;
		this.searcher = viewer.getSearcher();
		this.labelProvider = (AbstractColumnedTableLabelProvider) viewer.createLabelProvider(viewer);
		this.searchedElements = getSearchedElements();
	}

	public ArcadCollection getSearchedElements() {
		ArcadCollection result = new ArcadCollection();
		ArcadCollection elements = (ArcadCollection) viewer.getInput();
		for (int i = 0; i < elements.count(); i++)
			if (searcher.match(elements.items(i), labelProvider))
				result.add(elements.items(i));
		return result;
	}

	public IArcadCollectionItem getFirstItem() {
		currentElement = searchedElements.items(0);
		return currentElement;
	}

	public ArcadCollection getAllItems() {
		currentElement = searchedElements.items(searchedElements.count() - 1);
		return searchedElements;
	}

	public IArcadCollectionItem getLastItem() {
		currentElement = searchedElements.items(searchedElements.count() - 1);
		return currentElement;
	}

	public IArcadCollectionItem getCurrentItem() {
		return currentElement;
	}

	public IArcadCollectionItem getPreviousItem() {
		currentElement = searchedElements.items(getCurrentElementIndex() - 1);
		return currentElement;
	}

	public IArcadCollectionItem getNextItem() {
		currentElement = searchedElements.items(getCurrentElementIndex() + 1);
		return currentElement;
	}

	private int getCurrentElementIndex() {
		return searchedElements.findFirst(currentElement);
	}
}
