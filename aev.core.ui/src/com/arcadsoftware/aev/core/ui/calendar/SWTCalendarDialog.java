/*
 * Créé le 14 mai 2004
 * Projet : org.vafada.swtcalendar
 * <i> Copyright 2004, Arcad-Software.</i>
 *  
 */
package com.arcadsoftware.aev.core.ui.calendar;

import java.util.Calendar;
import java.util.GregorianCalendar;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

import com.arcadsoftware.aev.core.ui.EvolutionCoreUIPlugin;
import com.arcadsoftware.aev.core.ui.tools.CoreUILabels;


/**
 * @author mlafon
 * @version 1.0.0
 * 
 * 
 */
public class SWTCalendarDialog extends Dialog {


	private SWTCalendar calendarControl;
	private SWTTimeChooser timeChooser;
	private Calendar calendar;


	/**
	 * Ouvre une fenètre de saisie de date.
	 * 
	 * @param parentShell
	 * @param calendar
	 * @return
	 */
	public static Calendar showCalendarDialog(Shell parentShell,Calendar calendar) {
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
	public SWTCalendarDialog(Shell parentShell) {
		super(parentShell);
		setBlockOnOpen(true);
		
		calendar = GregorianCalendar.getInstance();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.dialogs.Dialog#createDialogArea(org.eclipse.swt.widgets.Composite)
	 */
	protected Control createDialogArea(Composite parent) {
		Composite composite = (Composite)super.createDialogArea(parent);

		calendarControl = new SWTCalendar(composite, SWT.BORDER);
		calendarControl.setCalendar(calendar);

		timeChooser = new SWTTimeChooser(composite,CoreUILabels.resString("Label.Hour")); //$NON-NLS-1$
		timeChooser.setTime(calendar);

		return composite;
	}

	public void setCalendar(Calendar calendar) {
		this.calendar = calendar;
		if ((calendarControl != null) && !calendarControl.isDisposed()) {
			calendarControl.setCalendar(calendar);
			timeChooser.setTime(calendar);
		}
	}
	
	public Calendar getCalendar() {
		return calendar;
	}		

	/* (non-Javadoc)
	 * @see org.eclipse.jface.dialogs.Dialog#okPressed()
	 */
	protected void okPressed() {
		if (calendarControl != null)
			calendar = timeChooser.getTime(calendarControl.getCalendar());
		super.okPressed();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.window.Window#configureShell(org.eclipse.swt.widgets.Shell)
	 */
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText(CoreUILabels.resString("Label.Calendar")); //$NON-NLS-1$
		newShell.setImage(CoreUILabels.getImage(EvolutionCoreUIPlugin.CALENDAR_ICON));
	}

}
