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
package com.arcadsoftware.mmk.lists.arcad.storeitem;

import com.arcadsoftware.mmk.lists.AbstractArcadList;
import com.arcadsoftware.mmk.lists.metadata.AbstractStoreItemManager;
import com.arcadsoftware.mmk.lists.metadata.ListMetaDatas;
import com.arcadsoftware.mmk.lists.metadata.StoreItem;

public class ArcadStoreItemManager extends AbstractStoreItemManager {

	public ArcadStoreItemManager(final AbstractArcadList list) {
		super(list);
	}

	@Override
	public void createMetadata(final ListMetaDatas metadatas) {

	}

	@Override
	public StoreItem toStoreItem(final Object object) {
		return null;
	}

}
