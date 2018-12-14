package com.arcadsoftware.mmk.lists.impl.fillers;

import com.arcadsoftware.mmk.lists.AbstractList;

public class ExtractListFiller extends ListToListFiller {

	public ExtractListFiller(AbstractList fromList, 
			                 AbstractList toList, 
			                 String extractionQuery) {
		super(fromList, toList, extractionQuery);
	}

}
