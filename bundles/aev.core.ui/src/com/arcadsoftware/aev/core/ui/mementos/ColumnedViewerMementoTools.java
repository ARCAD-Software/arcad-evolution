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
 * Cr�� le 5 d�c. 2006
 */
package com.arcadsoftware.aev.core.ui.mementos;

import java.io.File;
import java.io.IOException;

import org.eclipse.ui.IMemento;

import com.arcadsoftware.aev.core.messages.MessageDetail;
import com.arcadsoftware.aev.core.messages.MessageManager;
import com.arcadsoftware.aev.core.tools.StringTools;
import com.arcadsoftware.aev.core.ui.EvolutionCoreUIPlugin;
import com.arcadsoftware.aev.core.ui.columned.model.ArcadColumn;
import com.arcadsoftware.aev.core.ui.columned.model.ArcadColumns;
import com.arcadsoftware.aev.core.ui.columned.model.ColumnedSortCriteria;
import com.arcadsoftware.aev.core.ui.columned.model.ColumnedSortCriteriaList;
import com.arcadsoftware.aev.core.ui.viewers.columned.AbstractColumnedViewer;

/**
 * @author jbeauquis
 */
public class ColumnedViewerMementoTools extends RootAndUserMementoTools {

	private static ColumnedViewerMementoTools instance;

	public static ColumnedViewerMementoTools getInstance() {
		if (instance == null) {
			instance = new ColumnedViewerMementoTools();
			instance.load();
		}
		return instance;
	}

	private String filename = null;

	private String filterViewerId = StringTools.EMPTY;

	private ColumnedViewerMementoTools() {
		super();
	}

	/*
	 * (non-Javadoc)
	 * @see com.arcadsoftware.aev.core.ui.mementos.RootAndUserMementoTools#getFileName ()
	 */
	@Override
	protected String getFileName() {
		if (filename == null) {
			filename = EvolutionCoreUIPlugin.getDefault().getStateLocation().toString()
					+ AbstractColumnedViewer.FILENAME;
		}
		final File directory = new File(EvolutionCoreUIPlugin.getDefault().getStateLocation().toString()
				+ AbstractColumnedViewer.DIRECTORYNAME);
		if (!directory.exists()) {
			directory.mkdir();
		}
		final File file = new File(filename);
		if (!file.exists()) {
			try {
				if(!file.createNewFile()){
					return null;
				}
			} catch (final IOException e) {
				MessageManager.addException(e, MessageManager.LEVEL_PRODUCTION).addDetail(MessageDetail.ERROR,
						"File : " + filename);//$NON-NLS-1$
			}
		}
		return filename;
	}

	/**
	 * @return Renvoie filterViewerId.
	 */
	public String getFilterViewerId() {
		return filterViewerId;
	}

	public ColumnedViewerSettings getViewerSetting(final String identifier) {
		final ColumnedViewerSettings filter = new ColumnedViewerSettings("*ALL", "*ALL", identifier); //$NON-NLS-1$//$NON-NLS-2$
		return (ColumnedViewerSettings) getCurrentSettings(filter);
	}

	/*
	 * (non-Javadoc)
	 * @see com.arcadsoftware.aev.core.ui.mementos.RootAndUserMementoTools#keep(com
	 * .arcadsoftware.aev.core.ui.mementos.ArcadSettings)
	 */
	@Override
	protected boolean keep(final ArcadSettings s) {
		return true;
	}

	/*
	 * (non-Javadoc)
	 * @see com.arcadsoftware.aev.core.ui.mementos.RootAndUserMementoTools#keyEquals
	 * (com.arcadsoftware.aev.core.ui.mementos.ArcadSettings, com.arcadsoftware.aev.core.ui.mementos.ArcadSettings)
	 */
	@Override
	public boolean keyEquals(final ArcadSettings ref, final ArcadSettings toCompare) {
		if (ref instanceof ColumnedViewerSettings && toCompare instanceof ColumnedViewerSettings) {
			final ColumnedViewerSettings r = (ColumnedViewerSettings) ref;
			final ColumnedViewerSettings c = (ColumnedViewerSettings) toCompare;
			return r.getServerName().equalsIgnoreCase(c.getServerName())
					&& r.getUserName().equalsIgnoreCase(c.getUserName())
					&& r.getViewerId().equalsIgnoreCase(c.getViewerId());
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * @see com.arcadsoftware.aev.core.ui.mementos.RootAndUserMementoTools#readKeys (java.lang.String, java.lang.String,
	 * org.eclipse.ui.IMemento)
	 */
	@Override
	public ArcadSettings readKeys(final String serverName, final String userName, final IMemento user) {
		final IMemento colViewer = user.getChild("columnedViewer");//$NON-NLS-1$
		final String colViewerId = colViewer.getString("id");//$NON-NLS-1$
		final IMemento[] cols = colViewer.getChildren("column");//$NON-NLS-1$
		final ArcadColumns arcCols = new ArcadColumns();
		for (final IMemento col : cols) {
			arcCols.add(new ArcadColumn(col.getString("id"), col.getString("label"), //$NON-NLS-1$ //$NON-NLS-2$
					col.getString("userLabel"), col.getInteger("visible"), //$NON-NLS-1$ //$NON-NLS-2$
					col.getInteger("position"), col.getInteger("width")));//$NON-NLS-1$ //$NON-NLS-2$
		}

		final ColumnedViewerSettings settings = new ColumnedViewerSettings(serverName, userName, colViewerId, arcCols);

		final IMemento sortNode = user.getChild("sort");//$NON-NLS-1$
		if (sortNode != null) {
			final ColumnedSortCriteriaList list = new ColumnedSortCriteriaList(arcCols);
			final IMemento[] sortCols = sortNode.getChildren("column");//$NON-NLS-1$
			for (final IMemento sortCol : sortCols) {
				final int id = sortCol.getInteger("id");//$NON-NLS-1$
				final String columnName = sortCol.getString("name");//$NON-NLS-1$
				final String sortOrder = sortCol.getString("order");//$NON-NLS-1$
				final int index = sortCol.getInteger("index");//$NON-NLS-1$
				final ColumnedSortCriteria c = new ColumnedSortCriteria(id, columnName);
				c.setId(id);
				c.setColumnIndex(index);
				c.setSortOrder(sortOrder);
				list.add(c);
			}
			settings.setSortCriteriaList(list);
		}
		return settings;
	}

	/*
	 * (non-Javadoc)
	 * @see com.arcadsoftware.aev.core.ui.mementos.RootAndUserMementoTools#saveKeys (org.eclipse.ui.IMemento,
	 * com.arcadsoftware.aev.core.ui.mementos.ArcadSettings)
	 */
	@Override
	public void saveKeys(final IMemento user, final ArcadSettings s) {
		if (s instanceof ColumnedViewerSettings) {
			final ColumnedViewerSettings colViewerSettings = (ColumnedViewerSettings) s;
			final IMemento colViewer = user.createChild("columnedViewer");//$NON-NLS-1$
			colViewer.putString("id", colViewerSettings.getViewerId());//$NON-NLS-1$
			for (int i = 0; i < colViewerSettings.getColumns().count(); i++) {
				final IMemento aCol = colViewer.createChild("column");//$NON-NLS-1$
				aCol.putString("id", colViewerSettings.getColumn(i).getIdentifier());//$NON-NLS-1$
				aCol.putString("label", colViewerSettings.getColumn(i).getName());//$NON-NLS-1$
				aCol.putString("userLabel", colViewerSettings.getColumn(i).getUserName());//$NON-NLS-1$
				aCol.putInteger("visible", colViewerSettings.getColumn(i).getVisible());//$NON-NLS-1$
				aCol.putInteger("position", colViewerSettings.getColumn(i).getPosition());//$NON-NLS-1$
				aCol.putInteger("width", colViewerSettings.getColumn(i).getWidth());//$NON-NLS-1$
			}
			// <FM number="2013/00188" version="08.16.04" date="28 f�vr. 2013 user="md">
			final IMemento sortNode = user.createChild("sort");//$NON-NLS-1$
			if (colViewerSettings.getSortCriteriaList() != null) {
				final ColumnedSortCriteriaList list = colViewerSettings.getSortCriteriaList();
				for (int i = 0; i < list.getSize(); i++) {
					final IMemento aCol = sortNode.createChild("column");//$NON-NLS-1$
					final ColumnedSortCriteria c = (ColumnedSortCriteria) list.getItems(i);
					aCol.putInteger("id", c.getId());//$NON-NLS-1$
					aCol.putString("name", c.getColumnName());//$NON-NLS-1$
					aCol.putInteger("index", c.getColumnIndex());//$NON-NLS-1$
					aCol.putString("order", c.getSortOrder());//$NON-NLS-1$
				}
			}
			// </FM>

		}
	}

	/**
	 * @param filterViewerId
	 *            filterViewerId � d�finir.
	 */
	public void setFilterViewerId(final String filterViewerId) {
		this.filterViewerId = filterViewerId;
	}
}
