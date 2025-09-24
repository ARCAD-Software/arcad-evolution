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
 *  SWTDayChooser.java  - A day chooser component for SWT
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
import java.util.Date;
import java.util.Locale;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

public class SWTDayChooser extends Composite implements MouseListener {
	private Calendar calendar;
	private Color colorBlue;
	private Color colorRed;
	private int day;
	private String dayNames[];
	private final CLabel days[];
	private Locale locale;
	private SWTCalendar mainCalendar;
	private Color oldDayBackgroundColor;
	private Color selectedColor;
	private CLabel selectedDay;
	private final Calendar today;

	public SWTDayChooser(final Composite parent) {
		super(parent, SWT.NONE);
		locale = Locale.getDefault();
		days = new CLabel[49];
		selectedDay = null;
		final Calendar calendar = Calendar.getInstance(locale);
		today = (Calendar) calendar.clone();

		final GridLayout gridLayout = new GridLayout();
		gridLayout.makeColumnsEqualWidth = true;
		gridLayout.numColumns = 7;
		gridLayout.marginHeight = 0;
		gridLayout.marginWidth = 0;
		gridLayout.horizontalSpacing = 0;
		gridLayout.verticalSpacing = 0;
		setLayout(gridLayout);

		for (int y = 0; y < 7; y++) {
			for (int x = 0; x < 7; x++) {
				final int index = x + 7 * y;
				if (y == 0) {
					days[index] = new CLabel(this, SWT.SHADOW_IN);
					if (oldDayBackgroundColor == null) {
						oldDayBackgroundColor = days[index].getBackground();
					}
					final GridData data = new GridData();
					data.horizontalAlignment = GridData.FILL;
					days[index].setLayoutData(data);
					days[index].addMouseListener(this);
				} else {
					days[index] = new CLabel(this, SWT.SHADOW_IN);
					days[index].setText("x"); //$NON-NLS-1$
					final GridData data = new GridData();
					data.horizontalAlignment = GridData.FILL;
					days[index].setLayoutData(data);
					days[index].addMouseListener(this);
				}
			}
		}
		init();
		setDay(Calendar.getInstance().get(Calendar.DAY_OF_MONTH));

		addDisposeListener(event -> {
			oldDayBackgroundColor.dispose();
			selectedColor.dispose();
			colorRed.dispose();
			colorBlue.dispose();
		});
	}

	public SWTDayChooser(final Composite parent, final SWTCalendar swtCalendar) {
		this(parent);
		mainCalendar = swtCalendar;
	}

	protected void drawDays() {
		final Calendar tmpCalendar = (Calendar) calendar.clone();
		final int firstDayOfWeek = tmpCalendar.getFirstDayOfWeek();
		tmpCalendar.set(Calendar.DAY_OF_MONTH, 1);

		int firstDay = tmpCalendar.get(Calendar.DAY_OF_WEEK) - firstDayOfWeek;
		if (firstDay < 0) {
			firstDay += 7;
		}

		int i;

		for (i = 0; i < firstDay; i++) {
			days[i + 7].setVisible(false);
			days[i + 7].setText(""); //$NON-NLS-1$
		}

		tmpCalendar.add(Calendar.MONTH, 1);
		final Date firstDayInNextMonth = tmpCalendar.getTime();
		tmpCalendar.add(Calendar.MONTH, -1);

		Date day = tmpCalendar.getTime();
		int n = 0;
		final Color foregroundColor = getForeground();
		while (day.before(firstDayInNextMonth)) {
			days[i + n + 7].setText(Integer.toString(n + 1));
			days[i + n + 7].setVisible(true);
			if (tmpCalendar.get(Calendar.DAY_OF_YEAR) == today.get(Calendar.DAY_OF_YEAR) &&
					tmpCalendar.get(Calendar.YEAR) == today.get(Calendar.YEAR)) {
				days[i + n + 7].setForeground(colorRed);
			} else {
				days[i + n + 7].setForeground(foregroundColor);
			}

			if (n + 1 == this.day) {
				days[i + n + 7].setBackground(selectedColor);
				selectedDay = days[i + n + 7];
			} else {
				days[i + n + 7].setBackground(oldDayBackgroundColor);
			}

			n++;
			tmpCalendar.add(Calendar.DATE, 1);
			day = tmpCalendar.getTime();
		}

		for (int k = n + i + 7; k < 49; k++) {
			days[k].setVisible(false);
			days[k].setText(""); //$NON-NLS-1$
		}
	}

	public Calendar getCalendar() {
		return calendar;
	}

	public int getDay() {
		return day;
	}

	protected void init() {
		colorRed = new Color(super.getDisplay(), 164, 0, 0);
		colorBlue = new Color(super.getDisplay(), 0, 0, 164);
		selectedColor = new Color(super.getDisplay(), 160, 160, 160);

		calendar = Calendar.getInstance(locale);
		final int firstDayOfWeek = calendar.getFirstDayOfWeek();
		final DateFormatSymbols dateFormatSymbols = new DateFormatSymbols(locale);
		dayNames = dateFormatSymbols.getShortWeekdays();
		int day = firstDayOfWeek;
		for (int i = 0; i < 7; i++) {
			days[i].setText(dayNames[day]);
			days[i].removeMouseListener(this);
			if (day == 1) {
				days[i].setForeground(colorRed);
			} else {
				days[i].setForeground(colorBlue);
			}

			if (day < 7) {
				day++;
			} else {
				day -= 6;
			}

		}
		drawDays();
	}

	@Override
	public void mouseDoubleClick(final MouseEvent event) {
	}

	@Override
	public void mouseDown(final MouseEvent event) {
		if (event.button == 1) { // left click
			final CLabel label = (CLabel) event.widget;
			final String buttonText = label.getText();
			final int day = new Integer(buttonText);
			setDay(day);
		}
	}

	@Override
	public void mouseUp(final MouseEvent event) {
	}

	private void notifyListeners() {
		if (mainCalendar != null) {
			mainCalendar.notifyListeners(calendar);
		}
	}

	public void setCalendar(final Calendar c) {
		calendar = c;
		drawDays();
		notifyListeners();
	}

	public void setDay(int d) {
		if (d < 1) {
			d = 1;
		}

		final Calendar tmpCalendar = (Calendar) calendar.clone();
		tmpCalendar.set(Calendar.DAY_OF_MONTH, 1);
		tmpCalendar.add(Calendar.MONTH, 1);
		tmpCalendar.add(Calendar.DATE, -1);
		final int maxDaysInMonth = tmpCalendar.get(Calendar.DATE);

		if (d > maxDaysInMonth) {
			d = maxDaysInMonth;
		}

		day = d;

		if (selectedDay != null) {
			selectedDay.setBackground(oldDayBackgroundColor);
			selectedDay.redraw();

		}

		for (int i = 7; i < 49; i++) {
			if (days[i].getText().equals(Integer.toString(day))) {
				selectedDay = days[i];
				selectedDay.setBackground(selectedColor);
				break;
			}
		}
		calendar.set(Calendar.DATE, d);
		notifyListeners();
	}

	public void setLocale(final Locale locale) {
		this.locale = locale;
		init();
	}

	public void setMonth(final int month) {
		calendar.set(Calendar.MONTH, month);
		setDay(day);
		drawDays();
		notifyListeners();
	}

	public void setYear(final int year) {
		calendar.set(Calendar.YEAR, year);
		drawDays();
		notifyListeners();
	}
}
