package com.arcadsoftware.aev.core.ui.viewers.columned.impl;

import com.arcadsoftware.aev.core.collections.ArcadCollection;
import com.arcadsoftware.aev.core.collections.IArcadCollectionItem;
import com.arcadsoftware.aev.core.ui.labelproviders.columned.AbstractColumnedTableLabelProvider;
import com.arcadsoftware.aev.core.ui.viewers.columned.AbstractColumnedViewer;

/**
 * @author dlelong
 */
public class ColumnedTableSearcherNavigator implements IColumnedSearcherNavigator {

	private IArcadCollectionItem currentElement = null;
	private final AbstractColumnedTableLabelProvider labelProvider;
	private final ArcadCollection searchedElements;
	private final IColumnedSearcher searcher;
	private final AbstractColumnedViewer viewer;

	public ColumnedTableSearcherNavigator(final AbstractColumnedViewer viewer) {
		super();
		this.viewer = viewer;
		searcher = viewer.getSearcher();
		labelProvider = (AbstractColumnedTableLabelProvider) viewer.createLabelProvider(viewer);
		searchedElements = getSearchedElements();
	}

	@Override
	public ArcadCollection getAllItems() {
		currentElement = searchedElements.items(searchedElements.count() - 1);
		return searchedElements;
	}

	private int getCurrentElementIndex() {
		return searchedElements.findFirst(currentElement);
	}

	@Override
	public IArcadCollectionItem getCurrentItem() {
		return currentElement;
	}

	@Override
	public IArcadCollectionItem getFirstItem() {
		currentElement = searchedElements.items(0);
		return currentElement;
	}

	@Override
	public IArcadCollectionItem getLastItem() {
		currentElement = searchedElements.items(searchedElements.count() - 1);
		return currentElement;
	}

	@Override
	public IArcadCollectionItem getNextItem() {
		currentElement = searchedElements.items(getCurrentElementIndex() + 1);
		return currentElement;
	}

	@Override
	public IArcadCollectionItem getPreviousItem() {
		currentElement = searchedElements.items(getCurrentElementIndex() - 1);
		return currentElement;
	}

	@Override
	public ArcadCollection getSearchedElements() {
		final ArcadCollection result = new ArcadCollection();
		final ArcadCollection elements = (ArcadCollection) viewer.getInput();
		for (int i = 0; i < elements.count(); i++) {
			if (searcher.match(elements.items(i), labelProvider)) {
				result.add(elements.items(i));
			}
		}
		return result;
	}
}
