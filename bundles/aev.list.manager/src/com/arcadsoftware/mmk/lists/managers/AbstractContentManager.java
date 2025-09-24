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
package com.arcadsoftware.mmk.lists.managers;

import com.arcadsoftware.mmk.lists.AbstractArcadList;
import com.arcadsoftware.mmk.lists.IContentAction;

public abstract class AbstractContentManager extends AbstractLoggedObject
		implements IContentAction {
	protected AbstractCashManager cashManager;
	protected AbstractArcadList list;

	public AbstractContentManager(final AbstractArcadList list) {
		super();
		this.list = list;
		setLogger(list.getLogger());
	}

	/**
	 * Renvoit
	 * 
	 * @return the cashManager AbstractCashManager :
	 */
	public AbstractCashManager getCashManager() {
		return cashManager;
	}

}
