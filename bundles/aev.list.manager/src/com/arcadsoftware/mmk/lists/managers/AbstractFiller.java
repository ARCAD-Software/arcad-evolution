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
import com.arcadsoftware.mmk.lists.metadata.StoreItem;

/**
 * Classe abstraite de gestion du remplissage de liste.<br>
 * <div> <div class="intro">Généralités</div> Cette classe permet de définir une méthode de remplissage d'une liste par
 * implémentation la méthode {@link #fill() fill()}.<br>
 * L'ajout d'un élément dans la liste s'effctue en deux temps : <br>
 * - L'affectation des valeurs à l'objet <code>storeItem</code> de la liste,<br>
 * - L'appel à la méthode <code>addItem</code>.<br>
 * Pour affecter des valeurs à l'objet <code>storeItem</code>, vous pouvez utiliser deux méthodes : <br>
 * - L'utilisation de la méthode {@link AbstractList#toStoreItem(Object)() toStoreItem()}
 * <code>toStoreItem(Object element)</code> qui permet de transférer les données d'un objet quelconque vers un objet de
 * type <code>storeItem</code>, - L'affectation directe via {@link AbstractArcadList#getStoreItem()
 * AbstractList#getStoreItem()}
 *
 * @author MD
 */
public abstract class AbstractFiller extends AbstractLoggedObject {

	boolean checkIfExists;
	private boolean fillMode = true;
	private AbstractArcadList list;
	boolean replaceIfExists;

	public AbstractFiller() {
		super();
	}

	public AbstractFiller(final AbstractArcadList list) {
		setList(list);
	}

	public abstract int fill();

	/**
	 * Renvoit la liste à remplir par le filler.
	 * 
	 * @return the list AbstractList :
	 */
	public AbstractArcadList getList() {
		return list;
	}

	public int populate() {
		if (getList() != null) {
			if (!fillMode) {
				getList().getContentManager().getCashManager().setFlushImmediat(false);
			}
			final int result = fill();
			if (!fillMode) {
				getList().getContentManager().getCashManager().setFlushImmediat(true);
				getList().getContentManager().getCashManager().flushRequest();
			}
			return result;
		}
		return -1;
	}

	public int saveItem(final StoreItem item) {
		int count = 0;
		if (getList() != null) {

			if (fillMode) {
				getList().setStoreItem(item);
				getList().saveItem();
				count = 1;
			} else {
				count = getList().addItems(item, checkIfExists, replaceIfExists);
				getList().getContentManager().getCashManager().setFlushImmediat(true);
			}
		}
		return count;
	}

	/**
	 * @param list
	 *            the list to set
	 */
	public void setList(final AbstractArcadList list) {
		this.list = list;
		setLogger(list.getLogger());
		if (list.getStoreItem().getMetadatas() == null) {
			list.initStoreItem();
		}
	}

	public void toogleToAddMode(final boolean checkIfExists, final boolean replaceIfExists) {
		fillMode = false;
		this.checkIfExists = checkIfExists;
		this.replaceIfExists = replaceIfExists;
	}

	public void toogleToFillMode() {
		fillMode = true;
	}

}
