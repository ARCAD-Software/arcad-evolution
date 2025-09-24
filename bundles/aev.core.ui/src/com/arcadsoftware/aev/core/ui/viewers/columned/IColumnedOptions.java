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
package com.arcadsoftware.aev.core.ui.viewers.columned;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ICellModifier;

/**
 * @author MD
 */
public interface IColumnedOptions {
	Object[] getColumnProperties();

	boolean getHeaderVisible();

	boolean getLinesVisible();

	void setCellEditors(CellEditor[] editors);

	void setCellModifier(ICellModifier modifier);

	void setColumnProperties(String[] columnProperties);

	void setHeaderVisible(boolean show);

	void setLinesVisible(boolean show);

}
