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
package com.arcadsoftware.mmk.lists.arcad.lists;

import com.arcadsoftware.mmk.lists.AbstractArcadList;
import com.arcadsoftware.mmk.lists.AbstractXmlList;
import com.arcadsoftware.mmk.lists.arcad.storeitem.ArcadStoreItemManager;
import com.arcadsoftware.mmk.lists.metadata.AbstractStoreItemManager;

public class ArcadList extends AbstractXmlList {

	@Override
	public AbstractArcadList createCloneList() {
		return new ArcadList();
	}

	@Override
	public AbstractStoreItemManager createStoreItemManager(final AbstractArcadList list) {
		return new ArcadStoreItemManager(list);
	}

}
