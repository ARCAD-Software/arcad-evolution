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
package com.arcadsoftware.mmk.lists.ext;

import java.io.File;

import com.arcadsoftware.mmk.lists.metadata.AbstractStoreItemManager;
import com.arcadsoftware.mmk.lists.metadata.ListColumnDataType;
import com.arcadsoftware.mmk.lists.metadata.ListMetaDatas;
import com.arcadsoftware.mmk.lists.metadata.StoreItem;

public class TestStoreItemManager extends AbstractStoreItemManager {

	@Override
	public void createMetadata(final ListMetaDatas metadatas) {
		metadatas.setId("");
		metadatas.setVersion("");
		metadatas.addColumnDef("value", "arcad.list.text", ListColumnDataType.STRING, true);
	}

	@Override
	public StoreItem toStoreItem(final Object object) {
		if (object instanceof File) {
			final StoreItem item = new StoreItem(list.getMetadatas());
			item.setUserValue(
					new String[] { ((File) object).getAbsolutePath() });
			return item;
		}
		return null;
	}

}
