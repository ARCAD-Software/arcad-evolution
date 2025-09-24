/*******************************************************************************
 * Copyright (c) 2025 ARCAD Software.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     ARCAD Software - initial API and implementation
 *******************************************************************************/
package com.arcadsoftware.mmk.lists.impl.fillers;

import com.arcadsoftware.mmk.lists.AbstractArcadList;
import com.arcadsoftware.mmk.lists.IListBrowseListener;
import com.arcadsoftware.mmk.lists.managers.AbstractFiller;
import com.arcadsoftware.mmk.lists.metadata.StoreItem;

public class ListToListFiller extends AbstractFiller implements
		IListBrowseListener {

	private final AbstractArcadList fromList;

	public ListToListFiller(final AbstractArcadList fromList,
			final AbstractArcadList toList) {
		this(fromList, toList, null);
	}

	public ListToListFiller(final AbstractArcadList fromList,
			final AbstractArcadList toList,
			final String extractionQuery) {
		super(toList);
		this.fromList = fromList;
		// this.extractionQuery = extractionQuery;
	}

	@Override
	public void elementBrowsed(final StoreItem item) {
		saveItem(item);
	}

	@Override
	public int fill() {
		fromList.addBrowseListener(this);
		try {
			fillBefore();
			final int count = fromList.browse();
			fillAfter();
			return count;
		} finally {
			fromList.removeBrowseListener(this);
		}
	}

	protected void fillAfter() {
	}

	protected void fillBefore() {
	}

}
