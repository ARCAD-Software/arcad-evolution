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

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.SWT;

import java.util.Calendar;

public class SWTYearChooser extends Spinner implements Listener {
    private SWTDayChooser dayChooser;
    private int year;

    public SWTYearChooser(Composite parent) {
        super(parent, SWT.NONE);
        Calendar calendar = Calendar.getInstance();
        dayChooser = null;
        setMinimum(calendar.getMinimum(Calendar.YEAR));
        setMaximum(calendar.getMaximum(Calendar.YEAR));
        setSelection(calendar.get(Calendar.YEAR));
        addListener(SWT.Selection, this);
    }

    protected void setValue(int newValue) {
        year = newValue;
        super.setSelection(year);
        if (dayChooser != null)
            dayChooser.setYear(newValue);
    }

    public void setYear(int y) {
        super.setSelection(y);
        if (dayChooser != null) {
			dayChooser.setYear(y);
		}
    }

    public int getYear() {
        return year;
    }

    public void setDayChooser(SWTDayChooser dayChooser) {
        this.dayChooser = dayChooser;
    }

    public void handleEvent(Event event) {
        setValue(getSelection());
    }

}
