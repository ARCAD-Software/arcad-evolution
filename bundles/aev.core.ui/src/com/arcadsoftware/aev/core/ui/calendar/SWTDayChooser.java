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

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.graphics.Color;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.text.DateFormatSymbols;

public class SWTDayChooser extends Composite implements MouseListener {
    private CLabel days[];
    private CLabel selectedDay;
    private int day;
    private Color oldDayBackgroundColor;
    private Color selectedColor;
    private Color colorRed;
    private Color colorBlue;
    private String dayNames[];
    private Calendar calendar;
    private Calendar today;
    private Locale locale;
    private SWTCalendar mainCalendar;

    public SWTDayChooser(Composite parent, SWTCalendar swtCalendar) {
        this(parent);
        this.mainCalendar = swtCalendar;
    }

    public SWTDayChooser(Composite parent) {
        super(parent, SWT.NONE);
        locale = Locale.getDefault();
        days = new CLabel[49];
        selectedDay = null;
        Calendar calendar = Calendar.getInstance(locale);
        today = (Calendar) calendar.clone();

        GridLayout gridLayout = new GridLayout();
        gridLayout.makeColumnsEqualWidth = true;
        gridLayout.numColumns = 7;
        gridLayout.marginHeight = 0;
        gridLayout.marginWidth = 0;
        gridLayout.horizontalSpacing = 0;
        gridLayout.verticalSpacing = 0;
        setLayout(gridLayout);

        for (int y = 0; y < 7; y++) {
            for (int x = 0; x < 7; x++) {
                int index = x + 7 * y;
                if (y == 0) {
                    days[index] = new CLabel(this, SWT.SHADOW_IN);
                    if (oldDayBackgroundColor == null)
                        oldDayBackgroundColor = days[index].getBackground();
                    GridData data = new GridData();
                    data.horizontalAlignment = GridData.FILL;
                    days[index].setLayoutData(data);
                    days[index].addMouseListener(this);
                } else {
                    days[index] = new CLabel(this, SWT.SHADOW_IN);
                    days[index].setText("x"); //$NON-NLS-1$
                    GridData data = new GridData();
                    data.horizontalAlignment = GridData.FILL;
                    days[index].setLayoutData(data);
                    days[index].addMouseListener(this);
                }
            }
        }
        init();
        setDay(Calendar.getInstance().get(Calendar.DAY_OF_MONTH));

        this.addDisposeListener(new DisposeListener() {
            public void widgetDisposed(DisposeEvent event) {
                oldDayBackgroundColor.dispose();
                selectedColor.dispose();
                colorRed.dispose();
                colorBlue.dispose();
            }
        });
    }

    protected void init() {
        colorRed = new Color(super.getDisplay(), 164, 0, 0);
        colorBlue = new Color(super.getDisplay(), 0, 0, 164);
        selectedColor = new Color(super.getDisplay(), 160, 160, 160);

        calendar = Calendar.getInstance(locale);
        int firstDayOfWeek = calendar.getFirstDayOfWeek();
        DateFormatSymbols dateFormatSymbols = new DateFormatSymbols(locale);
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

    protected void drawDays() {
        Calendar tmpCalendar = (Calendar) calendar.clone();
        int firstDayOfWeek = tmpCalendar.getFirstDayOfWeek();
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
        Date firstDayInNextMonth = tmpCalendar.getTime();
        tmpCalendar.add(Calendar.MONTH, -1);

        Date day = tmpCalendar.getTime();
        int n = 0;
        Color foregroundColor = getForeground();
        while (day.before(firstDayInNextMonth)) {
            days[i + n + 7].setText(Integer.toString(n + 1));
            days[i + n + 7].setVisible(true);
            if (tmpCalendar.get(Calendar.DAY_OF_YEAR) ==
                    today.get(Calendar.DAY_OF_YEAR) &&
                    tmpCalendar.get(Calendar.YEAR) ==
                    today.get(Calendar.YEAR)) {
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

    public void setDay(int d) {
        if (d < 1) {
            d = 1;
        }

        Calendar tmpCalendar = (Calendar) calendar.clone();
        tmpCalendar.set(Calendar.DAY_OF_MONTH, 1);
        tmpCalendar.add(Calendar.MONTH, 1);
        tmpCalendar.add(Calendar.DATE, -1);
        int maxDaysInMonth = tmpCalendar.get(Calendar.DATE);

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

    public int getDay() {
        return day;
    }

    public void setMonth(int month) {
        calendar.set(Calendar.MONTH, month);
        setDay(day);
        drawDays();
        notifyListeners();
    }

    public void setYear(int year) {
        calendar.set(Calendar.YEAR, year);
        drawDays();
        notifyListeners();
    }

    public void setCalendar(Calendar c) {
        calendar = c;
        drawDays();
        notifyListeners();
    }

    public void mouseDown(MouseEvent event) {
        if (event.button == 1) { //left click
            CLabel label = (CLabel) event.widget;
            String buttonText = label.getText();
            int day = new Integer(buttonText).intValue();
            setDay(day);
        }
    }

    public void mouseDoubleClick(MouseEvent event) {
    }

    public void mouseUp(MouseEvent event) {
    }

    private void notifyListeners() {
        if (mainCalendar != null)
            mainCalendar.notifyListeners(calendar);
    }

    public Calendar getCalendar() {
        return calendar;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
        init();
    }
}
