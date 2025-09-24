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
package com.arcadsoftware.mmk.lists.metadata;

import com.arcadsoftware.mmk.lists.AbstractArcadList;
import com.arcadsoftware.mmk.lists.managers.AbstractLoggedObject;

public abstract class AbstractStoreItemManager extends AbstractLoggedObject {

	protected AbstractArcadList list;

	public AbstractStoreItemManager() {
		super();
	}

	public AbstractStoreItemManager(final AbstractArcadList list) {
		this();
		setList(list);
	}

	public abstract void createMetadata(ListMetaDatas metadatas);

	/**
	 * @param list
	 *            the list to set
	 */
	public void setList(final AbstractArcadList list) {
		this.list = list;
		setLogger(list.getLogger());
	}

	public abstract StoreItem toStoreItem(Object object);

}
