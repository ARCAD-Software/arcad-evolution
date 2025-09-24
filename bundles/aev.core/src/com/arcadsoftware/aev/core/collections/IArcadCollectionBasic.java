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
/*
 * Cr�� le 26 avr. 04
 *
 */
package com.arcadsoftware.aev.core.collections;

/**
 * @author MD
 */
public interface IArcadCollectionBasic {

	void add(IArcadCollectionItem c);

	void clear();

	void copyAndAdd(IArcadCollectionItem c);

	void copyAndInsert(int index, IArcadCollectionItem c);

	int count();

	void delete(int index);

	void insert(int index, IArcadCollectionItem c);

	IArcadCollectionItem items(int index);

	Object[] toArray();

}
