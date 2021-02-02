/*
 *  SWTCalendar.java  - A calendar component for SWT
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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;

public class SWTCalendar extends Composite {

	private final SWTDayChooser dayChooser;

	private final List<SWTCalendarListener> listeners;
	private final SWTMonthChooser monthChooser;
	private final SWTYearChooser yearChooser;

	public SWTCalendar(final Composite parent) {
		this(parent, SWT.NONE);
	}

	public SWTCalendar(final Composite parent, final int style) {
		super(parent, style);

		listeners = new ArrayList<>();

		final GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
		gridLayout.marginHeight = 2;
		gridLayout.marginWidth = 2;
		gridLayout.horizontalSpacing = 2;
		gridLayout.verticalSpacing = 2;
		setLayout(gridLayout);

		monthChooser = new SWTMonthChooser(this);
		GridData data = new GridData();
		data.horizontalAlignment = GridData.BEGINNING;
		monthChooser.setLayoutData(data);

		yearChooser = new SWTYearChooser(this);
		data = new GridData();
		data.horizontalAlignment = GridData.END;
		yearChooser.setLayoutData(data);

		dayChooser = new SWTDayChooser(this, this);
		data = new GridData();
		data.horizontalAlignment = GridData.FILL;
		data.horizontalSpan = 2;
		data.grabExcessHorizontalSpace = true;
		dayChooser.setLayoutData(data);
		monthChooser.setDayChooser(dayChooser);
		yearChooser.setDayChooser(dayChooser);
	}

	public void addSWTCalendarListener(final SWTCalendarListener listener) {
		listeners.add(listener);
	}

	public Calendar getCalendar() {
		return dayChooser.getCalendar();
	}

	public void notifyListeners(final Calendar calendar) {
		final Iterator<SWTCalendarListener> i = listeners.iterator();
		while (i.hasNext()) {
			final SWTCalendarListener l = i.next();
			final Event event = new Event();
			event.widget = SWTCalendar.this;
			event.display = SWTCalendar.this.getDisplay();
			event.time = (int) new Date().getTime();
			event.data = calendar;// dayChooser.getCalendar();
			l.dateChanged(new SWTCalendarEvent(event));
		}
	}

	public void removeSWTCalendarListener(final SWTCalendarListener listener) {
		listeners.remove(listener);
	}

	public void setCalendar(final Calendar c) {
		setCalendar(c, true);
	}

	private void setCalendar(final Calendar c, final boolean update) {
		if (update) {
			yearChooser.setYear(c.get(Calendar.YEAR));
			monthChooser.setMonth(c.get(Calendar.MONTH));
			dayChooser.setDay(c.get(Calendar.DATE));
		}
	}

	public void setLocale(final Locale locale) {
		monthChooser.setLocale(locale);
		dayChooser.setLocale(locale);
	}
}
