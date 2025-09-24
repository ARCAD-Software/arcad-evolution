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
 * Created on 12 avr. 2006
 *
 */
package com.arcadsoftware.aev.core.ui.actions;

/**
 * Classe des actions n'agissant que sur une seule entit� ARCAD par envoi d'une commande sur le serveur iSeries.<br>
 * 
 * @author MD
 */
public abstract class AbstractSimpleItemAction extends ArcadAction {
	protected ArcadActions containerActions = null;

	/**
	 * Constructeur
	 */
	public AbstractSimpleItemAction() {
		super();
	}

	/**
	 * Constructeur
	 * 
	 * @param containerActions
	 */
	public AbstractSimpleItemAction(final ArcadActions containerActions) {
		super();
		this.containerActions = containerActions;
	}

}
