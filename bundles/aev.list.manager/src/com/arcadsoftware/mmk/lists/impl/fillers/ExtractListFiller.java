package com.arcadsoftware.mmk.lists.impl.fillers;

import com.arcadsoftware.mmk.lists.AbstractArcadList;

public class ExtractListFiller extends ListToListFiller {

	public ExtractListFiller(final AbstractArcadList fromList,
			final AbstractArcadList toList,
			final String extractionQuery) {
		super(fromList, toList, extractionQuery);
	}

}
