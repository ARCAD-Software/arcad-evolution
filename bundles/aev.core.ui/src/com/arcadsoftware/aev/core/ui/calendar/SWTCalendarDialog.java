/*
 * Créé le 14 mai 2004
 * Projet : org.vafada.swtcalendar
 * <i> Copyright 2004, Arcad-Software.</i>
 *
 */
package com.arcadsoftware.aev.core.ui.calendar;

import java.util.Calendar;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

import com.arcadsoftware.aev.core.ui.tools.CoreUILabels;
import com.arcadsoftware.aev.icons.Icon;

/**
 * @author mlafon
 * @version 1.0.0
 */
public class SWTCalendarDialog extends Dialog {

	/**
	 * Ouvre une fenètre de saisie de date.
	 *
	 * @param parentShell
	 * @param calendar
	 * @return
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

	private SWTTimeChooser timeChooser;

	/**
	 * @param parentShell
	 */
	public SWTCalendarDialog(final Shell parentShell) {
		super(parentShell);
		setBlockOnOpen(true);

		calendar = Calendar.getInstance();
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.jface.window.Window#configureShell(org.eclipse.swt.widgets.Shell)
	 */
	@Override
	protected void configureShell(final Shell newShell) {
		super.configureShell(newShell);
		newShell.setText(CoreUILabels.resString("Label.Calendar")); //$NON-NLS-1$
		newShell.setImage(Icon.CALENDAR_SELECT_DAY.image());
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.jface.dialogs.Dialog#createDialogArea(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	protected Control createDialogArea(final Composite parent) {
		final Composite composite = (Composite) super.createDialogArea(parent);

		calendarControl = new SWTCalendar(composite, SWT.BORDER);
		calendarControl.setCalendar(calendar);

		timeChooser = new SWTTimeChooser(composite, CoreUILabels.resString("Label.Hour")); //$NON-NLS-1$
		timeChooser.setTime(calendar);

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
		if (calendarControl != null) {
			calendar = timeChooser.getTime(calendarControl.getCalendar());
		}
		super.okPressed();
	}

	public void setCalendar(final Calendar calendar) {
		this.calendar = calendar;
		if (calendarControl != null && !calendarControl.isDisposed()) {
			calendarControl.setCalendar(calendar);
			timeChooser.setTime(calendar);
		}
	}

}
