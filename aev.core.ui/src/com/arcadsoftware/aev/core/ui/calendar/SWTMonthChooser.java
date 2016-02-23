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

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;

import java.util.Calendar;
import java.util.Locale;
import java.text.DateFormatSymbols;

public class SWTMonthChooser extends Composite implements Listener {
    private SWTDayChooser dayChooser;
    private Combo comboBox;
    private boolean initialized = false;
    private int month;
    private Locale locale;

    public SWTMonthChooser(Composite parent) {
        super(parent, SWT.NONE);

        locale = Locale.getDefault();
        setLayout(new FillLayout());
        comboBox = new Combo(this, SWT.DROP_DOWN | SWT.READ_ONLY);
        comboBox.addListener(SWT.Selection, this);

        initNames();

        initialized = true;
        setMonth(Calendar.getInstance().get(Calendar.MONTH));
    }

    public void initNames() {
        DateFormatSymbols dateFormatSymbols = new DateFormatSymbols(locale);
        String[] monthNames = dateFormatSymbols.getMonths();

        if (comboBox.getItemCount() == 12) {
            comboBox.removeAll();
        }
        for (int i = 0; i < 12; i++) {
            comboBox.add(monthNames[i]);
        }

        comboBox.select(month);
    }

    private void setMonth(int newMonth, boolean select) {
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

    public void setMonth(int newMonth) {
        setMonth(newMonth, true);
    }

    public int getMonth() {
        return month;
    }

    public void handleEvent(Event event) {
        int index = comboBox.getSelectionIndex();
        if (index >= 0) {
            setMonth(index, false);
        }
    }

    public void setDayChooser(SWTDayChooser dayChooser) {
		this.dayChooser = dayChooser;
	}

    public void setLocale(Locale locale) {
        this.locale = locale;
        initNames();
    }
}
