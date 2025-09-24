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
 *  SWTYearChooser.java  - A year chooser component for SWT
 *  Mark Bryan Yu
 *  swtcalendar.sourceforge.net
 *
 *  This program is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU Lesser General Public License
 *  as published by the Free Software Foundation; either version 2
 *  of the License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 */
package com.arcadsoftware.aev.core.ui.calendar;

import java.util.Calendar;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

public class SWTYearChooser extends Spinner implements Listener {
	private SWTDayChooser dayChooser;
	private int year;

	public SWTYearChooser(final Composite parent) {
		super(parent, SWT.NONE);
		final Calendar calendar = Calendar.getInstance();
		dayChooser = null;
		setMinimum(calendar.getMinimum(Calendar.YEAR));
		setMaximum(calendar.getMaximum(Calendar.YEAR));
		setSelection(calendar.get(Calendar.YEAR));
		addListener(SWT.Selection, this);
	}

	public int getYear() {
		return year;
	}

	@Override
	public void handleEvent(final Event event) {
		setValue(getSelection());
	}

	public void setDayChooser(final SWTDayChooser dayChooser) {
		this.dayChooser = dayChooser;
	}

	protected void setValue(final int newValue) {
		year = newValue;
		super.setSelection(year);
		if (dayChooser != null) {
			dayChooser.setYear(newValue);
		}
	}

	public void setYear(final int y) {
		super.setSelection(y);
		if (dayChooser != null) {
			dayChooser.setYear(y);
		}
	}

}
