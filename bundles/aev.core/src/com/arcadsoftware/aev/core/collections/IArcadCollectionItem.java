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
public interface IArcadCollectionItem extends IArcadDisplayable {
	IArcadCollectionItem duplicate();

	/**
	 * Egalit� simple de l'�l�ment sous �galit� de la version de la classe Object.
	 *
	 * @param item
	 * @return boolean
	 */
	boolean equalsItem(IArcadCollectionItem item);

	boolean equalsWithLevel(IArcadCollectionItem item);

	int getLevel();

	ArcadCollection getParent();

	int getTag();

	boolean hasChildren();

	void setIconID(String iconId);

	void setLevel(int level);

	void setParent(ArcadCollection parent);

	void setTag(int tag);

	void unsetParent(ArcadCollection parent);
}
