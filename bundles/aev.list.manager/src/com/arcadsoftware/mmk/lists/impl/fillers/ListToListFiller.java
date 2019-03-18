package com.arcadsoftware.mmk.lists.impl.fillers;



import com.arcadsoftware.mmk.lists.AbstractList;
import com.arcadsoftware.mmk.lists.IListBrowseListener;
import com.arcadsoftware.mmk.lists.managers.AbstractFiller;
import com.arcadsoftware.mmk.lists.metadata.StoreItem;

public class ListToListFiller extends AbstractFiller implements
		IListBrowseListener {

	private AbstractList fromList;
	
	public ListToListFiller(AbstractList fromList,
			              AbstractList toList) {
		this(fromList,toList,null);
	}
	
	public ListToListFiller(AbstractList fromList,
			              AbstractList toList,
			              String extractionQuery) {
		super(toList);
		this.fromList = fromList;
		//this.extractionQuery = extractionQuery;
	}

	@Override
	public int fill() {
		fromList.addBrowseListener(this);
		try{
			fillBefore();
			int count = fromList.browse();
			fillAfter();
			return count;
		} finally {
			fromList.removeBrowseListener(this);
		}
	}

	public void elementBrowsed(StoreItem item) {
		saveItem(item);
	}

	protected void fillBefore(){};
	protected void fillAfter(){};	
	
}
