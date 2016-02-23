/*
 * Créé le 18 déc. 07
 */
package com.arcadsoftware.aev.core.ui.dialogs.gui;

import java.util.Calendar;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

import com.arcadsoftware.aev.core.ui.EvolutionCoreUIPlugin;
import com.arcadsoftware.aev.core.ui.calendar.SWTCalendar;
import com.arcadsoftware.aev.core.ui.calendar.SWTCalendarDialog;
import com.arcadsoftware.aev.core.ui.calendar.SWTTimeChooser;
import com.arcadsoftware.aev.core.ui.tools.CoreUILabels;


public class CalendarDialog extends Dialog {

	private SWTCalendar calendarControl;
	private SWTTimeChooser timeChooser;
	private Calendar calendar;

	boolean showTime = true;
	boolean showDate = true;

	/**
	 * Ouvre une fenètre de saisie de date.
	 * 
	 * @param parentShell
	 * @param calendar
	 * @return Calendar
	 */
	public static Calendar showCalendarDialog(Shell parentShell, Calendar calendar) {
		SWTCalendarDialog dialog = new SWTCalendarDialog(parentShell);
		dialog.setCalendar(calendar);
		if (dialog.open() == OK) {
			return dialog.getCalendar();
		}
		return calendar;
	}

	/**
	 * @param parentShell
	 */
	public CalendarDialog(Shell parentShell, boolean showDate, boolean showTime) {
		super(parentShell);
		setBlockOnOpen(true);
		calendar = Calendar.getInstance();
		this.showTime = showTime;
		this.showDate = showDate;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.dialogs.Dialog#createDialogArea(org.eclipse.swt.widgets
	 * .Composite)
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite composite = (Composite) super.createDialogArea(parent);
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

	public void setCalendar(Calendar calendar) {
		this.calendar = calendar;
		if ((calendarControl != null) && !calendarControl.isDisposed()) {
			if (showDate)
				calendarControl.setCalendar(calendar);
			if (showTime)
				timeChooser.setTime(calendar);
		}
	}

	public Calendar getCalendar() {
		return calendar;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.dialogs.Dialog#okPressed()
	 */
	@Override
	protected void okPressed() {

		if (showDate)
			calendar = calendarControl.getCalendar();
		if (showTime)
			calendar = timeChooser.getTime(timeChooser.getTime(calendar));

		super.okPressed();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.window.Window#configureShell(org.eclipse.swt.widgets
	 * .Shell)
	 */
	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText(CoreUILabels.resString("Label.Calendar")); //$NON-NLS-1$
		newShell.setImage(CoreUILabels.getImage(EvolutionCoreUIPlugin.CALENDAR_ICON));
	}

}
