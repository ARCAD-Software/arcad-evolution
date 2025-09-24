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
package com.arcadsoftware.aev.core.messages;

import java.util.List;

public interface IMessageDetails {
	String getDescription();

	int getMaxDetailsType();

	/**
	 * @return Hierarchical list of Message Details (each detail entry can possible have embedded sub-detail
	 */
	List<? extends IMessageDetails> getMessageDetails();
}
