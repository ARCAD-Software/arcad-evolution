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
package com.arcadsoftware.ae.core.logger.messages;

import java.util.ArrayList;

public class Messages {
	ArrayList<AbstractMessage> list = new ArrayList<>();

	public void add(final AbstractMessage message) {
		list.add(message);
	}

	public void clear() {
		list.clear();
	}

	public AbstractMessage messageAt(final int index) {
		if (index > -1 && index < messageCount()) {
			return list.get(index);
		}
		return null;
	}

	public int messageCount() {
		return list.size();
	}
}
