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
 * Cr�� le 4 d�c. 2006
 */
package com.arcadsoftware.aev.core.ui.mementos;

import com.arcadsoftware.aev.core.ui.columned.model.ArcadColumn;
import com.arcadsoftware.aev.core.ui.columned.model.ArcadColumns;
import com.arcadsoftware.aev.core.ui.columned.model.ColumnedSortCriteriaList;
import com.arcadsoftware.aev.core.ui.viewers.columned.AbstractColumnedViewer;

/**
 * @author jbeauquis
 */
public class ColumnedViewerSettings extends ArcadSettings {

	private ArcadColumns columns;
	// <FM number="2013/00188" version="08.16.04" date="28 f�vr. 2013 user="md">
	private ColumnedSortCriteriaList sortCriteriaList = null;
	private String viewerId;

	public ColumnedViewerSettings(final String viewerId, final ArcadColumns cols) {
		this("*ALL", "*ALL", viewerId, cols); //$NON-NLS-1$ //$NON-NLS-2$
	}

	/**
	 * @param serverName
	 * @param userName
	 */
	public ColumnedViewerSettings(final String serverName, final String userName) {
		super(serverName, userName);
	}

	/**
	 * @param serverName
	 * @param userName
	 * @param absColViewer
	 */
	public ColumnedViewerSettings(final String serverName, final String userName,
			final AbstractColumnedViewer absColViewer) {
		super(serverName, userName);
		viewerId = absColViewer.getIdentifier();
		columns = absColViewer.getDisplayedColumns();
	}

	public ColumnedViewerSettings(final String serverName, final String userName, final String viewerId) {
		super(serverName, userName);
		this.viewerId = viewerId;
	}

	/**
	 * @param serverName
	 * @param userName
	 * @param viewerId
	 * @param cols
	 */
	public ColumnedViewerSettings(final String serverName, final String userName,
			final String viewerId, final ArcadColumns cols) {
		this(serverName, userName, viewerId);
		columns = cols;
	}

	public ArcadColumn getColumn(final int index) {
		return columns.items(index);
	}

	/**
	 * @return Renvoie columns.
	 */
	public ArcadColumns getColumns() {
		return columns;
	}

	public ColumnedSortCriteriaList getSortCriteriaList() {
		return sortCriteriaList;
	}

	/**
	 * @return Renvoie viewerId.
	 */
	public String getViewerId() {
		return viewerId;
	}

	/**
	 * @param columns
	 *            columns � d�finir.
	 */
	public void setColumns(final ArcadColumns columns) {
		this.columns = columns;
	}

	public void setSortCriteriaList(final ColumnedSortCriteriaList sortCriteriaList) {
		this.sortCriteriaList = sortCriteriaList;
	}
	// </FM>
}
