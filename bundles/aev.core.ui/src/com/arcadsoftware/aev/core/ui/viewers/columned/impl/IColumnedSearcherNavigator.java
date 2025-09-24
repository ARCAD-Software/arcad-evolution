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
package com.arcadsoftware.aev.core.ui.viewers.columned.impl;

import com.arcadsoftware.aev.core.collections.ArcadCollection;
import com.arcadsoftware.aev.core.collections.IArcadCollectionItem;

/**
 * @author dlelong
 */
public interface IColumnedSearcherNavigator {

	ArcadCollection getAllItems();

	IArcadCollectionItem getCurrentItem();

	IArcadCollectionItem getFirstItem();

	IArcadCollectionItem getLastItem();

	IArcadCollectionItem getNextItem();

	IArcadCollectionItem getPreviousItem();

	ArcadCollection getSearchedElements();
}
