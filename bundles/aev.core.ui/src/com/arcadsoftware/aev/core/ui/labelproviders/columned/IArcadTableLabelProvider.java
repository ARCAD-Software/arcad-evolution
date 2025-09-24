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
package com.arcadsoftware.aev.core.ui.labelproviders.columned;

import org.eclipse.jface.viewers.ITableLabelProvider;

public interface IArcadTableLabelProvider extends ITableLabelProvider {
	/*
	 * (non-Javadoc) To override to sort column by the values instead of the string values
	 * @see org.eclipse.jface.viewers.ITableLabelProvider#getColumnText(java.lang .Object, int)
	 */
	Object getColumnValue(Object element, int columnIndex);
}
