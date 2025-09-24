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
package com.arcadsoftware.mmk.lists.impl.fillers;

import com.arcadsoftware.mmk.lists.AbstractArcadList;

public class ExtractListFiller extends ListToListFiller {

	public ExtractListFiller(final AbstractArcadList fromList,
			final AbstractArcadList toList,
			final String extractionQuery) {
		super(fromList, toList, extractionQuery);
	}

}
