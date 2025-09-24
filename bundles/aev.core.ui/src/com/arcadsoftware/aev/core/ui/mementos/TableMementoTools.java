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
 * Created on 13 f�vr. 2006
 */
package com.arcadsoftware.aev.core.ui.mementos;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.eclipse.ui.IMemento;
import org.eclipse.ui.WorkbenchException;
import org.eclipse.ui.XMLMemento;

import com.arcadsoftware.aev.core.messages.MessageDetail;
import com.arcadsoftware.aev.core.messages.MessageManager;
import com.arcadsoftware.aev.core.ui.columned.model.ArcadColumn;
import com.arcadsoftware.aev.core.ui.columned.model.ArcadColumns;

/**
 * @author MD
 */
public class TableMementoTools {

	public static final TableMementoTools instance = new TableMementoTools();

	public static boolean getViewerDef(final String filename, final ArcadColumns columns) {
		try {
			columns.clear();
			final XMLMemento x = XMLMemento.createReadRoot(new FileReader(filename));
			final IMemento[] columnDefs = x.getChildren("columnDef"); //$NON-NLS-1$
			for (final IMemento cd : columnDefs) {
				final String property = cd.getString("property"); //$NON-NLS-1$
				final String name = cd.getString("label"); //$NON-NLS-1$
				final int visible = cd.getInteger("visible"); //$NON-NLS-1$
				final int width = cd.getInteger("width"); //$NON-NLS-1$
				final int position = cd.getInteger("position"); //$NON-NLS-1$
				final int actualIndex = cd.getInteger("actualPosition"); //$NON-NLS-1$
				final ArcadColumn c = new ArcadColumn(property, name, visible, position, width, actualIndex);
				columns.add(c);
			}
		} catch (final WorkbenchException e) {
			MessageManager.addException(e, MessageManager.LEVEL_PRODUCTION).addDetail(MessageDetail.ERROR,
					"TableMementoTools.getViewerDef");//$NON-NLS-1$
		} catch (final FileNotFoundException e) {
			// Do nothing
		}
		return true;
	}

	public static boolean setViewerDef(final String filename, final ArcadColumns columns) {
		// Enregistrement du fichier
		final XMLMemento x = XMLMemento.createWriteRoot("element"); //$NON-NLS-1$
		for (int i = 0; i < columns.count(); i++) {
			final ArcadColumn c = columns.items(i);
			final IMemento columnDef = x.createChild("columnDef"); //$NON-NLS-1$
			columnDef.putString("property", c.getIdentifier());//$NON-NLS-1$
			columnDef.putString("label", c.getIdentifier());//$NON-NLS-1$
			columnDef.putInteger("visible", c.getVisible());//$NON-NLS-1$
			columnDef.putInteger("width", c.getWidth()); //$NON-NLS-1$
			columnDef.putInteger("position", c.getPosition());//$NON-NLS-1$
		}
		try {
			x.save(new FileWriter(filename));
			return true;
		} catch (final IOException e) {
			MessageManager.addException(e, MessageManager.LEVEL_PRODUCTION).addDetail(MessageDetail.ERROR,
					"File : " + filename);//$NON-NLS-1$
		}
		return false;
	}

	/**
	 *
	 */
	private TableMementoTools() {
		super();
	}

}
