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
 *  SWTMonthChooser.java  - A month chooser component for SWT
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

import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.Locale;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

public class SWTMonthChooser extends Composite implements Listener {
	private final Combo comboBox;
	private SWTDayChooser dayChooser;
	private boolean initialized = false;
	private Locale locale;
	private int month;

	public SWTMonthChooser(final Composite parent) {
		super(parent, SWT.NONE);

		locale = Locale.getDefault();
		setLayout(new FillLayout());
		comboBox = new Combo(this, SWT.DROP_DOWN | SWT.READ_ONLY);
		comboBox.addListener(SWT.Selection, this);

		initNames();

		initialized = true;
		setMonth(Calendar.getInstance().get(Calendar.MONTH));
	}

	public int getMonth() {
		return month;
	}

	@Override
	public void handleEvent(final Event event) {
		final int index = comboBox.getSelectionIndex();
		if (index >= 0) {
			setMonth(index, false);
		}
	}

	public void initNames() {
		final DateFormatSymbols dateFormatSymbols = new DateFormatSymbols(locale);
		final String[] monthNames = dateFormatSymbols.getMonths();

		if (comboBox.getItemCount() == 12) {
			comboBox.removeAll();
		}
		for (int i = 0; i < 12; i++) {
			comboBox.add(monthNames[i]);
		}

		comboBox.select(month);
	}

	public void setDayChooser(final SWTDayChooser dayChooser) {
		this.dayChooser = dayChooser;
	}

	public void setLocale(final Locale locale) {
		this.locale = locale;
		initNames();
	}

	public void setMonth(final int newMonth) {
		setMonth(newMonth, true);
	}

	private void setMonth(final int newMonth, final boolean select) {
		if (!initialized) {
			return;
		}

		month = newMonth;
		if (select) {
			comboBox.select(month);
		}

		if (dayChooser != null) {
			dayChooser.setMonth(month);
		}
	}
}
