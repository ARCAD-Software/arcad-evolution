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

public abstract class AbstractCashManager extends AbstractLoggedObject {
	protected boolean active = false;

	private boolean flushImmediat = true;
	protected AbstractArcadList list;

	public AbstractCashManager(final AbstractArcadList list) {
		super();
		this.list = list;
		setLogger(list.getLogger());
	}

	public abstract int flush();

	public void flushRequest() {
		if (flushImmediat) {
			flush();
		}
	}

	/**
	 * Renvoit
	 * 
	 * @return the active boolean :
	 */
	public boolean isActive() {
		return active;
	}

	/**
	 * Renvoit
	 * 
	 * @return the flushImmediat boolean :
	 */
	public boolean isFlushImmediat() {
		return flushImmediat;
	}

	/**
	 * @param flushImmediat
	 *            the flushImmediat to set
	 */
	public void setFlushImmediat(final boolean flushImmediat) {
		this.flushImmediat = flushImmediat;
	}

}
