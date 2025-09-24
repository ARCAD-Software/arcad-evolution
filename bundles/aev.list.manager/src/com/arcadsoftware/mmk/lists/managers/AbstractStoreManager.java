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

import com.arcadsoftware.ae.core.exceptions.ArcadException;
import com.arcadsoftware.mmk.lists.AbstractArcadList;

public abstract class AbstractStoreManager extends AbstractLoggedObject {

	protected AbstractFiller filler;
	protected AbstractArcadList list;

	public AbstractStoreManager(final AbstractArcadList list) {
		super();
		this.list = list;
		setLogger(list.getLogger());
	}

	protected boolean finalization() throws ArcadException {
		return true;
	}

	/**
	 * Renvoit
	 * 
	 * @return the filler AbstractPopulater :
	 */
	public AbstractFiller getFiller() {
		return filler;
	}

	protected boolean initialization() throws ArcadException {
		return true;
	}

	/**
	 * Méthode permettant l'enregistrement d'une ligne de contenu.<br>
	 * 
	 * @param item
	 *            AbstractListItem : item à enregistrer.
	 * @return boolean : Retourne <code><b>true</b></code> si l'opération s'est effectuée avec succès et
	 *         <code><b>false</b></code> sinon.
	 */
	public abstract boolean saveItem() throws ArcadException;

	/**
	 * @param filler
	 *            the filler to set
	 */
	public void setFiller(final AbstractFiller populater) {
		filler = populater;
	}

	public int store() throws ArcadException {
		if (filler != null) {
			if (initialization()) {
				try {
					return filler.populate();
				} finally {
					finalization();
				}
			}
		}
		return -1;
	}

}
