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
 * Cr�� le 18 d�c. 07
 */
package com.arcadsoftware.aev.core.ui.dialogs.gui;

import java.util.Calendar;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

import com.arcadsoftware.aev.core.ui.calendar.SWTCalendar;
import com.arcadsoftware.aev.core.ui.calendar.SWTCalendarDialog;
import com.arcadsoftware.aev.core.ui.calendar.SWTTimeChooser;
import com.arcadsoftware.aev.core.ui.tools.CoreUILabels;
import com.arcadsoftware.aev.icons.Icon;

public class CalendarDialog extends Dialog {

	/**
	 * Ouvre une fen�tre de saisie de date.
	 *
	 * @param parentShell
	 * @param calendar
	 * @return Calendar
	 */
	public static Calendar showCalendarDialog(final Shell parentShell, final Calendar calendar) {
		final SWTCalendarDialog dialog = new SWTCalendarDialog(parentShell);
		dialog.setCalendar(calendar);
		if (dialog.open() == OK) {
			return dialog.getCalendar();
		}
		return calendar;
	}

	private Calendar calendar;
	private SWTCalendar calendarControl;

	boolean showDate = true;
	boolean showTime = true;

	private SWTTimeChooser timeChooser;

	/**
	 * @param parentShell
	 */
	public CalendarDialog(final Shell parentShell, final boolean showDate, final boolean showTime) {
		super(parentShell);
		setBlockOnOpen(true);
		calendar = Calendar.getInstance();
		this.showTime = showTime;
		this.showDate = showDate;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.jface.window.Window#configureShell(org.eclipse.swt.widgets .Shell)
	 */
	@Override
	protected void configureShell(final Shell newShell) {
		super.configureShell(newShell);
		newShell.setText(CoreUILabels.resString("Label.Calendar")); //$NON-NLS-1$
		newShell.setImage(Icon.CALENDAR_SELECT_DAY.image());
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.jface.dialogs.Dialog#createDialogArea(org.eclipse.swt.widgets .Composite)
	 */
	@Override
	protected Control createDialogArea(final Composite parent) {
		final Composite composite = (Composite) super.createDialogArea(parent);
		if (showDate) {
			calendarControl = new SWTCalendar(composite, SWT.BORDER);
			calendarControl.setCalendar(calendar);
		}
		if (showTime) {
			timeChooser = new SWTTimeChooser(composite, CoreUILabels.resString("Label.Hour")); //$NON-NLS-1$
			timeChooser.setTime(calendar);
		}
		return composite;
	}

	public Calendar getCalendar() {
		return calendar;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.jface.dialogs.Dialog#okPressed()
	 */
	@Override
	protected void okPressed() {

		if (showDate) {
			calendar = calendarControl.getCalendar();
		}
		if (showTime) {
			calendar = timeChooser.getTime(timeChooser.getTime(calendar));
		}

		super.okPressed();
	}

	public void setCalendar(final Calendar calendar) {
		this.calendar = calendar;
		if (calendarControl != null && !calendarControl.isDisposed()) {
			if (showDate) {
				calendarControl.setCalendar(calendar);
			}
			if (showTime) {
				timeChooser.setTime(calendar);
			}
		}
	}

}
